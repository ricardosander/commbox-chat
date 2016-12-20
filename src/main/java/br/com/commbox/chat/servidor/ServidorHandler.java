package br.com.commbox.chat.servidor;

import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;

public class ServidorHandler extends IoHandlerAdapter {

	private final Set<IoSession> clientes = new HashSet<>();

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		String texto = message.toString();

		MensagemFactory mensagemFactory = new MensagemFactory();
		Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_USUARIO, (int) session.getId(), texto);

		this.broadcast(mensagem);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {

		MensagemFactory mensagemFactory = new MensagemFactory();
		Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, (int) session.getId(),
				"entrou da sala.");

		this.clientes.add(session);
		this.broadcast(mensagem);
		this.usuariosOnline();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

		MensagemFactory mensagemFactory = new MensagemFactory();
		Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, (int) session.getId(),
				"saiu da sala.");

		this.broadcast(mensagem);
		this.clientes.remove(session);
		this.usuariosOnline();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		throw new RuntimeException(cause);
	}

	private void broadcast(Mensagem mensagem) {

		this.clientes.forEach(cliente -> {

			mensagem.setDestino((int) cliente.getId());
			cliente.write(mensagem.toString());
		});
	}

	private void usuariosOnline() {

		StringBuilder builder = new StringBuilder();
		for (IoSession cliente : this.clientes) {

			if (builder.length() != 0) {
				builder.append(";");
			}
			builder.append(cliente.getId());
		}

		MensagemFactory mensagemFactory = new MensagemFactory();
		Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MESANGEM_USUARIOS_ONLINE, 0,
				builder.toString());
		this.broadcast(mensagem);
	}
}
