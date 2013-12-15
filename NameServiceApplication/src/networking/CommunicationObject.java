package networking;

import java.io.Serializable;

public class CommunicationObject implements Serializable {

    private static final long serialVersionUID = -7521858389081708120L;

    private final String serviceName;
    private final String callingMehtodName;
    private final Object[] parametersArray;


    public CommunicationObject(final String serviceName, String callingMehtodName, final Object[] parametersArray) {
        this.serviceName = serviceName;
        this.callingMehtodName = callingMehtodName;
        this.parametersArray = parametersArray;
    }


    public final String getServiceName() {
        return serviceName;
    }


    public final Object[] getParametersArray() {
        return parametersArray;
    }

    public String getCallingMethodName() {
        return callingMehtodName;
    }
}