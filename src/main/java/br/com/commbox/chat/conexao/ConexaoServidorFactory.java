package br.com.commbox.chat.conexao;

import br.com.commbox.Configuracao;

public class ConexaoServidorFactory {

	public ConexaoServidor newConexaoServidor() {

		Configuracao configuracao = Configuracao.getInstance();

		switch (configuracao.getServico()) {

		case Configuracao.SOCKET:
			return new ConexaoServidorSimples(configuracao.getPorta());

		case Configuracao.NIO_SOCKET:
			return new ConexaoServidorNio(configuracao.getPorta());

		case Configuracao.MINA:
			return new ConexaoServidorMina(configuracao.getPorta());

		default:
			throw new IllegalArgumentException("Tipo inválido para conexão do servidor.");
		}

	}
}
