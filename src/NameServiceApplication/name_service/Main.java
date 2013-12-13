package NameServiceApplication.name_service;

import NameServiceApplication.name_service.networking.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;


public class Main {


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
            new Main(port);
        } catch (IOException e) {
            System.err.println("Couldnt Build up the NameServer!");
        }
    }


    public Main(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        try {
            // TODO change break statement (True into something else hi stephan)
            while (true) {
                final Socket socket = serverSocket.accept();
                new NameServiceThread(socket, RegisteredServices.getInstance());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private class NameServiceThread extends Thread   {
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

                    final CommunicationObject request = connection.receive();



                    if (request.getCallingMethodName() == "rebind") {

                        Object[] paramArray = request.getParametersArray();

                        //register new service
                        this.registeredServices.registerService(request.getServiceName(), (InetSocketAddress) paramArray[0]);


                        //done

                    } else if (request.getCallingMethodName() == "resolve") {

                        Object[] requestParamArray = request.getParametersArray();

                        // get the corresponding serviceRefernce
                        InetSocketAddress serviceReference = this.registeredServices.getServiceReference(request.getServiceName());
                        // set up the ParamArray for the message to send to client,
                        // requestParamArray[0] is the serviceName (String)
                        Object[] responseParamArray = new Object[]{serviceReference};
                        // set up the CommunicationObject to send
                        CommunicationObject sendCommunicationObject = new CommunicationObject(request.getServiceName(), request.getCallingMethodName(), responseParamArray);
                        //send the shit
                        connection.send(sendCommunicationObject);

                    }

                }

                connection.close();
            } catch (EOFException e) {
                // connection was closed by the client, so close the worker thread for this client
                System.out.println("Connection closed by client!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
