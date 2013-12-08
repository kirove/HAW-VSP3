package NameServiceApplication.name_service;

import NameServiceApplication.name_service.networking.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
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


	public Main(int port) throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);

        try {
            // TODO change break statement (True into something else hi stephan)
            while (true) {
                final Socket socket = serverSocket.accept();
                new NameServiceThread(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	public Serializable callMethod(String methodName, Serializable parametersObject) {
		final NameServiceImpl nameService = (NameServiceImpl) servant;
		if(methodName.equals(resolveMsgString) && parametersObject.getClass().equals(String.class)) {
			//Its a resolve Request with a String as Parameter
			System.out.println("Resolve function "+parametersObject);
			final RemoteMethodConection response=(RemoteMethodConection)nameService.resolve((String)parametersObject);
			return response;
		}
		else if(methodName.equals(rebindMsgString) && parametersObject.getClass().equals(RebindRequest.class)) {
			System.out.println("Rebind Object, Request: "+parametersObject);
			final RebindRequest request= (RebindRequest)parametersObject;
			final String name = request.getMethodName();
			final RemoteMethodConection servant =  new RemoteMethodConection( request.getHostname(), request.getPort(), request.getServantType());
			nameService.rebind(servant, name);
		}
		
		return null;
	}

	public static void main(String[] args) {
		
		if(args.length != 1){
			System.err.println("Missing Port Argument!");
			return;
		}
        int	port = 0;
		try {
            port = Integer.valueOf(args[0]); //can throw NumberFormatException
			if(port<1024 || port>=65535){
				throw new NumberFormatException("Port out of Range! Port must be between 1024 and 65535");
			}
		} catch(NumberFormatException e) {
			System.err.println("Its not a allowed Port!");
		}
		
		try {
			// Start the NameServiceServer
			new Main(port);
		} catch (IOException e) {
			System.err.println("Couldnt Build up the NameServer!");
		}
	}


    private class NameServiceThread extends Thread{
        private Socket socket;
        public NameServiceThread(Socket socket) {
            this.socket = socket;
            this.start();
        }

        @Override
        public void run(){
            try {
                System.out.println("New worker thread for " + socket.getInetAddress());

                final Connection connection = new Connection(socket);

                while (!socket.isClosed() && !isInterrupted()) {
                    final MethodCallMessage request = (MethodCallMessage)connection.receive();

                    if (request.getMethodName() == "rebind"){
                        // skeleton(host, port)
                        // service name (Bsp. Konto ID)
                        /**
                         * TODO DatenTyp erstellen um die serviceName zu speichern (Z.b Map array)
                         */
                        request.getParametersObject();

                        
                    }else if (request.getMethodName() == "resolve"){

                    }

                    Serializable reply = null;
                    try {
                        reply = caller.callMethod(request.getMethodName(), request.getParametersObject());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        reply = e;
                    }
                    connection.send(reply);
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
