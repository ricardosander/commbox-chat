package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConexaoClienteSimples implements ConexaoCliente {

	private Socket socket;
	private PrintStream saida;
	private Scanner entrada;

	public ConexaoClienteSimples(Socket socket) {
		
		this.socket = socket;
		try {
			
			this.entrada = new Scanner(this.socket.getInputStream());
			this.saida = new PrintStream(this.socket.getOutputStream());
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int getId() {
		return this.socket.getPort();
	}

	@Override
	public void fechar() {
		try {
			this.socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean temConteudo() {
		return this.entrada.hasNextLine();
	}

	@Override
	public String ler() {
		return this.entrada.nextLine();
	}

	@Override
	public void escrever(String texto) {
		this.saida.println(texto);
	}

}
