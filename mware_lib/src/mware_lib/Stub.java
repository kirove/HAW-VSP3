package mware_lib;



import networking.CommunicationObject;
import networking.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by me on 12/17/13.
 */
public abstract class Stub {

    private final String SERVICE_NAME;
    private Connection connectionToService;


    public Stub(Object gor) {

        CommunicationObject gorCommObject = (CommunicationObject) gor;

        SERVICE_NAME = gorCommObject.getServiceName();

        InetSocketAddress inetSocketAddressServerApp = (InetSocketAddress) gorCommObject.getParametersArray()[0];

        try {
            Socket socket = new Socket(inetSocketAddressServerApp.getAddress(), inetSocketAddressServerApp.getPort());

            connectionToService = new Connection(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
