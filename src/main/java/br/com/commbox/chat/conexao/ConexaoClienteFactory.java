package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.Socket;

import br.com.commbox.Configuracao;

public class ConexaoClienteFactory {

	public ConexaoCliente newConexaoCliente() {

		Configuracao configuracao = Configuracao.getInstance();

		try {
			switch (configuracao.getServico()) {

			case Configuracao.SOCKET:
				return new ConexaoClienteSimples(new Socket(configuracao.getServidor(), configuracao.getPorta()));

			case Configuracao.NIO_SOCKET:
				return new ConexaoClienteNio(configuracao.getServidor(), configuracao.getPorta());

			case Configuracao.MINA:
				return new ConexaoClienteMina(configuracao.getServidor(), configuracao.getPorta());

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
