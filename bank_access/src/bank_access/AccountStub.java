package bank_access;


import mware_lib.IStub;
import utilities.CommunicationObject;
import utilities.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Extends bank_access.AccountImplBase is a IStub responsible for the communication with the server side using serialized Objects
 */
public class AccountStub extends AccountImplBase implements IStub {

    private final String SERVICE_NAME;
    private final InetSocketAddress inetSocketAddressServerApp;


    //private final InetSocketAddress inetSocketAddressServerApplication;


    public AccountStub(Object gor) {

        CommunicationObject gorCommObject = (CommunicationObject) gor;

        SERVICE_NAME = gorCommObject.getServiceName();

        this.inetSocketAddressServerApp = (InetSocketAddress) gorCommObject.getParametersArray()[0];


    }

    @Override
    public void transfer(double amount) throws OverdraftException {
        Connection connectionBoundToService = openConnection(inetSocketAddressServerApp);

        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "transfer", new Object[]{amount});


        try {
            connectionBoundToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = null;

            receiveCommunicationObject = connectionBoundToService.receive();

            if (receiveCommunicationObject.getParametersArray()[0] instanceof OverdraftException) {
                throw (OverdraftException) receiveCommunicationObject.getParametersArray()[0];
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        closeConnection(connectionBoundToService);
    }

    @Override
    public double getBalance() {

        Connection connectionBoundToService = openConnection(inetSocketAddressServerApp);
        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "getBalance", new Object[]{});

        try {
            connectionBoundToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = connectionBoundToService.receive();
            closeConnection(connectionBoundToService);
            return (Double) receiveCommunicationObject.getParametersArray()[0];
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
