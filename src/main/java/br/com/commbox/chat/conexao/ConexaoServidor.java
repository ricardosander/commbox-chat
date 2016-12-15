package br.com.commbox.chat.conexao;

public interface ConexaoServidor {

	public int getId();
	public ConexaoCliente recebeCliente();
	public void fechar();
	
}
