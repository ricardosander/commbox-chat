package br.com.commbox.chat.cliente;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;
import br.com.commbox.chat.ui.Janela;

public class Recebedor implements Runnable {

	private ConexaoCliente conexao;
	private Janela janela;

	public Recebedor(ConexaoCliente cliente, Janela janela) {
		this.conexao = cliente;
		this.janela = janela;
	}

	@Override
	public void run() {

		String linha;
		while (this.conexao.temConteudo()) {

			linha = this.conexao.ler();

			if (linha.trim().isEmpty()) {
				continue;
			}

			MensagemFactory mensagemFactory = new MensagemFactory();
			Mensagem mensagem = mensagemFactory.newMensagem(linha);
			mensagem.escrever(this.janela);
		}

		this.conexao.fechar();
	}
}
