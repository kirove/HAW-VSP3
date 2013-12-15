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
        return new ManagerStub(gor);
    }
}
