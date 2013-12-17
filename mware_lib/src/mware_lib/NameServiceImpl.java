package mware_lib;

import networking.CommunicationObject;
import networking.Connection;

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

    private DispatcherThread dispatcherThread = null;


    private static NameServiceImpl instance = null;
    private final RegisteredSkeletons REGISTERED_SKELETONS = RegisteredSkeletons.getInstance();

    private final InetSocketAddress inetSocketAddressNameServer;
    private final InetSocketAddress inetSocketAddressServerApplication;

    public static NameServiceImpl getInstance(InetAddress inetAddressNameServer, int nameServerPort, int serverApplicationPort) {
        if (instance == null) {
            instance = new NameServiceImpl(inetAddressNameServer, nameServerPort, serverApplicationPort);

        }
        return instance;
    }

    private NameServiceImpl(InetAddress inetAddressNameServer, int nameServerPort, int serverApplicationPort) {
        InetAddress inetAddressServerApplication = null;
        try {
            inetAddressServerApplication = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.inetSocketAddressNameServer = new InetSocketAddress(inetAddressNameServer, nameServerPort);
        this.inetSocketAddressServerApplication = new InetSocketAddress(inetAddressServerApplication, serverApplicationPort);

    }


    //TODO: Interface definieren

    @Override
    public void rebind(Object servant, String serviceName) {

        if (this.dispatcherThread == null) {
            this.dispatcherThread = DispatcherThread.getInstance(inetSocketAddressServerApplication.getPort());
            this.dispatcherThread.start();
        }

        IServant specifiedServant = (IServant) servant;
        Skeleton skeleton = specifiedServant.getSkeleton(specifiedServant);

        //Put the mware_lib.Skeleton in a Map
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


        Connection connection = null;
        CommunicationObject gor = null;
        try {
            connection = new Connection(new Socket(inetSocketAddressNameServer.getAddress(), inetSocketAddressNameServer.getPort()));


            CommunicationObject sendCommObject = new CommunicationObject(serviceName, "resolve", new Object[]{});

            connection.send(sendCommObject);

            gor = connection.receive();

            // NameServer is sending an exception if the requested service name wasn't found
            if (gor.getParametersArray()[0] instanceof Exception) {
                throw new RuntimeException((Exception) gor.getParametersArray()[0]);
            }


            connection.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return gor;


    }


    public void shutdown() {
        this.dispatcherThread.interrupt();

    }


}