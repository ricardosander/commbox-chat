package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;

public class ConexaoServidorNio implements ConexaoServidor {

	private final int porta;
	private final ServerSocketChannel serverSocketChannel;
	private final Selector selector;
	private final ByteBuffer buffer;

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

		try {

			System.out.println("Servidor iniciado");

			Iterator<SelectionKey> inter;
			SelectionKey chave;

			while (true) {

				System.out.println("Ainda aceitando novos clientes.");
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


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void parar() {
		try {
			this.serverSocketChannel.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void fechar() {
		// TODO Auto-generated method stub

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
					chave.attachment() + " vazou ");
			
			cliente.close();
			this.atualizaUsuariosOnline();

		} else {
			mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_USUARIO, (int) chave.attachment(),
					builder.toString());
		}

		System.out.println("Mensagem recebida: " + mensagem);
		this.espalhar(mensagem);
	}

	private void espalhar(Mensagem mensagem) throws IOException {

		ByteBuffer buffer;

		for (SelectionKey chave : selector.keys()) {

			if (chave.isValid() && chave.channel() instanceof SocketChannel) {

				SocketChannel cliente = (SocketChannel) chave.channel();
				
				System.out.println("Enviando para " + cliente.socket().getPort() + ": " + mensagem.toString() + "||");
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
			System.out.println("Aceitando cliente " + endereco);

			MensagemFactory mensagemFactory = new MensagemFactory();
			Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, endereco, "entrou");

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

		MensagemFactory factory = new MensagemFactory();
		Mensagem mensagem = factory.newMensagem(MensagemFactory.MESANGEM_USUARIOS_ONLINE, 0, builder.toString());
		this.espalhar(mensagem);
	}

}
