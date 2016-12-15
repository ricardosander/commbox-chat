package br.com.commbox.chat.cliente;

import br.com.commbox.chat.conexao.ConexaoCliente;
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
		
//		System.out.println("\n\nRecebendo do servidor.");
		
		String linha;
		while (this.conexao.temConteudo()) {

			linha = this.conexao.ler();

			// System.out.println("Mensagem recebida do servidor: ");
			this.janela.escreve(linha);
		}

		System.out.println("\n\nTerminando de receber do servidor.");
		this.conexao.fechar();
	}

}
