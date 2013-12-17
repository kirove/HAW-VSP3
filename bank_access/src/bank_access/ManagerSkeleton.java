package bank_access;


import mware_lib.Skeleton;

/**
 * Created by Cenan on 12.12.13.
 */
public class ManagerSkeleton extends Skeleton<ManagerImplBase> {


    public ManagerSkeleton(ManagerImplBase servant) {
        super(servant);
    }

    public Object invokeMethod(Object[] parameterArray) {
        String methodName = (String) parameterArray[0];

        if (methodName.equals("createAccount")) {

            String owner = (String) parameterArray[1];
            String branch = (String) parameterArray[2];

            return super.getServant().createAccount(owner, branch);


        }

        return new RuntimeException("Method not Found in bank_access.ManagerSkeleton");
    }
}
