package Server;


import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signal;
import Server.Client.Client;
import Server.Deamons.ConnectionEstablisher;
import Server.Interfaces.Receiver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Vector;

/**
 * Created by piotr on 27.04.2017.
 */
public class Server implements Receiver{


    public static void main (String [] args){

        instance.init();
        instance.run();
        instance.close();

    }


    public boolean validateClientID(String clientID) {
        for (Client client : clients){
            if (client.getClientID().equals(clientID)){
                return false;
            }
        }
        return true;
    }

    public void send(Message message) throws InterruptedException{
        messages.addMessage(message);
    }

    private void init(){
        connectionEstablisher = new ConnectionEstablisher();

    }

    private void run (){

        Dispatcher dispatcher = new Dispatcher();

        while (true){
            try {

                Message message = messages.getMessage();

                System.out.println(message.toString());

                message.acceptADispatcher(dispatcher);

               if (Thread.interrupted()) break;
            }
            catch (InterruptedException e){

            }
        }
    }

    private void close(){

        connectionEstablisher.interrupt();

        try{
            connectionEstablisher.join();
            for (Client client : clients){
                client.close();
            }
        }
        catch (Exception e){
            System.err.println("An error occurred while closing program");
            System.err.println(e);
            System.exit(1);
        }

    }


    public class Dispatcher{

        /**
         * This method dispatches a signal.
         * @param signal a signal to be dispatched.
         */
        public void dispatch(Signal signal){
            switch (signal.signalType){
                case clientConnected:
                    try {
                        Client client = Client.class.cast(signal.object);
                        instance.clients.addElement(client);

                        client.start();
                    }
                    catch (ClassCastException e){
                        System.err.println("invalid client given");
                    }
                    break;

                case clientConnectionError:
                case connectionWithClientClosed:
                    try {
                        Client client = Client.class.cast(signal.object);
                        instance.clients.remove(client);
                        client.close();
                    }
                    //TODO: EXCEPTIONS
                    catch (ClassCastException e){
                        System.err.println("invalid client given");
                    }
                    catch (InterruptedException e){
                        System.err.println(e.getMessage());
                        throw new NotImplementedException();
                    }
                    catch (IOException e){
                        System.err.println(e.getMessage());
                        throw new NotImplementedException();
                    }
                    break;
                case closeServer:
                    Thread.currentThread().interrupt();
                    break;
                default:
                    throw new NotImplementedException();
                    //TODO
                    // break;
            }



        }

        /**
         * This method dispatches a message.
         * @param message a message to be dispatched.
         * @throws InvalidParameterException when such a client does not exist.
         */
        public void dispatch(Message message) throws InvalidParameterException, InterruptedException  {

            //TODO: message dispatcher
            if (message.receiver.equals(name)){

                if (message.message.equals("exit")) Thread.currentThread().interrupt();

                System.out.println(message.message);
                return;
            }

            for (Client client : instance.clients) {
                if (message.receiver.equals(client.getClientID())){
                    client.send(message);
                    return;
                }
            }

            throw new InvalidParameterException("Such a client does not exist");
        }
    }


    public static final Server instance = new Server();

    private Server(){}


    public final String name = "__server__";
    private Vector<Client> clients = new Vector<>();
    private ConnectionEstablisher connectionEstablisher = null;
    private MessagesQueue messages = new MessagesQueue();
}
