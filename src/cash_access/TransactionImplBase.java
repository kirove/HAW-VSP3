package cash_access;

import mware_lib.IServant;

public abstract class TransactionImplBase implements IServant {

    public abstract void deposit(String accountId,double amount) throws InvalidParamException;

    public abstract void withdraw(String accountId,double amount) throws InvalidParamException,OverdraftException;

    public abstract double getBalance(String accountId) throws InvalidParamException;

    public static TransactionImplBase narrowCast(Object o) {
        // TODO könnte eine Fehlerquelle sein
        return (TransactionStub) o;
    }
}