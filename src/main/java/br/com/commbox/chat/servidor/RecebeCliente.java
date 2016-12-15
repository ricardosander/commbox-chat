package br.com.commbox.chat.servidor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.model.Mensagem;

public class RecebeCliente implements Runnable {

	private final ExecutorService threadPool;
	private final ConexaoCliente cliente;
	private final BlockingQueue<Mensagem> mensagens;

	public RecebeCliente(ExecutorService threadPool, BlockingQueue<Mensagem> mensagens, ConexaoCliente cliente) {
		this.threadPool = threadPool;
		this.mensagens = mensagens;
		this.cliente = cliente;
	}

	public void run() {

		System.out.println("Cliente " + this.cliente.getId() + " sendo recebido em sua thread.");
		try {
			
			System.out.println("Iniciando leitura cliente " + this.cliente.getId());
			while (this.cliente.temConteudo()) {
				
				System.out.println("\n\nLendo mensagem do cliente " + this.cliente.getId());
				String mensagem = this.cliente.ler();
				
				System.out.println("Clietne " + cliente.getId() + " enviou: ");
				System.out.println(this.cliente.getId() + " disse: " + mensagem);
				
				System.out.println("Colocando mensagem do cliente " + this.cliente.getId() + " na fila de propagação: ");
				
				this.mensagens.put(new Mensagem(cliente.getId(), mensagem));
				System.out.println("\n\nMensagem do cliente " + this.cliente.getId() + " adicionada na fila.");
			}
			
			this.threadPool.execute(new NotificaEvento(this.mensagens, this.cliente, "saiu na sala."));
			System.out.println("Terminando leitura thread do cliente " + this.cliente.getId());
			cliente.fechar();
			
		} catch (InterruptedException e) {
			System.out.println("Erro na Thread: " + Thread.currentThread().getName());
			System.out.println("Cliente: " + this.cliente.getId());
			System.out.println("Erro: " + e.getMessage());
		} 
	}

}
