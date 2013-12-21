package mware_lib;





import utilities.CommunicationObject;
import utilities.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread for dispatching incomming requests   from clients. Delegates request from clients to the corresponding Skeletons passing them the corresponding socket
 */
public class DispatcherThread extends Thread {
    private ServerSocket serverSocket;
    private RegisteredSkeletons registeredSkeletons = RegisteredSkeletons.getInstance();

    private static DispatcherThread instance = null;

    private DispatcherThread(int applicationPort) {
        try {
            this.serverSocket = new ServerSocket(applicationPort);
            System.out.println("Dispatcher says: 'My socket uses the port: "  + serverSocket.getLocalPort() + "'");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.setDaemon(true);

    }


    /**
     * @param applicationPort
     * @return
     */
    public static DispatcherThread getInstance(int applicationPort) {
        if (instance == null) {
            instance = new DispatcherThread(applicationPort);
            instance.start();
        }

        return instance;

    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void run() {

        try {
            while (!isInterrupted()) {
                System.out.println("DispatcherThread waiting for request...");
                Socket socket = this.serverSocket.accept();


                Connection connection = new Connection(socket);

                CommunicationObject communicationObject = connection.receive();

                // get skeleton corresponding to the ServiceName
                Skeleton skeleton = registeredSkeletons.getSkeleton(communicationObject.getServiceName());


                skeleton.initiate(connection, communicationObject);



            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


}

