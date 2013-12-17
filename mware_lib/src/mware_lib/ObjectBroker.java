package mware_lib;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ObjectBroker {

    private final Integer SERVER_APPLICATION_PORT = 45550;

//	private final String serviceHost;

    private final NameServiceImpl nameService;


    private ObjectBroker(String nameServerAddress, int nameServerPort) {
        InetAddress inetNameServerAddress = null;
        try {
            inetNameServerAddress = InetAddress.getByName(nameServerAddress);

        } catch (UnknownHostException e) {
                        System.err.println("mware_lib.ObjectBroker: Unknown host");
            throw new RuntimeException(e);

        }

        System.out.println("\nObjectBorker started !\nNameServer Ip: " + inetNameServerAddress.toString()+ "\nNameServer Port: " + nameServerPort);

        this.nameService = NameServiceImpl.getInstance(inetNameServerAddress, nameServerPort, SERVER_APPLICATION_PORT);

    }

    /**
     * Here is the Start Point where the Applications are starting to use the Middelware
     *
     * @param serviceHost: the Address of the Name service
     * @param port:  the Port of the Name service
     * @return : mware_lib.ObjectBroker Object
     */
    public static ObjectBroker init(String nameServerAddress, int nameServerPort) {

        System.out.println("\nObjectBorker initiated !\nNameServer Ip: " + nameServerAddress + "\nNameServer Port: " + nameServerPort);

        return new ObjectBroker(nameServerAddress, nameServerPort);

    }

    // Liefert den Namensdienst (Stellvetreterobjekt).
    public NameService getNameService() {
        return nameService;
    }


    public void shutdown(){
        this.nameService.shutdown();

    }



}
