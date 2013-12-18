package bank_access;


import mware_lib.Skeleton;
import networking.CommunicationObject;

/**
 * Created by Cenan on 12.12.13.
 */
public class ManagerSkeleton extends Skeleton<ManagerImplBase> {


    public ManagerSkeleton(ManagerImplBase servant) {
        super(servant);
    }

    public Object invokeMethod(CommunicationObject receivedCommObject) {


        if (receivedCommObject.getCallingMethodName().equals("createAccount")) {


            String owner = (String) receivedCommObject.getParametersArray()[0];
            String branch = (String) receivedCommObject.getParametersArray()[1];
            System.out.println("ManagerSkeleton creating account: " + owner);

            return super.getServant().createAccount(owner, branch);


        }

        return new RuntimeException("Method not Found in bank_access.ManagerSkeleton");

    }
}
