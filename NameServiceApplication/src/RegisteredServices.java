import java.net.InetSocketAddress;
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
    // holding the adress of the server where you can reach the service
    private Map<String, InetSocketAddress> registeredServicesMap = new HashMap<String, InetSocketAddress>();


    private RegisteredServices() {
    }

    public static RegisteredServices getInstance() {
        if (instance == null) {

            instance = new RegisteredServices();

        }
        return instance;

    }

    public synchronized InetSocketAddress getServiceReference(String serviceName) {
        return registeredServicesMap.get(serviceName);

    }

    public synchronized void registerService(String serviceName, InetSocketAddress serviceReference) {
        this.registeredServicesMap.put(serviceName, serviceReference);

    }


    public boolean isRegisteredService(String serviceName) {

        return this.registeredServicesMap.containsKey(serviceName);

    }




}
