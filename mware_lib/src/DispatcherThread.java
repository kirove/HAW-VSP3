

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

    private DispatcherThread(int serverApplicationPort) {
        try {
            this.serverSocket = new ServerSocket(serverApplicationPort);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.setDaemon(true);

    }


    /**
     *
     * @param serverApplicationPort
     * @return
     */
    public static DispatcherThread getInstance(int serverApplicationPort) {
        if (instance == null) {
            instance = new DispatcherThread(serverApplicationPort);
        }

        return instance;

    }


    public void run() {


        try {
            while (!isInterrupted()) {
                Socket socket = this.serverSocket.accept();

                Connection connection = new Connection(socket);

                CommunicationObject communicationObject = connection.receive();

                // get skeleton corresponding to the ServiceName
                Skeleton skeleton = registeredSkeletons.getSkeleton(communicationObject.getServiceName());

                skeleton.initiate(socket, communicationObject);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

