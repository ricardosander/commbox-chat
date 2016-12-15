package br.com.commbox.chat.servidor;

import java.util.concurrent.BlockingQueue;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.model.Mensagem;

public class NotificaEvento implements Runnable {

	private final ConexaoCliente cliente;
	private final BlockingQueue<Mensagem> mensagens;
	private final String mensagem;

	public NotificaEvento(BlockingQueue<Mensagem> mensagens, ConexaoCliente cliente, String mensagem) {
		this.mensagens = mensagens;
		this.cliente = cliente;
		this.mensagem = mensagem;
	}

	@Override
	public void run() {
		try {
			this.mensagens.put(new Mensagem(cliente.getId(), this.mensagem, true));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
