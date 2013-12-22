package mware_lib; /**
 * Created with IntelliJ IDEA.
 * User: me
 * Date: 12/11/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */


import utilities.CommunicationObject;
import utilities.Connection;
import utilities.MWareThreadPool;

import java.io.IOException;
import java.net.Socket;

// AccountStub
public abstract class Skeleton<E extends IServant> {


    private MWareThreadPool mWareThreadPool = MWareThreadPool.getFixedThreadPoolInstance(100);

    private E servant;


    protected abstract Object invokeMethod(CommunicationObject receivedCommObject);


    public Skeleton(E servant) {

        this.servant = servant;


    }

    public E getServant() {
        return servant;
    }

    public void initiate(Connection connection, CommunicationObject receivedCommObject) {


        SkeletonThread skeletonThread = new SkeletonThread(this, connection, receivedCommObject);

        mWareThreadPool.execute(skeletonThread);


    }

    public CommunicationObject processCommunication(CommunicationObject receivedCommObject) {


        Object returnValue = invokeMethod(receivedCommObject);

        Object[] responseArray = new Object[]{returnValue};

        return new CommunicationObject(receivedCommObject.getServiceName(), receivedCommObject.getCallingMethodName(), responseArray);


    }

    public void shutDown() {

        // shut down the thread pool
        mWareThreadPool.shutdownAndAwaitTermination();

    }


    private class SkeletonThread extends Thread {

        Skeleton skeleton;

        private Connection connection;

        private CommunicationObject receivedCommObject;

        public SkeletonThread(Skeleton skeleton, Connection connection, CommunicationObject receivedCommObject) {

            this.skeleton = skeleton;

            this.connection = connection;

            this.receivedCommObject = receivedCommObject;

            //must run as a DaemonThread so that the VM stops if there is only this thread running and no other non-daemon thread
            this.setDaemon(true);

        }


        @Override
        public void run() {

            System.out.println("########New SkeletonThread executed as " + Thread.currentThread());
            CommunicationObject responseCommunicationObject = skeleton.processCommunication(this.receivedCommObject);
            try {
                connection.send(responseCommunicationObject);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//

            try {
                this.connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}