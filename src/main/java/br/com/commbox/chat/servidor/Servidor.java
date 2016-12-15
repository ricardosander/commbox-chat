package br.com.commbox.chat.servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.conexao.ConexaoServidor;
import br.com.commbox.chat.conexao.ConexaoServidorFactory;
import br.com.commbox.chat.model.Mensagem;

public class Servidor {

	private ConexaoServidor servidor;
	private final List<ConexaoCliente> clientes;
	private final BlockingQueue<Mensagem> mensagens;
	private final ExecutorService threadPool; 

	public Servidor(int porta) {

		servidor = new ConexaoServidorFactory().newConexaoServidor(porta);
		this.clientes = new ArrayList<>();
		this.mensagens = new ArrayBlockingQueue<>(2);
		this.threadPool = Executors.newCachedThreadPool(new MinhaThreadFactory());
		
		System.out.println("\f----- Iniciando Servidor-----");
		System.out.println("Porta: " + servidor.getId());
	}

	public void rodar() {

		this.threadPool.execute(new Notificador(this.clientes, this.mensagens));
		while (true) {

			ConexaoCliente cliente = this.servidor.recebeCliente();

			System.out.println("\n\nRecebendo cliente na porta: " + cliente.getId());
			this.clientes.add(cliente);

			this.threadPool.execute(new RecebeCliente(this.mensagens, cliente));
		}
	}
	
	public void parar() {
		
		System.out.println("\n\n\nParando servidor.");
		//fechar e avisar os clientes.
		//altera variaveis atomic
		//shutdown no threadpool
		this.servidor.fechar();
	}

	public static void main(String[] args) {
		
		Servidor servidor = new Servidor(12345);
		servidor.rodar();
	}
}
