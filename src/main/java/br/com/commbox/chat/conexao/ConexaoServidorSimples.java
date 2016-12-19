package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.servidor.MinhaThreadFactory;
import br.com.commbox.chat.servidor.Notificador;
import br.com.commbox.chat.servidor.RecebeCliente;
import br.com.commbox.chat.servidor.UsuariosOnline;
import br.com.commbox.chat.ui.JanelaServidor;

public class ConexaoServidorSimples implements ConexaoServidor {

	private final ServerSocket servidor;
	private final ConexaoClienteFactory clienteFactory;

	private final List<ConexaoCliente> clientes;
	private final BlockingQueue<Mensagem> mensagens;
	private final ExecutorService threadPool;

	public ConexaoServidorSimples(int porta) {

		try {

			this.servidor = new ServerSocket(porta);
			this.clienteFactory = new ConexaoClienteFactory();

			this.mensagens = new ArrayBlockingQueue<>(2);
			this.threadPool = Executors.newCachedThreadPool(new MinhaThreadFactory());
			this.clientes = Collections.synchronizedList(new LinkedList<>());

			System.out.println("\f----- Iniciando Servidor-----");
			System.out.println("Porta: " + this.getId());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getPorta() {
		return this.servidor.getLocalPort();
	}

	@Override
	public void rodar() {

		this.threadPool.execute(new JanelaServidor(this));
		this.threadPool.execute(new Notificador(this));
		while (true) {

			ConexaoCliente cliente = this.recebeCliente();

			if (cliente == null) {
				break;
			}
			System.out.println("\n\nRecebendo cliente na porta: " + cliente.getId());
			this.adicionar(cliente);

			this.threadPool.execute(new RecebeCliente(this, cliente));
		}

	}

	@Override
	public void parar() {
		System.out.println("\n\n\nParando servidor.");
		// fechar e avisar os clientes.
		this.threadPool.shutdown();
		this.fechar();
	}

	@Override
	public void fechar() {

		try {
			this.servidor.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ConexaoCliente recebeCliente() {

		try {
			return this.clienteFactory.newConexaoCliente(this.servidor.accept());
		} catch (SocketException e) {
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public void executa(Runnable runnable) {
		this.threadPool.execute(runnable);
	}

	public int getId() {
		return this.servidor.getLocalPort();
	}

	public List<ConexaoCliente> getClientes() {
		return this.clientes;
	}

	public BlockingQueue<Mensagem> getMensagens() {
		return mensagens;
	}

	public synchronized void adicionar(ConexaoCliente cliente) {

		this.clientes.add(cliente);
		this.threadPool.execute(new UsuariosOnline(this.threadPool, this.clientes));
	}

	public synchronized void remover(ConexaoCliente cliente) {

		this.clientes.remove(cliente);
		this.threadPool.execute(new UsuariosOnline(this.threadPool, this.clientes));
	}

}
