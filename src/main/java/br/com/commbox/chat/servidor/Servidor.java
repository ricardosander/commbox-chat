package br.com.commbox.chat.servidor;

import java.io.IOException;

public interface Servidor {

	public void rodar();
	public void parar() throws IOException;
}
