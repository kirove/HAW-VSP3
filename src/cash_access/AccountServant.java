package cash_access;

import mware_lib.Servant;

import java.io.IOException;
import java.io.Serializable;

public class AccountServant extends Servant {

	public AccountServant(Object servant) throws IOException {
		super(servant);
	}

	@Override
	public Serializable callMethod(String methodName, Serializable parametersObject) {
		final Account account = (Account) servant;

		if (methodName.equals("deposit")) {
			account.deposit((Double) parametersObject);
			return null;
		} else if (methodName.equals("getBalance")) {
			final double result = account.getBalance();
			return new Double(result);
		} else if (methodName.equals("withdraw")) {
			try {
				account.withdraw((Double) parametersObject);
			} catch (OverdraftException e) {
				return e;
			}
			return null;
		} else {
			final String message = "Unknown method " + methodName + " for class " + servant.getClass().getName() + " was called by the remote client!";
			System.err.println(message);
			// throw an exception which is interpreted by the client
			return new RuntimeException(message);
		}
	}

}
