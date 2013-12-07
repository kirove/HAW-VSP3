package bank_access;

import mware_lib.networking.Stub;

import java.io.IOException;
import java.io.Serializable;

/**
 * Extends AccountImplBase is a Stub responsible for the communication with the server side using serialized Objects
 */
public class AccountStub extends AccountImplBase {
    private final Stub client;


    public AccountStub(final String host, final int port) throws IOException {
        this.client = new Stub(host, port);
    }

    /**
     *
     * @param amount
     * @throws OverdraftException is thrown if result(coming from Server) is an Overdraft Exception Object.
     */
    @Override
    public void transfer(double amount) throws OverdraftException {

        Serializable result = client.callMethod("transfer", new Object[]{amount});

        if (result instanceof OverdraftException) {
            throw (OverdraftException) result;
        }
    }

    @Override
    public double getBalance() {
        Serializable result = client.callMethod("getBalance", new Object[]{});

        // casting from Serializable into Double
        return (Double) result;
    }
}
