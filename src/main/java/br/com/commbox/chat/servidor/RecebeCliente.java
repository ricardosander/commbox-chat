package br.com.commbox.chat.servidor;

import br.com.commbox.chat.conexao.ConexaoCliente;
import br.com.commbox.chat.conexao.ConexaoServidorSimples;
import br.com.commbox.chat.model.Mensagem;
import br.com.commbox.chat.model.MensagemFactory;

public class RecebeCliente implements Runnable {

	private final ConexaoServidorSimples servidor;
	private final ConexaoCliente cliente;

	public RecebeCliente(ConexaoServidorSimples conexaoServidorSimples, ConexaoCliente cliente) {
		this.servidor = conexaoServidorSimples;
		this.cliente = cliente;
	}

	public void run() {

		MensagemFactory mensagemFactory = new MensagemFactory();

		Mensagem entrada = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, this.cliente.getId(),
				"entrou na sala");
		this.servidor.getJanela().escreverMensagem(entrada.getMensagem());

		this.servidor.executa(new NotificaEvento(this.servidor.getMensagens(), entrada));

		while (this.cliente.temConteudo()) {

			Mensagem mensagem = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_USUARIO, this.cliente.getId(),
					this.cliente.ler());
			this.servidor.getJanela().escreverMensagem(mensagem.getMensagem());
			this.servidor.executa(new NotificaEvento(this.servidor.getMensagens(), mensagem));
		}

		Mensagem saida = mensagemFactory.newMensagem(MensagemFactory.MENSAGEM_NOTIFICACAO, this.cliente.getId(),
				"saiu na sala");

		this.servidor.getJanela().escreverMensagem(saida.getMensagem());
		this.servidor.executa(new NotificaEvento(this.servidor.getMensagens(), saida));

		this.servidor.remover(cliente);
		cliente.fechar();
	}

}
