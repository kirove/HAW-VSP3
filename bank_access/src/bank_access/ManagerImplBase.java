package bank_access;


import mware_lib.IServant;
import mware_lib.Skeleton;

public abstract class ManagerImplBase implements IServant {
    /**
     * @param owner
     * @param branch: auf deutsch= filiale
     * @return
     */
    public abstract String createAccount(String owner, String branch);

    /**
     * downcast object to bank_access.ManagerImplBase
     *
     * @param gor: Object
     * @return bank_access.ManagerImplBase Object
     */
    public static ManagerImplBase narrowCast(Object gor) {
        return new ManagerStub(gor);
    }


    @Override
    public Skeleton getSkeleton(IServant servant) {
        return new ManagerSkeleton((ManagerImplBase) servant);
    }
}
