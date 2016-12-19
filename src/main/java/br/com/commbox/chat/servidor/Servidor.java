package br.com.commbox.chat.servidor;

import br.com.commbox.chat.conexao.ConexaoServidor;
import br.com.commbox.chat.conexao.ConexaoServidorFactory;

public class Servidor {

	public static void main(String[] args) {

		int tipo = ConexaoServidorFactory.NIO_SOCKET;
		int porta = 12345;

		ConexaoServidorFactory factory = new ConexaoServidorFactory();
		
		ConexaoServidor servidor = factory.newConexaoServidor(tipo, porta);
		
		servidor.rodar();
		System.exit(0);
	}
}
