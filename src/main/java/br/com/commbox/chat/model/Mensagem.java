package br.com.commbox.chat.model;

public class Mensagem {

	private int usuario;
	private String mensagem;

	public Mensagem(int usuario, String mensagem) {
		this.usuario = usuario;
		this.mensagem = mensagem;
	}
	
	public int getUsuario() {
		return usuario;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
