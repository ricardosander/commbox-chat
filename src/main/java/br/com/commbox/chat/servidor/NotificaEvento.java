package br.com.commbox.chat.servidor;

import java.util.concurrent.BlockingQueue;

import br.com.commbox.chat.model.Mensagem;

public class NotificaEvento implements Runnable {

	private final BlockingQueue<Mensagem> mensagens;
	private final Mensagem mensagem;

	public NotificaEvento(BlockingQueue<Mensagem> mensagens, Mensagem mensagem) {
		this.mensagens = mensagens;
		this.mensagem = mensagem;
	}

	@Override
	public void run() {
		try {
			this.mensagens.put(mensagem);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
