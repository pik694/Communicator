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

                //TODO: remove printing messages
                System.out.println(message.toString());

                message.acceptDispatcher(dispatcher);


            }
            catch (InterruptedException e){
                //TODO
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

        //TODO: Handle unneeded signals
        public void dispatch(NewClientSignal signal){}

        public void dispatch(RemoveClientSignal signal){}

        public void dispatch(ClientIDSignal signal) {}

        public void dispatch(ClientIDAcceptedSignal signal){}

        public void dispatch(ClientIDRejectedSignal signal){}

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
            //TODO: signal the problem
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

            //TODO: message dispatcher
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

                throw new InvalidParameterException("Such a client does not exist");
            }
            catch (Exception e){
                //TODO
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
