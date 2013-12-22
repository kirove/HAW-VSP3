package bank_access;


import mware_lib.IServant;
import mware_lib.Skeleton;

public  abstract class AccountImplBase implements IServant {

    public abstract void transfer(double amount) throws OverdraftException;

    public abstract double getBalance();

    /**
     * downcast object to bank_access.AccountStub
     *
     * @param gor: Object
     * @return bank_access.AccountImplBase Object
     */
    public static AccountImplBase narrowCast(Object gor) {
             return new AccountStub(gor);
    }


    @Override
    public Skeleton getSkeleton(IServant servant) {
        return new AccountSkeleton((AccountImplBase) servant);
    }

    @Override
    public String getSkeletonType() {
        return "AccountImplBase";
    }
}