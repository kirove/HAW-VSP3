package cash_access;



import mware_lib.networking.CommunicationObject;
import mware_lib.networking.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class TransactionStub extends TransactionImplBase {

    private final String SERVICE_NAME;
    private Connection connectionToService;


    public TransactionStub(Object gor) {

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

    @Override
    public void deposit(String accountId, double amount) throws InvalidParamException {
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


    }

    @Override
    public void withdraw(String accountId, double amount) throws InvalidParamException, OverdraftException {
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


    }

    @Override
    public double getBalance(String accountId) throws InvalidParamException {
        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "getBalance", new Object[]{accountId});


        try {
            connectionToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = connectionToService.receive();

            // if not true then the return value is null (NullObject)
            if (receiveCommunicationObject.getParametersArray()[0] instanceof InvalidParamException) {
                throw (InvalidParamException) receiveCommunicationObject.getParametersArray()[0];
            }

            return (Double) receiveCommunicationObject.getParametersArray()[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
