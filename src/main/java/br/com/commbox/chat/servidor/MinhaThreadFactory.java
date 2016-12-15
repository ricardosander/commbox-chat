package br.com.commbox.chat.servidor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MinhaThreadFactory implements ThreadFactory {

	private final ThreadFactory fabricaPadrao;

	public MinhaThreadFactory() {
		this.fabricaPadrao = Executors.defaultThreadFactory();
	}
	
	@Override
	public Thread newThread(Runnable r) {
		
		Thread newThread = this.fabricaPadrao.newThread(r);
		newThread.setUncaughtExceptionHandler(new ThreadServidorExecao());
		newThread.setDaemon(true);
		
		return newThread;
	}

}
