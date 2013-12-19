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
public abstract class Skeleton<E extends IServant> {


    private E servant;
    private CommunicationObject receivedCommObject;


    protected abstract Object invokeMethod(CommunicationObject receivedCommObject);


    public Skeleton(E servant) {

        this.servant = servant;


    }

    public E getServant() {
        return servant;
    }

    public void initiate(Connection connection, CommunicationObject receivedCommObject) {

        this.receivedCommObject = receivedCommObject;

        SkeletonThread skeletonThread = new SkeletonThread(this, connection);

        skeletonThread.start();


    }

    public CommunicationObject processCommunication() {


        Object returnValue = invokeMethod(receivedCommObject);

        Object[] responseArray = new Object[]{returnValue};

        return new CommunicationObject(receivedCommObject.getServiceName(), receivedCommObject.getCallingMethodName(), responseArray);


    }


    private class SkeletonThread extends Thread {

        Skeleton skeleton;

        private Connection connection;

        public SkeletonThread(Skeleton skeleton, Connection connection) {

            this.skeleton = skeleton;

            this.connection = connection;

            //must run as a DaemonThread so that the VM stops if there is only this thread running and no other non-daemon thread
            this.setDaemon(true);

        }


        @Override
        public void run() {
            CommunicationObject responseCommunicationObject = skeleton.processCommunication();
            try {
                connection.send(responseCommunicationObject);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

}