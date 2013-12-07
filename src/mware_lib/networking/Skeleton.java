package mware_lib.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Skeleton extends Thread {
	private final MethodCaller caller;
	private final ServerSocket serverSocket;

	public Skeleton(final int listenPort, final MethodCaller caller, final boolean isDaemon)
			throws IOException {
		this.caller = caller;
		this.serverSocket = new ServerSocket(listenPort);
		
		this.setDaemon(isDaemon);
		this.start();
	}

	
	public void run() {
		try {
			while (!isInterrupted()) {
				final Socket socket = serverSocket.accept();

				new SkeletonThread(caller, socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public final String getHost() {
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostname;
	}

	
	public final int getPort() {
		return serverSocket.getLocalPort();
	}

	
	public void close() throws IOException {
		serverSocket.close();
	}
}
