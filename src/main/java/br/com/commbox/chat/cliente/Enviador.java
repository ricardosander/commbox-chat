package br.com.commbox.chat.cliente;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.ui.Janela;

public class Enviador implements Runnable {

	private ConexaoCliente conexao;
	private Janela janela;

	public Enviador(ConexaoCliente cliente, Janela janela) {
		this.conexao = cliente;
		this.janela = janela;
	}

	@Override
	public void run() {

		String linha;
		do {

			linha = janela.getMensagem();
			if (linha.equals("\\q")) {
				break;
			}
			this.conexao.escrever(linha);
		} while (!linha.equals("\\q"));

		this.conexao.fechar();
	}

}
