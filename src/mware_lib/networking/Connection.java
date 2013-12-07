package mware_lib.networking;

import java.io.*;
import java.net.Socket;

public class Connection {
	private final InputStream inputStream;
	private final ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	
	public Connection(Socket socket) throws IOException {
		inputStream = socket.getInputStream();
		objectOutputStream = new ObjectOutputStream( socket.getOutputStream() );
	}
	
	
	public final Serializable receive() throws IOException, ClassNotFoundException {
		if (objectInputStream == null) {
			// this is blocking if no data is available
			objectInputStream = new ObjectInputStream(inputStream);
		}
		
		final Serializable object = (Serializable) objectInputStream.readObject();
		
		return object;
	}
	
	
	public void send(final Serializable object) throws IOException {
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
	}
	
	
	public void close() throws IOException {
		inputStream.close();
		objectOutputStream.close();
	}
}
