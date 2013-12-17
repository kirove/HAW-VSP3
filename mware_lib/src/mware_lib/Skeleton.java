package mware_lib; /**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/11/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */



import networking.CommunicationObject;
import networking.Connection;

import java.io.IOException;
import java.net.Socket;

// AccountStub
public abstract class Skeleton<E extends IServant> extends Thread {
    private E servant;
    private Connection connection;
    //this serviceName is set in the initialize method, it is then used to proof that the client isn't trying to work on a different service with a different servicename,
    private String serviceName;


    protected abstract Object invokeMethod(Object[] parametersArray);


    public Skeleton(E servant) {

        this.servant = servant;
        //must run as a DaemonThread so that the VM stops if there is only this thread running and no other non-daemon thread
        this.setDaemon(true);

    }

    public E getServant() {
        return servant;
    }

    public void initiate(Socket socket, CommunicationObject receivedCommObject) {

        try {


            connection = new Connection(socket);

            serviceName = receivedCommObject.getServiceName();

            processCommunication(receivedCommObject);

            this.start();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The connection can't be established...");

        }
    }

    private void processCommunication(CommunicationObject receivedCommObject) throws IOException {
        Object[] receivedParametersArray = receivedCommObject.getParametersArray();

        Object returnValue = invokeMethod(receivedParametersArray);

        Object[] responseArray = new Object[]{returnValue};

        CommunicationObject responseCommunicationObject = new CommunicationObject(receivedCommObject.getServiceName(), receivedCommObject.getCallingMehtodName(), responseArray);

        connection.send(responseCommunicationObject);
    }


    @Override
    public void run() {

        while (!isInterrupted()) {
            try {
                CommunicationObject communicationObject = connection.receive();

                // If this is true, client tried to use a different service (send the wrong serviceName) than that one for that the socket was created
                if (!communicationObject.getServiceName().equals(serviceName)) {

                    Object[] responseArray = new Object[]{new RuntimeException("Error: You are trying to use this reserved Socket for the wrong Service...")};

                    CommunicationObject responseCommunicationObject = new CommunicationObject(communicationObject.getServiceName(), communicationObject.getCallingMehtodName(), responseArray);

                    connection.send(responseCommunicationObject);
                } else {

                    processCommunication(communicationObject);

                }

            } catch (Exception e) {

                System.err.println("Exception in Skelleton.run(), exception is of Class: " + e.getClass() + " cause is: " + e.getCause());

                throw new RuntimeException(e);


            }
        }
    }
}
