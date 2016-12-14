package br.com.commbox.chat.cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import br.com.commbox.chat.ui.Janela;

public class Recebedor implements Runnable {

	private Socket socket;
	private Janela janela;

	public Recebedor(Socket socket, Janela janela) {
		this.socket = socket;
		this.janela = janela;
	}

	@Override
	public void run() {
		
//		System.out.println("\n\nRecebendo do servidor.");
		
		Scanner scanner;
		try {
			
			scanner = new Scanner(this.socket.getInputStream());
			
			String linha;
			while (scanner.hasNext()) {
				
				linha = scanner.nextLine();
				
//				System.out.println("Mensagem recebida do servidor: ");
				janela.escreve(linha);
			}
			
			System.out.println("\n\nTerminando de receber do servidor.");
			scanner.close();
			
		} catch (IOException e) {
			System.out.println("Erro: " + e.getMessage());
		}
	}

}
