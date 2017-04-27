package Server.Deamons;

import Messages.Signal;
import Messages.SignalType;
import Server.Client.Client;
import Server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by piotr on 27.04.2017.
 */
public class ConnectionEstablisher implements Runnable{

    public ConnectionEstablisher (){
        thread = new Thread(this);
        thread.start();
    }


    public void run(){
        try (
                ServerSocket serverSocket = new ServerSocket(PORT_NR);
                ){
            serverSocket.setSoTimeout(TIMEOUT);

            while (true){

                try{
                    Client client = establishConnection(serverSocket);
                    Server.instance.send(new Signal(name, Server.instance.name, SignalType.clientConnected, client));
                }
                catch (IOException e){
                    if (thread.isInterrupted()){
                        throw e;
                    }

                }
            }

        }
        catch (Exception e){
            try{
                Server.instance.send(new Signal(name, Server.instance.name, SignalType.threadFinished));
            }
            catch (InterruptedException exception){
                System.err.println("Unable to exit properly. Program will exit.");
                System.err.println(e);
                System.err.println(exception);
                System.exit(-1);
            }
        }
    }




    public void interrupt (){
        thread.interrupt();
    }
    public void join() throws InterruptedException {
        thread.join();
    }


    private Client establishConnection(ServerSocket serverSocket) throws IOException{

        try
        {
            Socket socket = serverSocket.accept();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            while (true){

                Signal signal;
                try {
                    signal = (Signal) inputStream.readObject();
                    if (signal.signalType == SignalType.clientID){
                        if (Server.instance.validateClientID(signal.message)){
                            outputStream.writeObject(new Signal(Server.instance.name, signal.message, SignalType.clientIDAccepted));
                            return  new Client(signal.message, socket, inputStream, outputStream);
                        }
                    }
                    outputStream.writeObject(new Signal(Server.instance.name, "noID", SignalType.clientIDRejected));
                }

                catch (ClassNotFoundException e){
                    outputStream.writeObject(new Signal(Server.instance.name, "noID", SignalType.clientIDRejected));
                }

            }
        }
        catch (IOException e){
            throw e;
        }


    }

    private final String name = "__connection_establisher__";
    private final Thread thread;
    private static final int TIMEOUT = 1000;
    private static final int PORT_NR = 65256;
}
