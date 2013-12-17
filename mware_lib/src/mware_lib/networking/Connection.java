package mware_lib.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * is responsiable for all the communications, send/ receive / close Connection including Streams In/Output
 */
public class Connection {
   // private final InputStream inputStream;
   // private final ObjectOutputStream objectOutputStream;
   // private ObjectInputStream objectInputStream;
    private Socket socket;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
//        inputStream = socket.getInputStream();
//        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }


    public final CommunicationObject receive() throws IOException, ClassNotFoundException {
//        if (objectInputStream == null) {
//            // this is blocking if no data is available
//            objectInputStream = new ObjectInputStream(inputStream);
//        }

        System.out.println("in Connection.receive()");
        ObjectInputStream objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        System.out.println("in Connection.receive() after socket.getInputStream()");
        // readObject deserializes  the incomming objectStream
        final CommunicationObject communicationObject = (CommunicationObject) objectInputStream.readObject();
        System.out.println("in Connection.received: " + communicationObject);

        return communicationObject;
    }


    public void send(final CommunicationObject communicationObject) throws IOException {
        System.out.println("in Connection.send()");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.flush();
    }


    public void close() throws IOException {
        this.socket.close();

//        this.socket.getInputStream().close();
//        this.socket.getOutputStream().close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}

