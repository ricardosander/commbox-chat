package br.com.commbox.chat.conexao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import br.com.commbox.chat.servidor.ServidorHandler;

public class ConexaoServidorMina implements ConexaoServidor {

	private final IoAcceptor servidor;
	private final int porta;

	public ConexaoServidorMina(int porta) {

		this.servidor = new NioSocketAcceptor();
		this.porta = porta;

		this.configurarServidor();

		this.servidor.setHandler(new ServidorHandler());
	}

	@Override
	public int getPorta() {
		return this.porta;
	}

	@Override
	public void rodar() {

		try {
			servidor.bind(new InetSocketAddress(porta));
			
			synchronized (servidor) {
				servidor.wait();	
			}
			
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void parar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fechar() {
		// TODO Auto-generated method stub

	}

	private void configurarServidor() {

		this.servidor.getFilterChain().addLast("logger", new LoggingFilter());
		this.servidor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
	}

}
