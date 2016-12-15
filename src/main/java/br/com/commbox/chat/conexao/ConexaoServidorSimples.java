package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.ServerSocket;

public class ConexaoServidorSimples implements ConexaoServidor {

	private final ServerSocket servidor;
	private final ConexaoClienteFactory clienteFactory;

	public ConexaoServidorSimples(int porta) {
		
		try {
			this.servidor = new ServerSocket(porta);
			this.clienteFactory = new ConexaoClienteFactory();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int getId() {
		return this.servidor.getLocalPort();
	}

	@Override
	public ConexaoCliente recebeCliente() {
		
		try {
			return this.clienteFactory.newConexaoCliente(this.servidor.accept());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void fechar() {
		
		try {
			this.servidor.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
