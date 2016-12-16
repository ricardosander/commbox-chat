package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConexaoClienteNio implements ConexaoCliente {

	private final SocketChannel socketChannel;
	private final AtomicBoolean temConteudo = new AtomicBoolean(true);;

	public ConexaoClienteNio(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public ConexaoClienteNio(String servidor, int porta) {

		try {

			this.socketChannel = SocketChannel.open();
			boolean conectado = socketChannel.connect(new InetSocketAddress(servidor, porta));

			if (!conectado) {
				throw new RuntimeException("Não foi possível conectar ao servidor.");
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getId() {
		return this.socketChannel.socket().getPort();
	}

	@Override
	public void fechar() {
		this.fechar();
	}

	@Override
	public boolean temConteudo() {
		return this.temConteudo.get();
	}

	@Override
	public String ler() {

		String retorno = null;
		try {

			ByteBuffer buffer = ByteBuffer.allocate(1024);
			StringBuilder builder = new StringBuilder();

			int bytesLidos = this.socketChannel.read(buffer);

			if (bytesLidos == -1) {
				
				this.temConteudo.set(false);
				return null;
			}

			buffer.flip();
			byte[] bytes = new byte[buffer.limit()];
			buffer.get(bytes);
			builder.append(new String(bytes));
			buffer.clear();

			retorno = builder.toString();

			return retorno;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void escrever(String texto) {

		try {

			ByteBuffer buffer = ByteBuffer.wrap(texto.getBytes());
			this.socketChannel.write(buffer);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
