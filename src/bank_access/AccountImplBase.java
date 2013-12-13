package bank_access;

import mware_lib.IServant;

public abstract class AccountImplBase  implements IServant{

    public abstract void transfer(double amount) throws OverdraftException;

    public abstract double getBalance();

    /**
     * downcast object to AccountStub
     *
     * @param o: Object
     * @return AccountImplBase Object
     */
    public static AccountImplBase narrowCast(Object gor) {
             return new AccountStub(gor);
    }
}