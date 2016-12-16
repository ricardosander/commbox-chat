package br.com.commbox.chat.conexao;

public class ConexaoServidorFactory {

	public final static int SOCKET = 1;
	public final static int NIO_SOCKET = 2;

	public ConexaoServidor newConexaoServidor(int tipo, int porta) {

		switch (tipo) {

		case ConexaoClienteFactory.SOCKET:
			return new ConexaoServidorSimples(porta);

		case ConexaoClienteFactory.NIO_SOCKET:
			return new ConexaoServidorNio(porta);

		default:
			throw new IllegalArgumentException("Tipo inválido para conexão do servidor.");
		}

	}
}
