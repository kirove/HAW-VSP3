package mware_lib;





import utilities.CommunicationObject;
import utilities.Connection;

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
    private  InetSocketAddress I_NET_SOCKET_ADDRESS_SERVER_APP;

    private NameServiceImpl(InetAddress inetAddressNameServer, int nameServerPort, int applicationPort) {
        InetAddress inetAddressServerApplication = null;
        try {
            inetAddressServerApplication = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.inetSocketAddressNameServer = new InetSocketAddress(inetAddressNameServer, nameServerPort);
        this.I_NET_SOCKET_ADDRESS_SERVER_APP = new InetSocketAddress(inetAddressServerApplication, applicationPort);

        System.out.println("\nLocal nameService(impl) started !\nNameServer Ip: " + inetSocketAddressNameServer.getAddress().toString() + "\nNameServer Port: " + inetSocketAddressNameServer.getPort());
    }

    public static NameServiceImpl getInstance(InetAddress inetAddressNameServer, int nameServerPort, int applicationPort) {


        if (instance == null) {
            instance = new NameServiceImpl(inetAddressNameServer, nameServerPort, applicationPort);

        }
        return instance;
    }


    //TODO: Interface definieren

    @Override
    public void rebind(Object servant, String serviceName) {

        if (this.dispatcherThread == null) {
            this.dispatcherThread = DispatcherThread.getInstance(I_NET_SOCKET_ADDRESS_SERVER_APP.getPort());
        }
        //needs to be set again, for debugging reasons (when we start the applications with a 0 as PORT for randomized port generation)
        I_NET_SOCKET_ADDRESS_SERVER_APP = new InetSocketAddress(I_NET_SOCKET_ADDRESS_SERVER_APP.getAddress(), this.dispatcherThread.getPort());


        IServant specifiedServant = (IServant) servant;
        Skeleton skeleton = specifiedServant.getSkeleton(specifiedServant);

        //Put the mware_lib.Skeleton in a Map
        this.REGISTERED_SKELETONS.registerSkeleton(serviceName, skeleton);

        Object[] paramArray = new Object[]{I_NET_SOCKET_ADDRESS_SERVER_APP};
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
//        this.dispatcherThread.interrupt();

    }


}
