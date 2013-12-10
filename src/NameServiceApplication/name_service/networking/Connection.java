package NameServiceApplication.name_service.networking;

import java.io.*;
import java.net.Socket;

/**
 * is responsiable for all the communications, send/ receive / close Connection including Streams In/Output
 */
public class Connection {
    private final InputStream inputStream;
    private final ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    public Connection(Socket socket) throws IOException {
        inputStream = socket.getInputStream();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }


    public final CommunicationObject receive() throws IOException, ClassNotFoundException {
        if (objectInputStream == null) {
            // this is blocking if no data is available
            objectInputStream = new ObjectInputStream(inputStream);
        }

        // readObject deserializes  the incomming objectStream
        final CommunicationObject communicationObject = (CommunicationObject) objectInputStream.readObject();

        return communicationObject;
    }


    public void send(final CommunicationObject communicationObject) throws IOException {
        objectOutputStream.writeObject(communicationObject);
        objectOutputStream.flush();
    }


    public void close() throws IOException {
        inputStream.close();
        objectOutputStream.close();
    }
}
