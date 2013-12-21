package bank_access;


import mware_lib.Skeleton;
import mware_lib.VoidObject;
import utilities.CommunicationObject;

/**
 * Created by Cenan on 12.12.13.
 */
public class AccountSkeleton extends Skeleton<AccountImplBase> {


    public AccountSkeleton(AccountImplBase servant) {
        super(servant);
    }

    public Object invokeMethod(CommunicationObject receivedCommObject){


        if(receivedCommObject.getCallingMethodName().equals("transfer")){
            Double amount = (Double) receivedCommObject.getParametersArray()[0];
            try {
                super.getServant().transfer(amount);
                return VoidObject.getInstance();
            } catch (OverdraftException e) {
               return e;
            }
        }
        else if(receivedCommObject.getCallingMethodName().equals("getBalance")){
            return super.getServant().getBalance();
        }
         throw new RuntimeException("Method not Found in bank_access.AccountSkeleton");
    }
}
