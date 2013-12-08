package NameServiceApplication.name_service.networking;

import java.io.Serializable;

public class MethodCallMessage implements Serializable{

	private static final long serialVersionUID = -7521858389081708120L;
	
	private final String methodName;
	private final Serializable parametersObject;

	
	public MethodCallMessage(final String methodName, final Serializable parametersObject) {
		this.methodName = methodName;
		this.parametersObject = parametersObject;
	}

	
	public final String getMethodName() {
		return methodName;
	}

	
	public final Serializable getParametersObject() {
		return parametersObject;
	}
}
