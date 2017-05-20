package Server.Client;


import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signals.ClientThreadsFinishedSignal;
import Messages.Signals.NewClientSignal;
import Server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by piotr on 13.04.2017.
 */

//TODO: Write Client class using  NIO package
public class Client implements Receiver {



    public Client(String clientID, Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException {

        this.clientID = clientID;
        this.socket = socket;
        this.outputStream_ = outputStream;
        this.inputStream_ = inputStream;

        this.inClient = new InClient();
        this.outClient = new OutClient();


        this.in = new Thread(outClient);
        this.out = new Thread(inClient);

    }


    public void start(){
        in.start();
        out.start();
    }

    public void closeClient () throws IOException, InterruptedException{
        socket.close();
        in.join();
        out.join();
    }

    private synchronized void close(){

        try {

            Thread oppositeThread = Thread.currentThread() == in ? out : in;

            oppositeThread.interrupt();

            socket.close();
            inputStream_.close();
            outputStream_.close();

            oppositeThread.join();

            Server.instance.send(new ClientThreadsFinishedSignal(clientID));

        }
        catch (Exception e){
            System.err.println("Unexpected exception : " + e);
            System.exit(1);
        }

    }

    public void send(Message message)throws InterruptedException{
        outClient.messages.addMessage(message);
    }

    public String getClientID(){
        return clientID;
    }


    private class InClient implements Runnable{

        public void run(){

            try{
                while (true) {

                    Message message = Message.class.cast(inputStream_.readObject());
                    Server.instance.send(message);

                }
            }

            catch (Exception e){
                boolean shallIClose = false;

                synchronized (mutex){
                    if (!exiting) {
                        shallIClose = true;
                        exiting = true;
                    }
                }

                if (shallIClose){
                    close();
                }
            }

        }

    }
    private class OutClient implements Runnable{


        public void run(){

            try
            {
                while (true){
                    Message message = messages.getMessage();
                    outputStream_.writeObject(message);
                }
            }

            catch (Exception e){

                boolean shallIClose = false;

                synchronized (mutex){
                    if (!exiting) {
                        shallIClose = true;
                        exiting = true;
                    }
                }

                if (shallIClose){
                    close();
                }


            }

        }
        public MessagesQueue messages = new MessagesQueue();
    }

    private final ObjectInputStream inputStream_;
    private final ObjectOutputStream outputStream_;
    private final Socket socket;
    private final String clientID;
    private final OutClient outClient;
    private final InClient inClient;
    private final Thread in;
    private final Thread out;

    private boolean exiting = false;
    private Object mutex = new Object();
}
