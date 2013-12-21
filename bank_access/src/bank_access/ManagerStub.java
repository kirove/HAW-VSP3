package bank_access;


import utilities.CommunicationObject;
import utilities.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Extends bank_access.ManagerImplBase is a Stub responsible for the communication with the server side using serialized Objects
 */
public class ManagerStub extends ManagerImplBase implements mware_lib.Stub {

    private final String SERVICE_NAME;
    private final InetSocketAddress inetSocketAddressServerApp;



    //private final InetSocketAddress inetSocketAddressServerApplication;


    public ManagerStub(Object gor) {

        CommunicationObject gorCommObject = (CommunicationObject) gor;

        SERVICE_NAME = gorCommObject.getServiceName();

        this.inetSocketAddressServerApp = (InetSocketAddress) gorCommObject.getParametersArray()[0];


    }

    @Override
    public String createAccount(String owner, String branch) {


        Connection connectionToService = openConnection(inetSocketAddressServerApp);

        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "createAccount", new Object[]{owner, branch});


        try {
            connectionToService.send(sendCommObject);

            CommunicationObject receiveCommunicationObject = connectionToService.receive();

            closeConnection(connectionToService);

            return (String) receiveCommunicationObject.getParametersArray()[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public Connection openConnection(InetSocketAddress inetSocketAddressServerApp) {


        try {
            Socket socket = new Socket(inetSocketAddressServerApp.getAddress(), inetSocketAddressServerApp.getPort());

            return new Connection(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}