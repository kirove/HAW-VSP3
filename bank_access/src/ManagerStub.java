import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Extends ManagerImplBase is a Stub responsible for the communication with the server side using serialized Objects
 */
public class ManagerStub extends ManagerImplBase {

    private final String SERVICE_NAME;
    private Connection connectionToService;


    //private final InetSocketAddress inetSocketAddressServerApplication;


    public ManagerStub(Object gor) {

        CommunicationObject gorCommObject = (CommunicationObject) gor;

        SERVICE_NAME = gorCommObject.getServiceName();

        InetSocketAddress inetSocketAddressServerApp = (InetSocketAddress) gorCommObject.getParametersArray()[0];

        try {
            Socket socket = new Socket(inetSocketAddressServerApp.getAddress(), inetSocketAddressServerApp.getPort());

            connectionToService = new Connection(socket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public String createAccount(String owner, String branch) {
        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "createAccount", new Object[]{owner, branch});


        try {
            connectionToService.send(sendCommObject);
            CommunicationObject receiveCommunicationObject = connectionToService.receive();

            return (String) receiveCommunicationObject.getParametersArray()[0];

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }
}