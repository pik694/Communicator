package Server;


import Interfaces.Dispatcher;
import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signals.*;
import Messages.TextMessage;
import Server.Client.Client;
import Server.Daemons.ConnectionEstablisher;
import Server.Daemons.SuperUser;

import java.security.InvalidParameterException;
import java.util.Vector;



/**
 * Created by piotr on 27.04.2017.
 *
 * Main server class. Here everything begins and ends.
 *
 * @version 1.0
 * @author piotr
 */
public class Server implements Receiver{


    /**
     * Place where the ball starts rolling
     */

    public static void main (String [] args){
        instance.init();
        instance.run();

    }


    /**
     * Checks if connection establisher can connect new client with such an ID.
     * Checks if client with such an ID is already connected.
     * @param clientID ID to be validated
     * @return false if a client with such an ID is already connected to the server.
     */
    public boolean validateClientID(String clientID) {
        for (Client client : clients_){
            if (client.getClientID().equals(clientID)){
                return false;
            }
        }
        return true;
    }


    /**
     * Gives all clients' IDs.
     * @return Vector of strings where each string is an ID of a client connected to the server.
     */
    public Vector <String> getClients (){

        Vector <String> clients = new Vector<>();

        for (Client client : clients_){
            clients.add(client.getClientID());
        }

        return clients;
    }


    /**
     * Written to conform to Receiver interface
     * @param message message to receive and then dispatch
     * @throws InterruptedException sometimes thread must wait to add message to the queue, so it might be interrupted on wait.
     */
    public void send(Message message) throws InterruptedException{
        messages.addMessage(message);
    }

    /**
     * Initialises daemons threads.
     */
    private void init(){
        connectionEstablisher = new ConnectionEstablisher();
        root = new SuperUser();

    }

    /**
     * Main dispatching loop.
     */
    private void run (){

        MessagesDispatcher dispatcher = new MessagesDispatcher();

        while (true){
            try {

                Message message = messages.getMessage();

                System.out.println(message.toString());

                message.acceptDispatcher(dispatcher);


            }
            catch (InterruptedException e){
                System.err.println("Unexpected exception:" + e);
                System.exit(1);
            }
        }
    }


    /**
     * @brief Cleans up and closes threads.
     * Invoked when server is to close.
     */
    private void close(){

        connectionEstablisher.interrupt();

        try{
            connectionEstablisher.join();
            root.join();

            for (Client client : clients_){
                client.closeClient();
            }
        }
        catch (Exception e){
            System.err.println("An error occurred while closing program");
            System.err.println(e);
            System.exit(1);
        }

        System.out.println("Server will exit");

    }


    /**
     * Class that is responsible for dispatching messages.
     * Written as a visitor. Each method dispatches different type of message.
     */
    public class MessagesDispatcher implements Dispatcher {

        public void dispatch(NewClientSignal signal){
            throw new RuntimeException("This signal should not have gotten here.");
        }

        public void dispatch(RemoveClientSignal signal){
            throw new RuntimeException("This signal should not have gotten here.");
        }

        public void dispatch(ClientIDSignal signal) {
            throw new RuntimeException("This signal should not have gotten here.");
        }

        public void dispatch(ClientIDAcceptedSignal signal){
            throw new RuntimeException("This signal should not have gotten here.");
        }

        public void dispatch(ClientIDRejectedSignal signal){
            throw new RuntimeException("This signal should not have gotten here.");
        }

        public void dispatch(ClientThreadsFinishedSignal signal){

            for (Client client : clients_){
                if (client.getClientID().equals(signal.getSender())){
                    clients_.remove(client);
                    break;
                }
            }

            try {
                for (Client client : clients_) {
                    client.send(new RemoveClientSignal(name, client.getClientID(),signal.getSender()));
                }
            }
            catch (InterruptedException e){
                System.err.println("Unexpected exception:" + e);
                System.exit(3);
            }
        }

        public void dispatch(EstablisherThreadFinishedSignal signal){
            dispatch(new CloseServerSignal(name));
            System.err.println("Connection establisher unexpectedly exited.");
        }

        public void dispatch (CloseServerSignal signal){
            System.out.println("Server is closing");
            close();
            System.out.println("All modules has been closed properly. Server will terminate.");
            System.exit(0);
        }

        public void dispatch (ClientConnectedSignal signal){

            try {
                Client newClient = Client.class.cast(signal.getClient());

                for (Client client : clients_){
                    client.send(new NewClientSignal(name, client.getClientID(), newClient.getClientID()));
                    newClient.send(new NewClientSignal(name, newClient.getClientID(), client.getClientID()));
                }
                clients_.addElement(newClient);

                newClient.start();
            }
            catch (ClassCastException e){
                System.err.println("invalid client given");
            }
            catch (InterruptedException e){
                System.err.println("Unexpected exception:" + e);
                System.exit(2);
            }

        }

        public void dispatch (TextMessage textMessage) {

            try {

                if (textMessage.getReceiver().equals(name)) {

                    System.out.println(textMessage.getMessage());
                    return;
                }

                for (Client client : instance.clients_) {
                    if (textMessage.getReceiver().equals(client.getClientID())) {
                        client.send(textMessage);
                        return;
                    }
                }

                throw new InvalidParameterException("Such a client does not exist : " + textMessage.getReceiver());
            }
            catch (Exception e){
               System.out.println(e);
            }

        }
    }

    /**
     * Server is a singleton
     */
    public static final Server instance = new Server();

    private Server(){}


    public final String name = "__server__";

    private Vector<Client> clients_ = new Vector<>();
    private ConnectionEstablisher connectionEstablisher = null;
    private SuperUser root = null;
    private MessagesQueue messages = new MessagesQueue();
}
