package br.com.commbox.chat.model;

public class MensagemFactory {

	public final static int MENSAGEM_USUARIO = 1;
	public final static int MENSAGEM_NOTIFICACAO = 2;
	public final static int MESANGEM_USUARIOS_ONLINE = 3;

	public Mensagem newMensagem(String linha) {

		String[] parametros = linha.split("\\|");
		if (parametros.length != 4) {
			throw new IllegalArgumentException("Texto informado não é uma mensagem válida.");
		}

		int tipo = Integer.parseInt(parametros[0]);
		int origem = Integer.parseInt(parametros[1]);
		int destino = Integer.parseInt(parametros[2]);
		String mensagem = parametros[3];

		return this.newMensagem(tipo, origem, destino, mensagem);
	}

	public Mensagem newMensagem(int tipo, int origem, String mensagem) {

		switch (tipo) {
		case MENSAGEM_USUARIO:
			return new MensagemUsuario(origem, mensagem, tipo);

		case MENSAGEM_NOTIFICACAO:
			return new MensagemNotificacao(origem, mensagem, tipo);

		case MESANGEM_USUARIOS_ONLINE:
			return new MensagemUsuariosOnline(origem, mensagem, tipo);

		default:
			throw new IllegalArgumentException("Tipo de mensagem inválido.");
		}
	}

	public Mensagem newMensagem(int tipo, int origem, int destino, String mensagem) {

		switch (tipo) {
		case MENSAGEM_USUARIO:
			return new MensagemUsuario(origem, destino, mensagem, tipo);

		case MENSAGEM_NOTIFICACAO:
			return new MensagemNotificacao(origem, destino, mensagem, tipo);

		case MESANGEM_USUARIOS_ONLINE:
			return new MensagemUsuariosOnline(origem, destino, mensagem, tipo);

		default:
			throw new IllegalArgumentException("Tipo de mensagem inválido.");
		}
	}
}
