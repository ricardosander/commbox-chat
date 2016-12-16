package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.Socket;

public class ConexaoClienteFactory {

	public final static int SOCKET = 1;
	public final static int NIO_SOCKET = 2;

	public ConexaoCliente newConexaoCliente(int tipo, String servidor, int porta) {

		try {
			switch (tipo) {

			case ConexaoClienteFactory.SOCKET:
				return new ConexaoClienteSimples(new Socket(servidor, porta));

			case ConexaoClienteFactory.NIO_SOCKET:
				return new ConexaoClienteNio(servidor, porta);

			default:
				throw new IllegalArgumentException("Tipo de conexão para o cliente inválido.");
			
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ConexaoCliente newConexaoCliente(Socket socket) {
		return new ConexaoClienteSimples(socket);
	}
}
