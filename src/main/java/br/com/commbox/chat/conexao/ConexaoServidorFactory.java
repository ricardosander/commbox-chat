package br.com.commbox.chat.conexao;

public class ConexaoServidorFactory {

	public ConexaoServidor newConexaoServidor(int porta) {
		return new ConexaoServidorSimples(porta);
	}
}
