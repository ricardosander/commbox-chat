package br.com.commbox.chat.servidor;

import java.util.Collections;
import java.util.LinkedList;
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
		this.mensagens = new ArrayBlockingQueue<>(2);
		this.threadPool = Executors.newCachedThreadPool(new MinhaThreadFactory());
		this.clientes = Collections.synchronizedList(new LinkedList<>());
		
		System.out.println("\f----- Iniciando Servidor-----");
		System.out.println("Porta: " + servidor.getId());
	}

	public void rodar() {

		this.threadPool.execute(new Notificador(this));
		while (true) {

			ConexaoCliente cliente = this.servidor.recebeCliente();

			System.out.println("\n\nRecebendo cliente na porta: " + cliente.getId());
			this.adicionar(cliente);

			this.threadPool.execute(new RecebeCliente(this, cliente));
		}
	}
	
	public List<ConexaoCliente> getClientes() {
		return this.clientes;
	}
	
	public BlockingQueue<Mensagem> getMensagens() {
		return mensagens;
	}
	
	public void executa(Runnable runnable) {
		this.threadPool.execute(runnable);
	}
	
	public synchronized void adicionar(ConexaoCliente cliente) {
		
		this.clientes.add(cliente);
		this.threadPool.execute(new UsuariosOnline(this.threadPool, this.clientes));
	}
	
	public synchronized void remover(ConexaoCliente cliente) {
		
		this.clientes.remove(cliente);
		this.threadPool.execute(new UsuariosOnline(this.threadPool, this.clientes));
	}
	
	public void parar() {
		
		System.out.println("\n\n\nParando servidor.");
		//fechar e avisar os clientes.
		this.threadPool.shutdown();
		this.servidor.fechar();
	}

	public static void main(String[] args) {
		
		Servidor servidor = new Servidor(12345);
		servidor.rodar();
	}
}
