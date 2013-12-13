package mware_lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/10/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegisteredSkeletons {

    private static RegisteredSkeletons instance = null;
    private Map<String, Skeleton> registeredSkeletonMap = new HashMap<String, Skeleton>();


    private RegisteredSkeletons() {
    }

    public static RegisteredSkeletons getInstance() {
        if (instance == null) {

            instance = new RegisteredSkeletons();

        }
        return instance;

    }

    public synchronized Skeleton getSkeleton(String serviceName) {
        return registeredSkeletonMap.get(serviceName);

    }

    public synchronized void registerSkeleton(String serviceName, Skeleton serviceReference) {
        this.registeredSkeletonMap.put(serviceName, serviceReference);

    }

}
