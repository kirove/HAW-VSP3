package cash_access;

import mware_lib.networking.Stub;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

public class AccountStub extends Account {
	private final Stub client;
	
	
	public AccountStub(final String host, final int port) throws UnknownHostException, IOException {
		this.client = new Stub(host, port);
	}
	
	
	public void deposit(double amount) {
		client.callMethod(new Double(amount));
	}

	
	public void withdraw(double amount) throws OverdraftException {
		final Serializable result = client.callMethod(new Double(amount));
		
		if (result instanceof OverdraftException) {
			throw (OverdraftException)result;
		} else if (result != null) {
			System.out.println("Method withdraw has received a return value, but it has no one.");
		}
	}

	
	public double getBalance() {
		final Serializable result = client.callMethod(null);
		
		return (Double)result;
	}
}
