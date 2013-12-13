package bank_access;

import mware_lib.IServant;
import mware_lib.Skeleton;
import mware_lib.VoidObject;
import java.lang.Exception;

/**
 * Created by Cenan on 12.12.13.
 */
public class AccountSkeleton extends Skeleton<AccountImplBase> {


    public AccountSkeleton(AccountImplBase servant) {
        super(servant);
    }

    public Object invokeMethod(Object[] parameterArray){
        String methodName = (String)parameterArray[0];

        if(methodName.equals("transfer")){
            Double amount = (Double) parameterArray[1];
            try {
                super.getServant().transfer(amount);
                return VoidObject.getInstance();
            } catch (OverdraftException e) {
               return e;
            }
        }
        else if(methodName.equals("getBalance")){
            return super.getServant().getBalance();
        }
         return new RuntimeException("Method not Found in AccountSkeleton");
    }
}
