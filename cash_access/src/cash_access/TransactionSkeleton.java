package cash_access;


import mware_lib.Skeleton;
import mware_lib.VoidObject;
import networking.CommunicationObject;

/**
 * Created by Cenan on 12.12.13.
 */
public class TransactionSkeleton extends Skeleton<TransactionImplBase> {


    public TransactionSkeleton(TransactionImplBase servant) {
        super(servant);
    }

    public Object invokeMethod(CommunicationObject receivedCommObject){


        if(receivedCommObject.getCallingMethodName().equals("deposit")){

            String accountID = (String ) receivedCommObject.getParametersArray()[0];
            Double amount = (Double) receivedCommObject.getParametersArray()[1];
            try {
                super.getServant().deposit(accountID, amount);
                return VoidObject.getInstance();
            } catch (InvalidParamException e) {
                return e;
            }
        }
        else if(receivedCommObject.getCallingMethodName().equals("withdraw")){

            String accountID = (String ) receivedCommObject.getParametersArray()[0];
            Double amount = (Double) receivedCommObject.getParametersArray()[1];

            try {
                 super.getServant().withdraw(accountID, amount);
                return VoidObject.getInstance();
            } catch (InvalidParamException e) {
                return e;
            } catch (OverdraftException e) {
               return e;
            }
        }
        else if(receivedCommObject.getCallingMethodName().equals("getBalance")){

            String accountID = (String ) receivedCommObject.getParametersArray()[0];

            try {
                return  super.getServant().getBalance(accountID);

            } catch (InvalidParamException e) {
                return e;
            }
        }


         return new RuntimeException("Method not Found in AccountSkeleton");
    }
}
