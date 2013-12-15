import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Extends AccountImplBase is a Stub responsible for the communication with the server side using serialized Objects
 */
public class AccountStub extends AccountImplBase {

    private final String SERVICE_NAME;
    private Connection connectionBoundToService;


    //private final InetSocketAddress inetSocketAddressServerApplication;


    public AccountStub(Object gor) {

        CommunicationObject gorCommObject = (CommunicationObject) gor;

        SERVICE_NAME = gorCommObject.getServiceName();

        InetSocketAddress inetSocketAddressServerApp = (InetSocketAddress) gorCommObject.getParametersArray()[0];

        try {
            Socket socket = new Socket(inetSocketAddressServerApp.getAddress(), inetSocketAddressServerApp.getPort());

            connectionBoundToService = new Connection(socket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void transfer(double amount) throws OverdraftException {


        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "transfer", new Object[]{amount});

        try {
            connectionBoundToService.send(sendCommObject);

            CommunicationObject receiveCommunicationObject = connectionBoundToService.receive();

            if (receiveCommunicationObject.getParametersArray()[0] instanceof OverdraftException) {
                throw (OverdraftException) receiveCommunicationObject.getParametersArray()[0];
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getBalance() {
        CommunicationObject sendCommObject = new CommunicationObject(SERVICE_NAME, "getBalance", new Object[]{});

        try {
            connectionBoundToService.send(sendCommObject);

            CommunicationObject receiveCommunicationObject = connectionBoundToService.receive();

            return (Double) receiveCommunicationObject.getParametersArray()[0];


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}