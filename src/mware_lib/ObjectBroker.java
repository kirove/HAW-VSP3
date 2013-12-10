package mware_lib;

import bank_access.ManagerImplBase;
import bank_access.ManagerServant;
import bank_access.ManagerStub;
import cash_access.AccountServant;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ObjectBroker {


//	private final String serviceHost;

    private final NameService nameService;

    private ObjectBroker(String serviceHost, int listenPort) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(serviceHost);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.err.println("ObjectBroker: Unknown host");
            //To change body of catch statement use File | Settings | File Templates.
        }
        this.nameService = NameServiceImpl.getInstance(inetAddress, listenPort);

    }

    /**
     * Here is the Start Point where the Applications are starting to use the Middelware
     *
     * @param serviceHost: the Address of the Name service
     * @param listenPort:  the Port of the Name service
     * @return : ObjectBroker Object
     */
    public static ObjectBroker init(String serviceHost, int listenPort) {
        return new ObjectBroker(serviceHost, listenPort);
    }

    // Liefert den Namensdienst (Stellvetreterobjekt).
    public NameService getNameService() {
        return nameService;
    }

    //Create new ServantServer for this Object:
    public static Servant getServant(Object servant) {
        try {
            if (servant instanceof Account)
                return new AccountServant(servant);
            else if (servant instanceof ManagerImplBase)
                return new ManagerServant(servant);
        } catch (IOException e) {
            System.err.println("Couldnt build up a servant for " + servant);
        }
        return null;
    }

    public static ServantTypeAssoziation getAssoziationType(Servant servant) {
        ServantTypeAssoziation tmpType = ServantTypeAssoziation.none;
        if (servant instanceof ManagerServant)
            tmpType = ServantTypeAssoziation.Manger;
        if (servant instanceof AccountServant)
            tmpType = ServantTypeAssoziation.Account;
        return tmpType;
    }

    //create Stubs
    public static Object getStub(RemoteMethodConection remoteMethod) {
        Object stub = null;
        if (remoteMethod.getServantType().equals(ServantTypeAssoziation.Manger)) {
            try {
                stub = new ManagerStub(remoteMethod.getHostname(), remoteMethod.getPort());
            } catch (Exception e) {
                System.err.println("Couldnt build a Stub for a ManagerImplBase!");
            }
        } else if (remoteMethod.getServantType().equals(ServantTypeAssoziation.Account)) {
            try {
                stub = new AccountStub(remoteMethod.getHostname(), remoteMethod.getPort());
            } catch (Exception e) {
                System.err.println("Couldnt build a Stub for a Account!");
            }
        }

        if (stub == null) {
            System.err.println("Couldnt build a Stub!");
        }
        return stub;
    }
}
