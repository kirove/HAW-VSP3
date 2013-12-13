package mware_lib;

import bank_access.ManagerImplBase;
import bank_access.ManagerStub;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ObjectBroker {


//	private final String serviceHost;

    private final NameService nameService;

    private ObjectBroker(String nameServerAddress, int nameServerPort) {
        InetAddress inetNameServerAddress = null;
        try {
            inetNameServerAddress = InetAddress.getByName(nameServerAddress);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("ObjectBroker: Unknown host");
            //To change body of catch statement use File | Settings | File Templates.
        }
        this.nameService = NameServiceImpl.getInstance(inetNameServerAddress, nameServerPort);

    }

    /**
     * Here is the Start Point where the Applications are starting to use the Middelware
     *
     * @param serviceHost: the Address of the Name service
     * @param port:  the Port of the Name service
     * @return : ObjectBroker Object
     */
    public static ObjectBroker init(String serviceHost, int port) {


        return new ObjectBroker(serviceHost, port);

    }

    // Liefert den Namensdienst (Stellvetreterobjekt).
    public NameService getNameService() {
        return nameService;
    }




}
