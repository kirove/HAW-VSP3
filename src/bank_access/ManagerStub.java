package bank_access;

import mware_lib.networking.Stub;

import java.io.IOException;
import java.io.Serializable;

/**
 * Extends ManagerImplBase is a Stub responsible for the communication with the server side using serialized Objects
 */
public class ManagerStub extends ManagerImplBase {
    private final Stub client;


    public ManagerStub(final String host, final int port) throws IOException {
        this.client = new Stub(host, port);
    }

    @Override
    public String createAccount(String owner, String branch) {
        Serializable result = client.callMethod("createAccount", owner);

        // casting from Serializable into String
        return (String) result;
    }
}
