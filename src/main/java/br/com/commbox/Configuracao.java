package br.com.commbox;

public class Configuracao {

	public final static int SOCKET = 1;
	public final static int NIO_SOCKET = 2;
	public final static int MINA = 3;
	
	private static Configuracao configuracao;
	private final int servico;
	private final String servidor;
	private final int porta;
	
	private final static int SERVICO = Configuracao.MINA;
	private final static String SERVIDOR = "127.0.0.1";
	private final static int PORTA = 9999;
	
	
	public Configuracao() {
		this(Configuracao.SERVICO, Configuracao.SERVIDOR, Configuracao.PORTA);
	}
	
	private Configuracao(int servico, String servidor, int porta) {
		this.servico = servico;
		this.servidor = servidor;
		this.porta = porta;
	
	}
	
	public static Configuracao getInstance() {
		
		if (Configuracao.configuracao == null) {
			Configuracao.configuracao = new Configuracao();
		}
		return Configuracao.configuracao;
	}

	public int getServico() {
		return servico;
	}
	
	public String getServidor() {
		return servidor;
	}
	
	public int getPorta() {
		return porta;
	}
}
