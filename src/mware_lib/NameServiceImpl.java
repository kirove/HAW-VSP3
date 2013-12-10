package mware_lib;

import bank_access.AccountImplBase;
import bank_access.ManagerImplBase;
import cash_access.TransactionImplBase;
import mware_lib.NameService;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/10/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceImpl extends NameService {

    //TODO: Macht hier singleton überhaupt sinn? -definitiv nicht wenn nur der objectbroker referenzen hierauf vergibt...

    private static NameServiceImpl instance = null;
    private final InetAddress inetAddress;
    private final int listenPort;

    public static NameServiceImpl getInstance(InetAddress inetAddress, int listenPort) {
        if (instance == null) {
            instance = new NameServiceImpl(inetAddress, listenPort);

        }
        return instance;
    }

    private NameServiceImpl(InetAddress inetAddress, int listenPort) {
        this.inetAddress = inetAddress;
        this.listenPort = listenPort;

    }


    @Override
    public void rebind(Object servant, String name) {
        if (servant instanceof ManagerImplBase) {
            /**
             *  cast Servant, save Servant in Service-Name-Servant Map with name as key pointing on it, tell NameServer
             */


        } else if (servant instanceof AccountImplBase) {

        } else if (servant instanceof TransactionImplBase) {

        }


        throw new RuntimeException(new ClassNotFoundException("NameServiceImpl: Servant of unknown type!"));
    }

    @Override
    public Object resolve(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
