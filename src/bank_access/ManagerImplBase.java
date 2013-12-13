package bank_access;

import mware_lib.IServant;
import sun.nio.cs.ext.ISCII91;

public abstract class ManagerImplBase implements IServant {
    /**
     * @param owner
     * @param branch: auf deutsch= filiale
     * @return
     */
    public abstract String createAccount(String owner, String branch);

    /**
     * downcast object to ManagerImplBase
     *
     * @param gor: Object
     * @return ManagerImplBase Object
     */
    public static ManagerImplBase narrowCast(Object gor) {
        // TODO könnte eine Fehlerquelle sein
        return (ManagerStub) gor;
    }
}
