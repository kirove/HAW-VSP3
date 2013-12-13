package cash_access;

import java.io.IOException;
import java.io.Serializable;


public class TransactionStub extends TransactionImplBase {
    private final Stub client;

    public TransactionStub(final String host, final int port) throws IOException {
        this.client = new Stub(host, port);
    }

    @Override
    public void deposit(String accountId, double amount) throws InvalidParamException {
        Serializable result = client.callMethod("deposit", new Object []{accountId,new Double(amount)});

        if ( result instanceof InvalidParamException){
            throw (InvalidParamException) result;
        }
    }

    @Override
    public void withdraw(String accountId, double amount) throws InvalidParamException, OverdraftException {
        Serializable result = client.callMethod("withdraw", new Object []{accountId, new Double(amount)});

        if (result instanceof OverdraftException) {
            throw (OverdraftException)result;
        } else if ( result instanceof InvalidParamException){
            throw (InvalidParamException) result;
        }
    }

    @Override
    public double getBalance(String accountId) throws InvalidParamException {
        Serializable result = client.callMethod("getBalance", new Object []{accountId});

        if ( result instanceof InvalidParamException){
            throw (InvalidParamException) result;
        }

        return (Double)result;
    }
}
