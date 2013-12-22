package cash_access;


import mware_lib.IStub;
import utilities.CommunicationObject;
import utilities.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class TransactionStub extends TransactionImplBase implements IStub {

    private final String SERVICE_NAME;
    //    private Connection connectionToService;
    private InetSocketAddress inetSocketAddressServerApp;


    public TransactionStub(Object gor) {

        CommunicationObject gorCommObject = (CommunicationObject) gor;

        SERVICE_NAME = gorCommObject.getServiceName();

        this.inetSocketAddressServerApp = (InetSocketAddress) gorCommObject.getParametersArray()[0];


    }

    @Override
    public void deposit(String accountId, double amount) throws InvalidParamException {

        Connection connectionToService = openConnection(inetSocketAddressServerApp);


        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "deposit", new Object[]{accountId, amount});


        try {
            connectionToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = connectionToService.receive();

            // if not true then the return value is null (NullObject)
            if (receiveCommunicationObject.getParametersArray()[0] instanceof InvalidParamException) {
                throw (InvalidParamException) receiveCommunicationObject.getParametersArray()[0];
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        closeConnection(connectionToService);

    }

    @Override
    public void withdraw(String accountId, double amount) throws InvalidParamException, OverdraftException {

        Connection connectionToService = openConnection(inetSocketAddressServerApp);

        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "withdraw", new Object[]{accountId, amount});


        try {
            connectionToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = connectionToService.receive();

            // if not true then the return value is null (NullObject)
            if (receiveCommunicationObject.getParametersArray()[0] instanceof InvalidParamException) {
                throw (InvalidParamException) receiveCommunicationObject.getParametersArray()[0];
            }
            if (receiveCommunicationObject.getParametersArray()[0] instanceof OverdraftException) {
                throw (OverdraftException) receiveCommunicationObject.getParametersArray()[0];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        closeConnection(connectionToService);

    }

    @Override
    public double getBalance(String accountId) throws InvalidParamException {

        Connection connectionToService = openConnection(inetSocketAddressServerApp);

        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "getBalance", new Object[]{accountId});


        try {
            connectionToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = connectionToService.receive();

            // if not true then the return value is null (NullObject)
            if (receiveCommunicationObject.getParametersArray()[0] instanceof InvalidParamException) {
                throw (InvalidParamException) receiveCommunicationObject.getParametersArray()[0];
            }

            closeConnection(connectionToService);

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
