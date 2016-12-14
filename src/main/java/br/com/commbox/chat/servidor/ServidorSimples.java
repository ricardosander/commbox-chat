package br.com.commbox.chat.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServidorSimples implements Servidor {

	private ServerSocket servidor;
	private final BlockingQueue<String> mensagens;
	private final List<Socket> clientes;
	

	public ServidorSimples(int porta) throws IOException {

		servidor = new ServerSocket(porta);
		this.clientes = new ArrayList<Socket>();
		this.mensagens = new ArrayBlockingQueue<String>(2);
		
		System.out.println("\f----- Iniciando Servidor-----");
		System.out.println("Porta: " + servidor.getLocalPort());
	}

	public void rodar() {

		try {
			
			new Thread(new Notificador(this.clientes, this.mensagens)).start();
			
			while (true) {
				
				Socket cliente = servidor.accept();
				
				System.out.println("\n\nRecebendo cliente na porta: " + cliente.getPort());
				this.clientes.add(cliente);
				
				new Thread(new RecebeCliente(this.mensagens, cliente)).start();
			}
			
			
			//servidor.close();
			
		} catch (IOException e) {
			System.out.println("Houve um erro ao iniciar o servidor: " + e.getMessage());
		}
	}
	
	public void parar() throws IOException {
		
		System.out.println("\n\n\nParando servidor.");
		//altera variaveis atomic
		//shutdown no threadpool
		this.servidor.close();
	}

	public static void main(String[] args) throws IOException {
		
		ServidorSimples servidorSimples = new ServidorSimples(12345);
		servidorSimples.rodar();
	}
}
