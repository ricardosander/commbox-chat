package br.com.commbox.chat.servidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Notificador implements Runnable {

	private final List<Socket> clientes;
	private final BlockingQueue<String> mensagens;

	public Notificador(List<Socket> clientes, BlockingQueue<String> mensagens) {
		this.clientes = clientes;
		this.mensagens = mensagens;
	}

	public void run() {

		try {

			System.out.println("\n\nIniciando notificador.");

			while (true) {

				System.out.println("\n\nEsperando mensagem para notificar.");

				String mensagem = this.mensagens.take();

				System.out.println("\n\nPeguei uma mensagem: " + mensagem);

				this.clientes.forEach(cliente -> {

					try {

						System.out.println("\n\nDistribuindo para cada cliente.");

						PrintStream saida = new PrintStream(cliente.getOutputStream());

						System.out.println("\n\nImprimindo para o cliente.");
						saida.println(mensagem);
						System.out.println("\n\nMensagem impressa para o cliente.");
//						saida.close();

					} catch (IOException e) {
						System.out.println("\n\nHouve um erro ao buscar o stream do cliente.");
					}

				});
				System.out.println("\n\nMensagem impressa para todos os clientes.");
			}

		} catch (InterruptedException e) {
			System.out.println("Erro ao buscar mensagem: " + e.getMessage());
		}
		System.out.println("Finalizando notificador.");
	}
}