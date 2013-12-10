package NameServiceApplication.name_service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/10/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisteredServices {

    private static RegisteredServices instance = null;
    private Map<String, ServiceReference> registeredServicesMap = new HashMap<String, ServiceReference>();


    private RegisteredServices() {
    }

    public static RegisteredServices getInstance() {
        if (instance == null) {

            instance = new RegisteredServices();

        }
        return instance;

    }

    public synchronized ServiceReference getServiceReference(String serviceName) {
        return registeredServicesMap.get(serviceName);

    }

    public synchronized void registerService(String serviceName, ServiceReference serviceReference) {
        this.registeredServicesMap.put(serviceName, serviceReference);

    }

}
