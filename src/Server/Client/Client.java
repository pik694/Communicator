package Server.Client;


import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signal;
import Messages.SignalType;
import Server.Interfaces.Receiver;
import Server.Server;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.Socket;

/**
 * Created by piotr on 13.04.2017.
 */

//TODO: Write Client class using  NIO package
public class Client implements Receiver {



    public Client(String clientID, Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) throws IOException {

        this.clientID = clientID;
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;

        this.inClient = new InClient();
        this.outClient = new OutClient();


        this.in = new Thread(outClient);
        this.out = new Thread(inClient);

    }


    public void start(){
        in.start();
        out.start();
    }

    public void close() throws InterruptedException, IOException{

        //TODO: handle exceptions
        //TODO: make sure that it works and does not block

        in.interrupt();
        out.interrupt();

        in.join();
        out.join();

        socket.close();


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

                    Message message = Message.class.cast(inputStream.readObject());
                    Server.instance.send(message);

                    if (Thread.interrupted()) break;
                }
                close();
            }
            catch (Exception e){
                e.printStackTrace();
                System.err.println(e);
                System.exit(1);
            }

        }
        private void close() throws IOException, InterruptedException{
            Server.instance.send(new Signal(clientID, Server.instance.name, SignalType.threadFinished));
        }

    }
    private class OutClient implements Runnable{

        public OutClient (){
        }


        public void run(){

            try
            {
                while (true){
                    Message message = messages.getMessage();
                    outputStream.writeObject(message);
                }
            }
            catch (IOException e){
                //TODO: handle IOException and try to catch InterruptedException
                System.err.println(e.getMessage());
                throw new NotImplementedException();
            }
            catch (InterruptedException e){
                //TODO: handle IOException and try to catch InterruptedException
                System.err.println(e.getMessage());
                throw new NotImplementedException();
            }

        }
        public MessagesQueue messages = new MessagesQueue();
    }

    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Socket socket;
    private final String clientID;
    private final OutClient outClient;
    private final InClient inClient;
    private final Thread in;
    private final Thread out;
}
