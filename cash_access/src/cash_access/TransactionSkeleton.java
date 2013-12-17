package cash_access;


import mware_lib.Skeleton;
import mware_lib.VoidObject;

/**
 * Created by Cenan on 12.12.13.
 */
public class TransactionSkeleton extends Skeleton<TransactionImplBase> {


    public TransactionSkeleton(TransactionImplBase servant) {
        super(servant);
    }

    public Object invokeMethod(Object[] parameterArray){
        String methodName = (String)parameterArray[0];

        if(methodName.equals("deposit")){

            String accountID = (String ) parameterArray[1];
            Double amount = (Double) parameterArray[2];
            try {
                super.getServant().deposit(accountID, amount);
                return VoidObject.getInstance();
            } catch (InvalidParamException e) {
                return e;
            }
        }
        else if(methodName.equals("withdraw")){

            String accountID = (String ) parameterArray[1];
            Double amount = (Double) parameterArray[2];

            try {
                 super.getServant().withdraw(accountID, amount);
                return VoidObject.getInstance();
            } catch (InvalidParamException e) {
                return e;
            } catch (OverdraftException e) {
               return e;
            }
        }
        else if(methodName.equals("getBalance")){

            String accountID = (String ) parameterArray[1];

            try {
                return  super.getServant().getBalance(accountID);

            } catch (InvalidParamException e) {
                return e;
            }
        }


         return new RuntimeException("Method not Found in AccountSkeleton");
    }
}
