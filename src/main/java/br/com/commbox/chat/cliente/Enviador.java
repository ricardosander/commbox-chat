package br.com.commbox.chat.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import br.com.commbox.chat.ui.Janela;

public class Enviador implements Runnable {

	private Socket socket;
	private Janela janela;

	public Enviador(Socket socket, Janela janela) {
		this.socket = socket;
		this.janela = janela;
	}

	@Override
	public void run() {
		
		System.out.println("\n\nIniciando thread da leitura.");
		
		PrintStream saida;
		try {
			
			saida = new PrintStream(socket.getOutputStream());
			
			System.out.println("\n\nComeçando leitura do teclado: ");
			String linha;
			while (true) {
				
				linha = janela.getMensagem();
				if (linha.equals("fim")) {
					break;
				}
				saida.println(linha);
			}
		} catch (IOException e) {
			System.out.println("Houve um erro IO: " + e.getMessage());
		}
		
		System.out.println("Finalizando leitura do teclado.");
	}

}
