package br.com.commbox.chat.cliente;

import java.util.concurrent.BlockingQueue;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClienteHandler extends IoHandlerAdapter {

	private final BlockingQueue<String> fila;

	public ClienteHandler(BlockingQueue<String> fila) {
		this.fila = fila;
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		String mensagem = message.toString();
		this.fila.put(mensagem.toString());
	}
}
