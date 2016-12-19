package br.com.commbox.chat.servidor;

import br.com.commbox.chat.conexao.ConexaoServidorSimples;
import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;

public class UsuariosOnline implements Runnable {

	private final ConexaoServidorSimples servidor;

	public UsuariosOnline(ConexaoServidorSimples conexaoServidorSimples) {
		this.servidor = conexaoServidorSimples;
	}

	@Override
	public void run() {

		StringBuilder builder = new StringBuilder();
		this.servidor.getClientes().forEach(cliente -> {

			if (builder.length() != 0) {
				builder.append(";");
			}
			builder.append(cliente.getId());
		});

		this.servidor.getJanela().escreverUsuariosOnline(builder.toString());

		this.servidor.getClientes().forEach(cliente -> {

			MensagemFactory mensagemFactory = new MensagemFactory();
			Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MESANGEM_USUARIOS_ONLINE, cliente.getId(),
					builder.toString());
			this.servidor.executa(new NotificaCliente(cliente, mensagem));
		});
	}

}
