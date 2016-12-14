package br.com.commbox.chat.ui;

import java.util.Scanner;

public class Terminal implements Janela {

	Scanner teclado;
	
	public Terminal() {
		this.teclado = new Scanner(System.in);
	}
	
	@Override
	public void abrir() {
		System.out.println("-----Iniciando Chat-----");
	}

	@Override
	public void fechar() {
		System.out.println("-----Saindo do Chat-----");
	}

	@Override
	public String getMensagem() {
		
		System.out.println("\n\nDigite:");
		return this.teclado.nextLine();
	}

	@Override
	public void escreve(String mensagem) {
		System.out.println("\n" + mensagem);
	}

}
