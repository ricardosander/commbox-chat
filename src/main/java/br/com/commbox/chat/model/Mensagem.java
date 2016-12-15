package br.com.commbox.chat.model;

public class Mensagem {
	
	private final int usuario;
	private final String mensagem;
	private final boolean automatica;

	public Mensagem(int usuario, String mensagem) {
		this(usuario, mensagem, false);
	}
	
	public Mensagem(int usuario, String mensagem, boolean automatica) {
		this.usuario = usuario;
		this.mensagem = mensagem;
		this.automatica = automatica;
	}
	
	public int getUsuario() {
		return usuario;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public boolean isAutomatica() {
		return automatica;
	}
}
