package br.com.commbox.chat.model;

import br.com.commbox.chat.ui.Janela;

public class MensagemUsuariosOnline extends Mensagem {

	public MensagemUsuariosOnline(int origem, String mensagem, int tipo) {
		super(origem, mensagem, tipo);
	}
	
	public MensagemUsuariosOnline(int origem, int destino, String mensagem, int tipo) {
		super(origem, destino, mensagem, tipo);
	}

	@Override
	public String getMensagem() {
		return this.mensagem.replace(";", Mensagem.QUEBRA_DE_LINHA);
	}

	@Override
	public void escrever(Janela janela) {
		janela.atualizaUsuarios(this.getMensagem());
	}

}
