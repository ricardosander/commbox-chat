package br.com.commbox.chat.servidor;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.model.Mensagem;

public class NotificaCliente implements Runnable {

	private final ConexaoCliente cliente;
	private final Mensagem mensagem;

	public NotificaCliente(ConexaoCliente cliente, Mensagem mensagem) {
		this.cliente = cliente;
		this.mensagem = mensagem;
	}

	@Override
	public void run() {

		synchronized (mensagem) {
			this.mensagem.setDestino(this.cliente.getId());
			this.cliente.escrever(mensagem.toString());
		}
	}

}
