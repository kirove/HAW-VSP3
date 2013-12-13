package mware_lib;

import bank_access.AccountImplBase;
import bank_access.AccountSkeleton;
import bank_access.ManagerImplBase;
import bank_access.ManagerSkeleton;
import cash_access.TransactionImplBase;
import cash_access.TransactionSkeleton;
import mware_lib.networking.CommunicationObject;
import mware_lib.networking.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/10/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceImpl extends NameService {

    //TODO: Macht hier singleton überhaupt sinn? -definitiv nicht wenn nur der objectbroker referenzen hierauf vergibt...

    private final Integer PORT = 45550;

    private static NameServiceImpl instance = null;
    private final RegisteredSkeletons REGISTERED_SKELETONS = RegisteredSkeletons.getInstance();
    //TODO We need a new InetSocketAdress for the Server
    private final InetSocketAddress inetSocketAddressNameServer;
    private final InetSocketAddress inetSocketAddressServerApplication;

    public static NameServiceImpl getInstance(InetAddress inetAddress, int listenPort) {
        if (instance == null) {
            instance = new NameServiceImpl(inetAddress, listenPort);

        }
        return instance;
    }

    private NameServiceImpl(InetAddress inetAddress, int listenPort) {
        InetAddress inetAddressServerApplication = null;
        try {
            inetAddressServerApplication = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.inetSocketAddressNameServer = new InetSocketAddress(inetAddress, listenPort);
        this.inetSocketAddressServerApplication = new InetSocketAddress(inetAddressServerApplication, PORT);

    }

    // TODO here is the next point
    @Override
    public void rebind(Object servant, String serviceName) {

        if (servant instanceof AccountImplBase) {

            Skeleton skeleton = new AccountSkeleton((AccountImplBase) servant);
            processRebind(serviceName, skeleton);


        } else if (servant instanceof ManagerImplBase) {

            Skeleton skeleton = new ManagerSkeleton((ManagerImplBase) servant);
            processRebind(serviceName, skeleton);


        } else if (servant instanceof TransactionImplBase) {
            Skeleton skeleton = new TransactionSkeleton((TransactionImplBase) servant);
            processRebind(serviceName, skeleton);
        }


        throw new RuntimeException(new ClassNotFoundException("NameServiceImpl: Servant of unknown type!"));
    }

    private void processRebind(String serviceName, Skeleton skeleton) {
        //Put the Skeleton in a Map
        this.REGISTERED_SKELETONS.registerSkeleton(serviceName, skeleton);

        Object[] paramArray = new Object[]{inetSocketAddressServerApplication};
        CommunicationObject sendCommObject = new CommunicationObject(serviceName, "rebind", paramArray);

        try {
            Connection connection = new Connection(new Socket(inetSocketAddressNameServer.getAddress(), inetSocketAddressNameServer.getPort()));

            connection.send(sendCommObject);
            connection.close();

        } catch (IOException e) {
            throw new RuntimeException(e);  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Override
    public Object resolve(String serviceName) {

        try {
            Connection connection = new Connection(new Socket(inetSocketAddressNameServer.getAddress(), inetSocketAddressNameServer.getPort()));

            CommunicationObject sendCommObject = new CommunicationObject(serviceName, "resolve", new Object[]{});

            connection.send(sendCommObject);

            CommunicationObject gor = connection.receive();

            connection.close();

            return gor;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
