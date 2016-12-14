package br.com.commbox.chat.servidor;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class RecebeCliente implements Runnable {

	private Socket cliente;
	private BlockingQueue<String> mensagens;

	public RecebeCliente(BlockingQueue<String> mensagens, Socket cliente) {
		this.mensagens = mensagens;
		this.cliente = cliente;
	}

	public void run() {

		System.out.println("Cliente " + this.cliente.getPort() + " sendo recebido em sua thread.");
		try {
			
			Scanner scanner = new Scanner(cliente.getInputStream());
			
			System.out.println("Iniciando leitura cliente " + this.cliente.getPort());
			while (scanner.hasNextLine()) {
				
				System.out.println("\n\nLendo mensagem do cliente " + this.cliente.getPort());
				String mensagem = this.cliente.getPort() + " disse: " + scanner.nextLine();
				
				System.out.println("Clietne " + cliente.getPort() + " enviou: ");
				System.out.println(mensagem);
				
				System.out.println("Colocando mensagem do cliente " + this.cliente.getPort() + " na fila de propagação: ");
				this.mensagens.put(mensagem);
				System.out.println("\n\nMensagem do cliente " + this.cliente.getPort() + " adicionada na fila.");
			}
			
			System.out.println("Terminando leitura thread do cliente " + this.cliente.getPort());
			scanner.close();
			cliente.close();
			
		} catch (IOException | InterruptedException e) {
			System.out.println("Erro na Thread: " + Thread.currentThread().getName());
			System.out.println("Cliente: " + this.cliente.getPort());
			System.out.println("Erro: " + e.getMessage());
		} 
	}

}