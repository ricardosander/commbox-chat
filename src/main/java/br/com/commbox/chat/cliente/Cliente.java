package br.com.commbox.chat.cliente;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.conexao.ConexaoClienteFactory;
import br.com.commbox.chat.ui.Janela;
import br.com.commbox.chat.ui.JanelaFactory;

public class Cliente {

	private ConexaoCliente cliente;
	private Janela janela;

	public Cliente() {

		ConexaoClienteFactory factory = new ConexaoClienteFactory();

		this.cliente = factory.newConexaoCliente();
		this.janela = new JanelaFactory().newJanela();
		System.out.println("\fConectado ao servidor na porta " + cliente.getClass());
	}

	public void rodar() {

		try {

			this.janela.abrir();
			Thread recebedor = new Thread(new Recebedor(cliente, janela), "Recebedor");
			Thread enviador = new Thread(new Enviador(cliente, janela), "Enviador");

			recebedor.start();
			enviador.start();

			enviador.join();

			System.out.println("\n\n\nFinalizando cliente");

			this.parar();
		} catch (InterruptedException e) {
			System.out.println("Houve um erro de thread: " + e.getMessage());
		}
	}

	public void parar() {
		this.janela.fechar();
		this.cliente.fechar();
	}

	public static void main(String[] args) {

		Cliente cliente = new Cliente();
		cliente.rodar();
	}
}
