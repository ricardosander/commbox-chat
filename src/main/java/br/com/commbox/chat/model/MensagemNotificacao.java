package br.com.commbox.chat.model;

import br.com.commbox.chat.ui.Janela;

public class MensagemNotificacao extends Mensagem {

	public MensagemNotificacao(int origem, String mensagem, int tipo) {
		super(origem, mensagem, tipo);
	}

	public MensagemNotificacao(int origem, int destino, String mensagem, int tipo) {
		super(origem, destino, mensagem, tipo);
	}

	@Override
	public String getMensagem() {

		String mensagem = this.origem + " " + this.mensagem;
		if (this.destino == this.origem) {
			mensagem = "Você " + this.mensagem;
		}
		return mensagem;
	}

	@Override
	public void escrever(Janela janela) {
		janela.escreve(this.getMensagem());
	}

}
