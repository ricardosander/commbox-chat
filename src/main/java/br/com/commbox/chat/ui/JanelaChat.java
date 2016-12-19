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

public class JanelaChat implements Janela {

	private static final String QUEBRA_DE_LINHA = "\n";
	private static final String TITULO_JANELA = "Chat Commbox";

	private JFrame janela;

	private JPanel container;
	private JPanel painelSuperior;
	private JPanel painelChat;
	private JPanel painelUsuarios;
	private JPanel painelMensagem;
	private JPanel painelBotoes;

	private JButton botaoEnviar;
	private JButton botaoLimpar;
	private JButton botaoSair;

	private JTextArea textoChat;
	private JTextArea textoMensagem;
	private JTextArea textoUsuarios;

	public JanelaChat() {

		this.prepararJanela();
		this.prepararPaineis();
		this.prepararCaixasTexto();
		this.prepararBotoes();
	}

	public void abrir() {

		this.janela.pack();
		this.janela.setSize(540, 540);
		this.janela.setVisible(true);
	}

	private void prepararCaixasTexto() {

		textoChat = new JTextArea(10, 10);
		textoMensagem = new JTextArea(2, 10);
		textoUsuarios = new JTextArea(10, 4);

		textoUsuarios.setText("Usuários On-line");

		textoChat.setEditable(false);
		textoUsuarios.setEditable(false);

		textoMensagem.setLineWrap(true);

		JScrollPane scrollPaneChat = new JScrollPane(this.textoChat);
		scrollPaneChat.setPreferredSize(new Dimension(350, 200));

		JScrollPane scrollPaneMessage = new JScrollPane(this.textoMensagem);
		scrollPaneMessage.setPreferredSize(new Dimension(500, 50));

		JScrollPane scrollPaneUsuarios = new JScrollPane(textoUsuarios);
		scrollPaneUsuarios.setPreferredSize(new Dimension(150, 200));

		this.painelChat.add(scrollPaneChat);
		this.painelUsuarios.add(scrollPaneUsuarios);
		this.painelMensagem.add(scrollPaneMessage);
	}

	private synchronized void enviaMensagem() {

		String conteudo = textoMensagem.getText();
		if (conteudo == null || conteudo.trim().isEmpty()) {
			return;
		}
		this.notify();
	}

	private void prepararBotoes() {

		botaoEnviar = new JButton("Enviar");
		botaoEnviar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				enviaMensagem();
			}
		});

		botaoLimpar = new JButton("Limpar");
		botaoLimpar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				textoMensagem.setText(null);
			}
		});

		botaoSair = new JButton("Sair");
		botaoSair.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.painelBotoes.add(this.botaoSair);
		this.painelBotoes.add(this.botaoLimpar);
		this.painelBotoes.add(this.botaoEnviar);
	}

	private void prepararJanela() {

		this.janela = new JFrame();
		this.janela.setTitle(JanelaChat.TITULO_JANELA);
		this.janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void prepararPaineis() {

		this.container = new JPanel();
		this.container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		this.janela.add(container);

		this.painelSuperior = new JPanel();
		this.painelSuperior.setLayout(new BoxLayout(painelSuperior, BoxLayout.X_AXIS));

		this.painelChat = new JPanel();
		this.painelUsuarios = new JPanel();
		this.painelMensagem = new JPanel();
		this.painelBotoes = new JPanel();

		this.painelSuperior.add(this.painelChat);
		this.painelSuperior.add(this.painelUsuarios);

		this.container.add(this.painelSuperior);
		this.container.add(this.painelMensagem);
		this.container.add(this.painelBotoes);
	}

	public static void main(String[] args) {

		JanelaChat janela = new JanelaChat();
		janela.abrir();
	}

	@Override
	public void fechar() {

		synchronized (this) {
			this.notifyAll();
		}
		this.janela.setVisible(false);
	}

	@Override
	public String getMensagem() {

		synchronized (this) {
			try {
				this.wait();

				String mensagem = this.textoMensagem.getText().trim();

				textoMensagem.setText(null);

				return mensagem;

			} catch (InterruptedException e) {
				System.out.println("Houve um erro no envio de mensagem: " + e.getMessage());
			}
		}
		throw new RuntimeException("Houve um erro no chat.");
	}

	@Override
	public void escreve(String mensagem) {
		this.textoChat.append(QUEBRA_DE_LINHA + mensagem);
	}

	@Override
	public void atualizaUsuarios(String usuarios) {

		usuarios = usuarios.replace(";", QUEBRA_DE_LINHA);
		this.textoUsuarios.setText("Usuários On-line" + QUEBRA_DE_LINHA + usuarios);
	}
}
