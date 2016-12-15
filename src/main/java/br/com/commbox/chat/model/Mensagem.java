package br.com.commbox.chat.model;

import br.com.commbox.chat.ui.Janela;

public abstract class Mensagem {
	
	protected static final String QUEBRA_DE_LINHA = "\n";
	
	protected int destino;
	protected final int origem;
	protected final String mensagem;
	protected final int tipo;

	public Mensagem(int origem, String mensagem, int tipo) {
		this.origem = origem;
		this.mensagem = mensagem;
		this.tipo = tipo;
	}
	
	public Mensagem(int origem, int destino, String mensagem, int tipo) {
		this.origem = origem;
		this.destino = destino;
		this.mensagem = mensagem;
		this.tipo = tipo;
	}
	
	public void setDestino(int destino) {
		this.destino = destino;
	}
	
	public abstract String getMensagem();
	
	public abstract void escrever(Janela janela);
	
	@Override
	public String toString() {
		return this.tipo + "|" + this.origem + "|" + this.destino + "|" + this.mensagem;
	}
}
