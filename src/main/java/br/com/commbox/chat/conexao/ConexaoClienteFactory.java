package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.Socket;

public class ConexaoClienteFactory {

	public ConexaoCliente newConexaoCliente(String servidor, int porta) {

		try {
			return new ConexaoClienteSimples(new Socket(servidor, porta));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ConexaoCliente newConexaoCliente(Socket socket) {
		return new ConexaoClienteSimples(socket);
	}
}
