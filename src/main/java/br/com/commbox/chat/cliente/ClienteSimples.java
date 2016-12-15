package br.com.commbox.chat.cliente;

import java.io.IOException;
import java.net.UnknownHostException;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.conexao.ConexaoClienteFactory;
import br.com.commbox.chat.ui.Janela;
import br.com.commbox.chat.ui.JanelaFactory;

public class ClienteSimples implements Cliente {

	private ConexaoCliente cliente;
	private Janela janela;
	
	public ClienteSimples(String servidor, int porta) throws UnknownHostException, IOException {
		
		this.cliente = new ConexaoClienteFactory().newConexaoCliente(servidor, porta);
		this.janela = new JanelaFactory().newJanela();
		System.out.println("\fConectado ao servidor na porta " + cliente.getClass());
	}
	
	public void rodar() {
		
		try {

			this.janela.abrir();
			Thread recebedor = new Thread(new Recebedor(cliente, janela));
			Thread enviador = new Thread(new Enviador(cliente, janela));

			recebedor.start();
			enviador.start();

			enviador.join();

			System.out.println("\n\n\nFinalizando cliente");

		} catch (InterruptedException e) {
			System.out.println("Houve um erro de thread: " + e.getMessage());
		}
	}
	
	public void parar() {
		
	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		ClienteSimples cliente = new ClienteSimples("localhost", 12345);
		cliente.rodar();
	}
}
