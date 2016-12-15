package br.com.commbox.chat.servidor;

import java.lang.Thread.UncaughtExceptionHandler;

public class ThreadServidorExecao implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("Execeção na theard " + t.getName() + ": " + e.getMessage());
	}

}
