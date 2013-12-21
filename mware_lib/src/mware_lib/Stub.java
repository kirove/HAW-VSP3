package mware_lib;

import utilities.Connection;

import java.net.InetSocketAddress;

/**
 * Created by me on 12/20/13.
 * Needs to be implemented by all stubs deriving from Application Interfaces (such as AccountImplBase, etc.)!
 */
public interface Stub {

    /**
     * A Connection needs to be opened in each overridden method of the application interfaces (such as AccountImplBase, etc.)!!!
     * @param inetSocketAddressServerApp
     * @return
     */
    Connection openConnection(InetSocketAddress inetSocketAddressServerApp);

    /**
     * A Connection needs to be closed in each overridden method of the application interfaces (such as AccountImplBase, etc.) in the end of invoking that method!!!
     * @param inetSocketAddressServerApp
     * @return
     */
    void closeConnection(Connection connection);
}
