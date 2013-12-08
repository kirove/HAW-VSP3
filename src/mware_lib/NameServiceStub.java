package mware_lib;

import name_service. ;
import name_service.RebindRequest;
import name_service.RemoteMethodConection;
import mware_lib.networking.Stub;
import java.io.IOException;
import java.net.UnknownHostException;

public class NameServiceStub extends NameService {
    /**
     * Provide the connection to the name server
     */
	private Stub stub;

	public NameServiceStub(String host, int port){
		try{
			//connect the nameServer
			this.stub=new Stub(host, port);
		} catch (UnknownHostException e) {
			System.err.println("Couldnt connect to the NameServer!");
			this.stub=null;
		} catch (IOException e) {
			this.stub=null;
			System.err.println("Couldnt connect to the NameServer!");
		}
	}
	// Meldet ein Objekt (servant) beim Namensdienst an.
	// Eine eventuell schon vorhandene Objektreferenz gleichen Namens
	// soll �berschrieben werden.

    /**
     *
     * @param servant
     * @param name
     */
    @Override
	public void rebind(Object servant, String name) {
		if(stub==null) return;
        /**
         * Servant containing the Skelton and the corresponding server socket
         */
		Servant servantServer = ObjectBroker.getServant(servant);
		stub.callMethod(NameServiceServant.rebindMsgString,
				new RebindRequest(servantServer.getHost(), servantServer.getPort(), name, ObjectBroker.getAssoziationType(servantServer)));
	}
	// Liefert die Objektreferenz (Stellvertreterobjekt) zu einem Namen.
	@Override
	public Object resolve(String name) {
		if(stub==null) return null;
		//Send a Resolve Request
		Object reply = stub.callMethod(NameServiceServant.resolveMsgString, name);
		
		if(reply==null || !(reply.getClass().equals(RemoteMethodConection.class)) ) {
			System.err.println("Recieve no acceped Object from the Nameserver!");
			return null;
		}
		
		return ObjectBroker.getStub((RemoteMethodConection) reply);
	}
}
