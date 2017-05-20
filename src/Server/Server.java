package Server;


import Interfaces.Dispatcher;
import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signals.*;
import Messages.TextMessage;
import Server.Client.Client;
import Server.Deamons.ConnectionEstablisher;

import java.security.InvalidParameterException;
import java.util.Vector;



/**
 * Created by piotr on 27.04.2017.
 */
public class Server implements Receiver{


    public static void main (String [] args){

        instance.init();
        instance.run();

    }


    public boolean validateClientID(String clientID) {
        for (Client client : clients_){
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

    private void close(){

        connectionEstablisher.interrupt();

        try{
            connectionEstablisher.join();
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
            clients_.remove(signal.getSender());
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


    public static final Server instance = new Server();

    private Server(){}


    public final String name = "__server__";
    private Vector<Client> clients_ = new Vector<>();
    private ConnectionEstablisher connectionEstablisher = null;
    private MessagesQueue messages = new MessagesQueue();
}
