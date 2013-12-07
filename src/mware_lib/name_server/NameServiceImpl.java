package mware_lib.name_server;

import mware_lib.NameService;

import java.util.HashMap;

public class NameServiceImpl extends NameService {

	private HashMap<String, Object> bindedMethods;
	public NameServiceImpl() {
		bindedMethods = new HashMap<String, Object>();
	}
	@Override
	public synchronized void rebind(Object servant, String name){
		//Remove existing entrie with the same name!
		if(bindedMethods.containsKey(name))
			bindedMethods.remove(name);
		
		//bind the new function
		bindedMethods.put(name, servant);
	}

	@Override
	public synchronized Object resolve(String methodName){
		return bindedMethods.get(methodName);
	}
	
}
