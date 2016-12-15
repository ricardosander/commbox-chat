package br.com.commbox.chat.servidor;

import br.com.commbox.chat.model.Mensagem;

public class Notificador implements Runnable {

	private final Servidor servidor;

	public Notificador(Servidor servidor) {
		this.servidor = servidor;
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
			System.out.println("Erro ao buscar mensagem: " + e.getMessage());
		}
	}
}
