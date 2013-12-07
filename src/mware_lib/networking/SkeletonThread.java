package mware_lib.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class SkeletonThread extends Thread {
	private final MethodCaller caller;
	private final Socket socket;
	
	
	public SkeletonThread(final MethodCaller caller, final Socket socket) {
		this.caller = caller;
		this.socket = socket;
		
		this.setDaemon(true);
		this.start();
	}
	
	
	public void run() {
		try {
			System.out.println("New worker thread for " + socket.getInetAddress());
			
			final Connection connection = new Connection(socket);
		
			while (!socket.isClosed() && !isInterrupted()) {
				final MethodCallMessage request = (MethodCallMessage)connection.receive();
				Serializable reply = null;
				try {
					reply = caller.callMethod(request.getMethodName(), request.getParametersObject());
				} catch (RuntimeException e) {
					e.printStackTrace();
					reply = e;
				}
				connection.send(reply);
			}
			
			connection.close();
		} catch (EOFException e) {
			// connection was closed by the client, so close the worker thread for this client
			System.out.println("Connection closed by client!");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
