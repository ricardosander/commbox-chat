package br.com.commbox.chat.servidor;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.model.Mensagem;

public class Notificador implements Runnable {

	private final List<ConexaoCliente> clientes;
	private final BlockingQueue<Mensagem> mensagens;

	public Notificador(List<ConexaoCliente> clientes, BlockingQueue<Mensagem> mensagens) {
		this.clientes = clientes;
		this.mensagens = mensagens;
	}

	public void run() {

		try {

			System.out.println("\n\nIniciando notificador.");

			while (true) {

				System.out.println("\n\nEsperando mensagem para notificar.");

				Mensagem mensagem = this.mensagens.take();

				System.out.println("\n\nPeguei uma mensagem: " + mensagem);

				this.clientes.forEach(cliente -> {

					String mensagemEnviar = mensagem.getUsuario() + " disse: " + mensagem.getMensagem();
					if (cliente.getId() == mensagem.getUsuario()) {
						mensagemEnviar = "Você disse: " + mensagem.getMensagem();
					}

					System.out.println("\n\nDistribuindo para cada cliente.");

					System.out.println("\n\nImprimindo para o cliente.");
					cliente.escrever(mensagemEnviar);
					System.out.println("\n\nMensagem impressa para o cliente.");
				});
				System.out.println("\n\nMensagem impressa para todos os clientes.");
			}

		} catch (InterruptedException e) {
			System.out.println("Erro ao buscar mensagem: " + e.getMessage());
		}
		System.out.println("Finalizando notificador.");
	}
}
