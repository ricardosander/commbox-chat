package br.com.commbox.chat.model;

import br.com.commbox.chat.ui.Janela;

public class MensagemUsuario extends Mensagem {

	public MensagemUsuario(int origem, String mensagem, int tipo) {
		super(origem, mensagem, tipo);
	}

	public MensagemUsuario(int origem, int destino, String mensagem, int tipo) {
		super(origem, destino, mensagem, tipo);
	}

	@Override
	public String getMensagem() {

		String mensagem = this.origem + " disse: " + this.mensagem;
		if (this.destino == this.origem) {
			mensagem = "Você disse: " + this.mensagem;
		}
		return mensagem;
	}

	@Override
	public void escrever(Janela janela) {
		janela.escreve(this.getMensagem());
	}

}
