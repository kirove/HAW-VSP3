package bank_access;

public abstract class ManagerImplBase {
    /**
     * @param owner
     * @param branch: (filiale)
     * @return
     */
    public abstract String createAccount(String owner, String branch);

    public static ManagerImplBase narrowCast(Object gor) {
        // TODO könnte eine Fehlerquelle sein
        return (ManagerStub) gor;
    }
}
