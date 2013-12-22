package cash_access;


import mware_lib.IServant;
import mware_lib.Skeleton;

public abstract class TransactionImplBase implements IServant {

    public abstract void deposit(String accountId, double amount) throws InvalidParamException;

    public abstract void withdraw(String accountId, double amount) throws InvalidParamException, OverdraftException;

    public abstract double getBalance(String accountId) throws InvalidParamException;


    /**
     * downcast object to bank_access.AccountStub
     *
     * @param gor: Object
     * @return cash_access.TransactionImplBase Object
     */
    public static TransactionImplBase narrowCast(Object gor) {
        return new TransactionStub(gor);
    }


    @Override
    public Skeleton getSkeleton(IServant servant) {
        return new TransactionSkeleton((TransactionImplBase) servant);
    }

    @Override
    public String getSkeletonType() {
        return "TransactionImplBase";
    }

}