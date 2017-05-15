package Server;


import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signal;
import Messages.SignalType;
import Server.Client.Client;
import Server.Deamons.ConnectionEstablisher;
import Server.Interfaces.Receiver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


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

                //TODO: remove printing messages
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


    public class Dispatcher{

        /**
         * This method dispatches a signal.
         * @param signal a signal to be dispatched.
         */
        public void dispatch(Signal signal){
            switch (signal.signalType){
                case CLIENT_CONNECTED:
                    try {
                        Client newClient = Client.class.cast(signal.object);

                        for (Client client : clients){
                            client.send(new Signal(name, client.getClientID(), SignalType.ADD_CLIENT, newClient.getClientID()));
                            newClient.send(new Signal(name, newClient.getClientID(), SignalType.ADD_CLIENT, client.getClientID()));
                        }
                        clients.addElement(newClient);

                        newClient.start();
                    }
                    catch (ClassCastException e){
                        System.err.println("invalid client given");
                    }
                    catch (InterruptedException e){
                        System.err.println("Unexpected exception:" + e);
                        System.exit(2);
                    }
                    break;
                case CLIENT_THREADS_FINISHED:
                    clients.remove(signal.object);


                    try {
                        for (Client client : clients) {
                            client.send(new Signal(name, client.getClientID(), SignalType.REMOVE_CLIENT, signal.sender));
                        }
                    }
                    catch (InterruptedException e){
                        System.err.println("Unexpected exception:" + e);
                        System.exit(3);
                    }

                    break;
                case ESTABLISHER_THREAD_FINISHED:
                case CLOSE_SERVER:
                    close();
                    System.exit(0);
                    break;
                default:
                    System.err.println(signal.signalType);
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
