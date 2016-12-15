package br.com.commbox.chat.servidor;

import java.util.List;
import java.util.concurrent.ExecutorService;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;

public class UsuariosOnline implements Runnable {

	private final List<ConexaoCliente> clientes;
	private final ExecutorService threadPool;

	public UsuariosOnline(ExecutorService threadPool, List<ConexaoCliente> clientes) {
		this.threadPool = threadPool;
		this.clientes = clientes;
	}

	@Override
	public void run() {

		StringBuilder builder = new StringBuilder();
		this.clientes.forEach(cliente -> {
			
			if (builder.length() != 0) {
				builder.append(";");
			}
			builder.append(cliente.getId());
		});
		
		this.clientes.forEach(cliente -> {
			
			MensagemFactory mensagemFactory = new MensagemFactory();
			Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MESANGEM_USUARIOS_ONLINE, cliente.getId(), builder.toString());
			this.threadPool.execute(new NotificaCliente(cliente, mensagem));
		});
	}

}
