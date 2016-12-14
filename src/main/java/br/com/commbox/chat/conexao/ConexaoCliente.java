package br.com.commbox.chat.conexao;

public interface ConexaoCliente {

	public int getId();
	public void fechar();
	public boolean temConteudo();
	public String ler();
	public void escrever(String texto); 
}
