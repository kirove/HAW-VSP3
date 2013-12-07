package mware_lib.networking;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Stub implements MethodCaller {
	private final Connection connection;

	public Stub(final String host, final int port) throws IOException {
		final Socket clientSocket = new Socket(host, port);
		this.connection = new Connection(clientSocket);
	}

/*	public final Serializable callMethod(final Serializable parametersObject) {
		final String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		return callMethod(methodName, parametersObject);
	}
	*/
	
	public synchronized final Serializable callMethod(final String methodName, final Serializable parametersObject) {
		final MethodCallMessage message = new MethodCallMessage(methodName, parametersObject);
		
		try {
			connection.send(message);

			final Serializable reply = connection.receive();

			return reply;

		}
        // catch technical Exceptions (should not catch OverDraftException)
        catch(Exception e) {
            throw new RuntimeException(e.toString());
		}
	}
}
