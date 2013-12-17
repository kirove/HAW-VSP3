

import networking.CommunicationObject;
import networking.Connection;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;


public class NameServerStarter {


    /**
     * defines the name of the communication protocol
     */
    static public final String resolveMsgString = "resolve";

    /**
     * defines the name of the communication protocol
     */
    static public final String rebindMsgString = "rebind";


    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Missing Port Argument!");
            return;
        }
        int port = 0;
        try {
            port = Integer.valueOf(args[0]); //can throw NumberFormatException
            if (port < 1024 || port >= 65535) {
                throw new NumberFormatException("Port out of Range! Port must be between 1024 and 65535");
            }
        } catch (NumberFormatException e) {
            System.err.println("Its not a allowed Port!");
        }

        try {

            // Start the NameServiceServer
            new NameServerStarter(port);


        } catch (IOException e) {
            System.err.println("Couldnt Build up the NameServer!");
        }
    }


    public NameServerStarter(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getLocalHost());

        System.out.println("NameServer started !\nIp: " + serverSocket.getInetAddress().getHostAddress() + "\nPort: " + serverSocket.getLocalPort());


        try {
            // TODO change break statement (True into something else hi stephan)
            // TODO Threadpool benutzen
            while (true) {
                final Socket socket = serverSocket.accept();
                new NameServiceThread(socket, RegisteredServices.getInstance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private class NameServiceThread extends Thread {
        private Socket socket;
        private RegisteredServices registeredServices;


        public NameServiceThread(Socket socket, RegisteredServices registeredServices) {
            this.socket = socket;
            this.registeredServices = registeredServices;
            this.start();
        }

        @Override
        public void run() {
            try {
                System.out.println("New worker thread for " + socket.getInetAddress());

                final Connection connection = new Connection(socket);


                while (!socket.isClosed() && !isInterrupted()) {


                    System.out.println("Ready to receive...");


                    final CommunicationObject request = connection.receive();


                    if (request.getCallingMethodName() == "rebind") {
                        System.out.println("rebinding...");
                        Object[] paramArray = request.getParametersArray();

                        //register new service
                        this.registeredServices.registerService(request.getServiceName(), (InetSocketAddress) paramArray[0]);


                        //done

                    } else if (request.getCallingMethodName() == "resolve") {

                        if (this.registeredServices.isRegisteredService(request.getServiceName())) {
                            System.out.println("resolving...");
                            // get the corresponding serviceReference
                            InetSocketAddress serviceReference = this.registeredServices.getServiceReference(request.getServiceName());
                            // set up the ParamArray for the message to send to client,
                            // requestParamArray[0] is the serviceName (String)
                            Object[] responseParamArray = new Object[]{serviceReference};
                            // set up the CommunicationObject to send
                            CommunicationObject sendCommunicationObject = new CommunicationObject(request.getServiceName(), request.getCallingMethodName(), responseParamArray);
                            //send the shit
                            connection.send(sendCommunicationObject);
                        }
                        // requested service is unknown...
                        else {
                            System.err.println("requested service is unknown...");

                            Object[] responseParamArray = new Object[]{new IllegalArgumentException("Requested service is unknown...")};
                            // set up the CommunicationObject to send
                            CommunicationObject sendCommunicationObject = new CommunicationObject(request.getServiceName(), request.getCallingMethodName(), responseParamArray);
                            //send the shit
                            connection.send(sendCommunicationObject);
                        }

                    }

                }

                if (!connection.isClosed()) {
                    connection.close();
                }
            }
            catch (EOFException e) {

                System.out.println("Unexpected end of stream...");
                return;
            }
        catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
