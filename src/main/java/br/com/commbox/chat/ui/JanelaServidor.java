package br.com.commbox.chat.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import br.com.commbox.chat.conexao.ConexaoServidor;

public class JanelaServidor implements Runnable {

	private JFrame janela;
	
	private JButton botaoFechar;
	
	private JTextArea textoConsole;
	private JTextArea textoMensagens;
	private JTextArea textoUsuariosOnline;

	private final ConexaoServidor servidor;
	
	public JanelaServidor(ConexaoServidor servidor) {
		
		this.servidor = servidor;
		
		this.janela = new JFrame();
		this.janela.setTitle("Servidor de Chat");
		this.janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.textoConsole = new JTextArea(10, 10);
		this.textoMensagens = new JTextArea(10, 10);
		this.textoUsuariosOnline = new JTextArea(10, 10);
		
		this.textoConsole.setEditable(false);
		this.textoMensagens.setEditable(false);
		this.textoUsuariosOnline.setEditable(false);
		
		this.botaoFechar = new JButton("Parar");
		this.botaoFechar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				servidor.parar();
				janela.setVisible(false);
			}
		});
		
		JScrollPane scrollPaneConsole = new JScrollPane(textoConsole);
		scrollPaneConsole.setPreferredSize(new Dimension(350, 350));
		
		JScrollPane scrollPaneMensagens = new JScrollPane(textoMensagens);
		scrollPaneMensagens.setPreferredSize(new Dimension(350, 350));
		
		JScrollPane scrollPaneUsuariosOnline = new JScrollPane(textoUsuariosOnline);
		scrollPaneUsuariosOnline.setPreferredSize(new Dimension(350, 350));
		
		JPanel painelConsole = new JPanel();
		JPanel painelMensagens = new JPanel();
		JPanel painelUsuariosOnline = new JPanel();
		JPanel painelBotoes = new JPanel();
		
		painelConsole.add(scrollPaneConsole);
		painelMensagens.add(scrollPaneMensagens);
		painelUsuariosOnline.add(scrollPaneUsuariosOnline);
		painelBotoes.add(botaoFechar);
		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		this.janela.add(container);
		
		container.add(painelConsole);
		container.add(painelMensagens);
		container.add(painelUsuariosOnline);
		container.add(painelBotoes);
	}
	
	public void abrir() {
		
		this.janela.pack();
		this.janela.setSize(1200, 540);
		this.janela.setVisible(true);
	}
	
	public void fechar() {
		this.janela.setVisible(false);
	}
	
	@Override
	public void run() {
		this.abrir();
	}
}
