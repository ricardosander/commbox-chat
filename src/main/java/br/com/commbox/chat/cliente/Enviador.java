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

		System.out.println("\n\nIniciando thread da leitura.");

		System.out.println("\n\nComeçando leitura do teclado: ");
		String linha;
		do {

			linha = janela.getMensagem();
			if (linha.equals("fim")) {
				break;
			}
			this.conexao.escrever(linha);
		} while (!linha.equals("fim"));

		System.out.println("Finalizando leitura do teclado.");
	}

}
