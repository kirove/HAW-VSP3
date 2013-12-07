package mware_lib.networking;

import java.io.Serializable;

public interface MethodCaller {
	Serializable callMethod(final String methodName, final Serializable parametersObject);
}
