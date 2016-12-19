package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;
import br.com.commbox.chat.ui.JanelaServidor;

public class ConexaoServidorNio implements ConexaoServidor {

	private final int porta;
	private final ServerSocketChannel serverSocketChannel;
	private final Selector selector;
	private final ByteBuffer buffer;
	private JanelaServidor janela;
	private final AtomicBoolean rodando = new AtomicBoolean(true);

	public ConexaoServidorNio(int porta) {

		try {

			this.porta = porta;

			serverSocketChannel = ServerSocketChannel.open();

			serverSocketChannel.socket().bind(new InetSocketAddress(this.porta));
			serverSocketChannel.configureBlocking(false);
			selector = Selector.open();

			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			buffer = ByteBuffer.allocate(256);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getPorta() {
		return this.porta;
	}

	@Override
	public void rodar() {

		this.janela = new JanelaServidor(this);
		Thread threadJanela = new Thread(this.janela, "Janela Servidor");
		threadJanela.setDaemon(true);
		threadJanela.start();

		try {

			this.janela.escreverConsole("Servidor iniciado");

			Iterator<SelectionKey> inter;
			SelectionKey chave;

			while (this.rodando.get()) {

				this.janela.escreverConsole("Ainda aceitando novos clientes.");
				selector.select();
				inter = selector.selectedKeys().iterator();
				while (inter.hasNext()) {

					chave = inter.next();
					inter.remove();

					if (!chave.isValid()) {
						continue;
					}

					if (chave.isAcceptable()) {
						this.recebe(chave);
					}

					if (chave.isReadable()) {
						this.le(chave);
					}

				}

			}
		} catch (ClosedSelectorException e) {

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void parar() {

		this.rodando.set(false);
		try {
			this.selector.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.fechar();
	}

	@Override
	public void fechar() {

		try {
			this.serverSocketChannel.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void le(SelectionKey chave) throws IOException {

		SocketChannel cliente = (SocketChannel) chave.channel();

		StringBuilder builder = new StringBuilder();

		buffer.clear();
		int lido = 0;
		while ((lido = cliente.read(buffer)) > 0) {

			buffer.flip();
			byte[] bytes = new byte[buffer.limit()];
			buffer.get(bytes);
			builder.append(new String(bytes));
			buffer.clear();
		}

		Mensagem mensagem;
		MensagemFactory mensagemFactory = new MensagemFactory();

		if (lido < 0) {
			mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, (int) chave.attachment(),
					chave.attachment() + " saiu da sala.");

			cliente.close();
			this.atualizaUsuariosOnline();

		} else {
			mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_USUARIO, (int) chave.attachment(),
					builder.toString());
		}
		
		this.janela.escreverMensagem(mensagem.getMensagem());
		this.espalhar(mensagem);
	}

	private void espalhar(Mensagem mensagem) throws IOException {

		ByteBuffer buffer;

		for (SelectionKey chave : selector.keys()) {

			if (chave.isValid() && chave.channel() instanceof SocketChannel) {

				SocketChannel cliente = (SocketChannel) chave.channel();

				this.janela.escreverConsole("Enviando para " + cliente.socket().getPort() + ": " + mensagem.toString() + "||");
				mensagem.setDestino(cliente.socket().getPort());

				buffer = ByteBuffer.wrap((mensagem.toString() + "||").getBytes());

				cliente.write(buffer);
				buffer.rewind();
			}
		}
	}

	private void recebe(SelectionKey chave) throws IOException {

		SocketChannel socketChannel = ((ServerSocketChannel) chave.channel()).accept();
		socketChannel.configureBlocking(false);

		if (socketChannel != null) {

			socketChannel.configureBlocking(false);

			int endereco = socketChannel.socket().getPort();
			this.janela.escreverConsole("Aceitando cliente " + endereco);

			MensagemFactory mensagemFactory = new MensagemFactory();
			Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, endereco,
					"entrou na sala.");

			this.janela.escreverMensagem(mensagem.getMensagem());
			socketChannel.register(selector, SelectionKey.OP_READ, endereco);

			this.atualizaUsuariosOnline();
			this.espalhar(mensagem);
		}
	}

	private void atualizaUsuariosOnline() throws IOException {

		StringBuilder builder = new StringBuilder();

		for (SelectionKey chave : selector.keys()) {

			if (chave.isValid() && chave.channel() instanceof SocketChannel) {

				if (builder.length() != 0) {
					builder.append(";");
				}

				SocketChannel cliente = (SocketChannel) chave.channel();
				builder.append(cliente.socket().getPort());
			}
		}
		
		this.janela.escreverUsuariosOnline(builder.toString());

		MensagemFactory factory = new MensagemFactory();
		Mensagem mensagem = factory.newMensagem(MensagemFactory.MESANGEM_USUARIOS_ONLINE, 0, builder.toString());
		this.espalhar(mensagem);
	}

}
