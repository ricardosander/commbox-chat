package br.com.commbox.chat.servidor;

import br.com.commbox.chat.conexao.ConexaoServidorSimples;
import br.com.commbox.chat.model.Mensagem;

public class Notificador implements Runnable {

	private final ConexaoServidorSimples servidor;

	public Notificador(ConexaoServidorSimples conexaoServidorSimples) {
		this.servidor = conexaoServidorSimples;
	}

	public void run() {

		try {

			while (true) {

				Mensagem mensagem = this.servidor.getMensagens().take();
				this.servidor.getClientes().forEach(cliente -> {
					this.servidor.executa(new NotificaCliente(cliente, mensagem));
				});
			}

		} catch (InterruptedException e) {
			this.servidor.getJanela().escreverConsole("Erro ao buscar mensagem: " + e.getMessage());
		}
	}
}
