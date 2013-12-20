package networking;

import java.io.BufferedInputStream;
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
    private ObjectInputStream objectInputStream = null;
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
        if (this.objectInputStream == null) {
            this.objectInputStream = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
            System.out.println("in Connection.receive() objectInputStream was null");
        }
        System.out.println("in Connection.receive() after socket.getInputStream()");
        // readObject deserializes  the incomming objectStream


        final CommunicationObject communicationObject = (CommunicationObject) objectInputStream.readObject();

//        final Object receivedCrap= objectInputStream.readObject();
        System.out.println("in Connection.received vor cast auf CommunicationObject");
//        final CommunicationObject communicationObject = (CommunicationObject) receivedCrap;
        System.out.println("in Connection.received CommunicationObject: " + communicationObject + " from " + this.socket.getInetAddress() + " " + this.socket.getPort());



        return communicationObject;
    }


    public void send(final CommunicationObject communicationObject) throws IOException {
//        try {
//            this.wait(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("in Connection.send()");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(communicationObject);
        System.out.println("in Connection.send CommunicationObject: " + communicationObject + " to "  + this.socket.getInetAddress() + " " + this.socket.getPort());
        objectOutputStream.flush();
    }


    public void close() throws IOException {
        System.out.println("Closing connection...");
        this.socket.close();

//        this.socket.getInputStream().close();
//        this.socket.getOutputStream().close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}

