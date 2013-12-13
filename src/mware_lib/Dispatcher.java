package mware_lib;

import mware_lib.networking.CommunicationObject;
import mware_lib.networking.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Cenan on 12.12.13.
 */
public class Dispatcher extends Thread {
    private ServerSocket serverSocket;
    private RegisteredSkeletons registeredSkeletons = RegisteredSkeletons.getInstance();


    public Dispatcher(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.setDaemon(true);
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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}

