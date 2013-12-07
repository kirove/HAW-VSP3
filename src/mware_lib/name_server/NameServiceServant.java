package mware_lib.name_server;

import mware_lib.Servant;
import mware_lib.networking.MethodCaller;

import java.io.IOException;
import java.io.Serializable;

public class NameServiceServant extends Servant implements MethodCaller {
	
	static public final String resolveMsgString = "resolve";
	static public final String rebindMsgString = "rebind";
	
	public NameServiceServant(Object servant) throws IOException{
		super(servant);
		
	}
	
	public Serializable callMethod(String methodName, Serializable parametersObject) {
		final NameServiceImpl nameService = (NameServiceImpl) servant;
		if(methodName.equals(resolveMsgString) && parametersObject.getClass().equals(String.class)) {
			//Its a resolve Request with a String as Parameter
			System.out.println("Resolve function "+parametersObject);
			final RemoteMethodConection response=(RemoteMethodConection)nameService.resolve((String)parametersObject);
			return response;
		}
		else if(methodName.equals(rebindMsgString) && parametersObject.getClass().equals(RebindRequest.class)) {
			System.out.println("Rebind Object, Request: "+parametersObject);
			final RebindRequest request= (RebindRequest)parametersObject;
			final String name = request.getMethodName();
			final RemoteMethodConection servant =  new RemoteMethodConection( request.getHostname(), request.getPort(), request.getServantType());
			nameService.rebind(servant, name);
		}
		
		return null;
	}
	
	
	

	public static void main(String[] args) {
		
		if(args.length != 1){
			System.err.println("Missing Port Argument!");
			return;
		}
		
		int port = 0;
		try {
			port = Integer.valueOf(args[0]); //can throw NumberFormatException
			if(port<1024 || port>=65535){
				throw new NumberFormatException("Port out of Range!");
			}
		} catch(NumberFormatException e) {
			System.err.println("Its not a allowed Port!");
		}
		
		try {
			// Start the NameServer not as a deamon so it do not stop on the end of the main thread
			new NameServiceServant(new NameServiceImpl());
		} catch (IOException e) {
			System.err.println("Couldnt Build up the NameServer!");
		}
	}

	

}
