package br.com.commbox.chat.conexao;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import br.com.commbox.chat.cliente.ClienteHandler;

public class ConexaoClienteMina implements ConexaoCliente {

	private static final int TIMEOUT = 1000;

	private final NioSocketConnector cliente;
	private final IoSession session;
	private final AtomicBoolean rodando = new AtomicBoolean(true);

	private final BlockingQueue<String> fila = new ArrayBlockingQueue<>(2);

	public ConexaoClienteMina(String servidor, int porta) {

		cliente = new NioSocketConnector();

		cliente.setConnectTimeoutMillis(ConexaoClienteMina.TIMEOUT);

		configurarCliente();

		cliente.setHandler(new ClienteHandler(this.fila));

		ConnectFuture future = cliente.connect(new InetSocketAddress(servidor, porta));
		future.awaitUninterruptibly();
		session = future.getSession();
	}

	@Override
	public int getId() {
		return (int) this.session.getId();
	}

	@Override
	public void fechar() {

		this.rodando.set(false);
		if (this.session != null && this.session.isConnected()) {
			this.session.close(false);
			this.session.getCloseFuture().awaitUninterruptibly();
		}
		this.cliente.dispose();
	}

	@Override
	public boolean temConteudo() {
		return this.rodando.get();
	}

	@Override
	public String ler() {

		try {
			return this.fila.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void escrever(String texto) {
		this.session.write(texto);
	}

	private void configurarCliente() {

		cliente.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		cliente.getFilterChain().addLast("logger", new LoggingFilter());
	}
}
