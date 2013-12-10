package NameServiceApplication.name_service.networking;

import java.io.Serializable;

public class CommunicationObject implements Serializable {

    private static final long serialVersionUID = -7521858389081708120L;

    private final String methodName;
    private final Object[] parametersArray;


    public CommunicationObject(final String methodName, final Object[] parametersArray) {
        this.methodName = methodName;
        this.parametersArray = parametersArray;
    }


    public final String getMethodName() {
        return methodName;
    }


    public final Object[] getParametersArray() {
        return parametersArray;
    }
}
