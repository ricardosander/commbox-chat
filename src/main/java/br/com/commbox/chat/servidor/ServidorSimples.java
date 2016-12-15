package br.com.commbox.chat.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.conexao.ConexaoClienteFactory;
import br.com.commbox.chat.model.Mensagem;

public class ServidorSimples implements Servidor {

	private ServerSocket servidor;
	private final List<ConexaoCliente> clientes;
	private final BlockingQueue<Mensagem> mensagens;
	private final ExecutorService threadPool; 

	public ServidorSimples(int porta) throws IOException {

		servidor = new ServerSocket(porta);
		this.clientes = new ArrayList<>();
		this.mensagens = new ArrayBlockingQueue<>(2);
		this.threadPool = Executors.newCachedThreadPool(new MinhaThreadFactory());
		
		System.out.println("\f----- Iniciando Servidor-----");
		System.out.println("Porta: " + servidor.getLocalPort());
	}

	public void rodar() {

		ConexaoClienteFactory conexaoClienteFactory = new ConexaoClienteFactory();
		try {
			
			this.threadPool.execute(new Notificador(this.clientes, this.mensagens));
			while (true) {
				
				ConexaoCliente cliente = conexaoClienteFactory.newConexaoCliente(servidor.accept());
				
				System.out.println("\n\nRecebendo cliente na porta: " + cliente.getId());
				this.clientes.add(cliente);
				
				this.threadPool.execute(new RecebeCliente(this.mensagens, cliente));
			}
			
		} catch (IOException e) {
			System.out.println("Houve um erro ao iniciar o servidor: " + e.getMessage());
		}
	}
	
	public void parar() throws IOException {
		
		System.out.println("\n\n\nParando servidor.");
		//fechar e avisar os clientes.
		//altera variaveis atomic
		//shutdown no threadpool
		this.servidor.close();
	}

	public static void main(String[] args) throws IOException {
		
		ServidorSimples servidorSimples = new ServidorSimples(12345);
		servidorSimples.rodar();
	}
}
