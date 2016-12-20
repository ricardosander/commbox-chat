package br.com.commbox.chat.servidor;

import br.com.commbox.chat.conexao.ConexaoServidor;
import br.com.commbox.chat.conexao.ConexaoServidorFactory;

public class Servidor {

	public static void main(String[] args) {

		ConexaoServidorFactory factory = new ConexaoServidorFactory();

		ConexaoServidor servidor = factory.newConexaoServidor();

		servidor.rodar();
		 System.exit(0);
	}
}
