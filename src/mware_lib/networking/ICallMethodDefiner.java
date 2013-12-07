package mware_lib.networking;

import java.io.Serializable;

/**
 * defines the callMethod method, its Parameters and its return Values
 */
public interface ICallMethodDefiner {

	Serializable callMethod(final String methodName, final Serializable parametersObject);
}
