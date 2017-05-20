package Client.Model;


import Client.Controllers.MainController;
import Interfaces.Dispatcher;
import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signals.*;
import Messages.TextMessage;
import Server.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Created by piotr on 17.05.2017.
 */
public final class Model implements Receiver {

    public void send (Message message) throws InterruptedException{
        messagesQueue_.addMessage(message);
    }

    public boolean setServer (String address, int port){

        Socket temporarySocket;

        try{
            temporarySocket = new Socket(address, port);
        }
        catch (IOException e){
            return false;
        }

        if (socket_ != null) disconnect();
        socket_ = temporarySocket;

        try {
            inputStream_ = new ObjectInputStream(socket_.getInputStream());
            outputStream_ = new ObjectOutputStream(socket_.getOutputStream());
        }
        catch (IOException e){
            System.err.println("An unexpected error occurred : " + e);
            System.err.println("Process will terminate.");
            System.exit(1);
        }

        return true;
    }

    public boolean setClientId (String clientID){

        if (socket_ != null && clientID_ != null){
            throw new UnsupportedOperationException("Could not change clientID while connected to a server:\n"
                    + "Socket: " + socket_.toString()
                    + "\nclientID: " + clientID_);
        }

        Message answer;

        try {

            outputStream_.writeObject(new ClientIDSignal(clientID, Server.instance.name, clientID));
            answer = Message.class.cast(inputStream_.readObject());

        }
        catch (IOException e){
            throw new NotImplementedException();
        }
        catch (ClassNotFoundException e){
            throw new NotImplementedException();
        }

        if (answer instanceof  ClientIDAcceptedSignal){

            clientID_ = clientID;

            inputThread_.start();
            outputThread_.start();


            return true;
        }

        return false;
    }

    public String getClientID() {
        return clientID_;
    }

    public ObservableList<String> getOtherClients() {
        return otherClients_;
    }

    public InetAddress getServerAddress(){
        return socket_ == null ? null : socket_.getInetAddress();
    }
    public Integer getServerPortNumber(){
        return socket_ == null ? null : socket_.getPort();
    }

    public void disconnect(){

        if (socket_ == null) return;

        purposeDisconnection_ = true;

        try {

            inputThread_.interrupt();
            outputThread_.interrupt();

            socket_.close();

            outputThread_.join();
            inputThread_.join();

            socket_ = null;
            inputStream_ = null;
            outputStream_ = null;

        }
        catch (Exception e){
            System.err.println(e);
            throw new NotImplementedException();
        }

        purposeDisconnection_ = false;
    }

    public static Model instance = new Model();


    private Model(){

    }

    private class InputThread implements Runnable {
        public void run (){

            MessageDispatcher dispatcher = new MessageDispatcher();

            try{
                while(true){

                    Message message = Message.class.cast(inputStream_.readObject());
                    message.acceptDispatcher(dispatcher);

                }
            }
            catch (Exception e){
                if (!purposeDisconnection_){
                    System.err.println("Unexpected exception: " + e);
                    System.exit(1);
                }
            }
        }

        private class MessageDispatcher implements Dispatcher {

            public void dispatch(NewClientSignal signal){
                otherClients_.add(signal.getClientID());
            }
            public void dispatch(RemoveClientSignal signal){
                otherClients_.remove(signal.getClientID());
            }
            public void dispatch(ClientIDSignal signal){
                throw new NotImplementedException();
            }
            public void dispatch(ClientIDAcceptedSignal signal){
                throw new NotImplementedException();
            }
            public void dispatch(ClientIDRejectedSignal signal){
                throw new NotImplementedException();
            }

            public void dispatch(ClientThreadsFinishedSignal signal){
                throw new NotImplementedException();
            }
            public void dispatch(EstablisherThreadFinishedSignal signal){
                throw new NotImplementedException();
            }
            public void dispatch(ClientConnectedSignal signal){
                throw new NotImplementedException();
            }
            public void dispatch(CloseServerSignal signal){
                throw new NotImplementedException();
            }

            public void dispatch(TextMessage textMessage){
                try {
                    MainController.instance.send(textMessage);
                }
                catch (InterruptedException e){
                    throw new NotImplementedException();
                }
            }
        }


    }
    private class OutputThread implements Runnable{

        public void run (){
            try {
                while (true) {
                    Message message = messagesQueue_.getMessage();
                    outputStream_.writeObject(message);
                }
            }
            catch (Exception e){
                if (!purposeDisconnection_){
                    System.err.println("Unexpected exception: " + e);
                    System.exit(1);
                }
            }

        }
    }

    private final Thread inputThread_ = new Thread(new InputThread(), "inputThread");
    private final Thread outputThread_ = new Thread(new OutputThread(), "outputThread");
    private final MessagesQueue messagesQueue_ =  new MessagesQueue();
    private final ObservableList<String> otherClients_ = FXCollections.observableList(new ArrayList<String>());

    private Socket socket_;
    private ObjectInputStream inputStream_;
    private ObjectOutputStream outputStream_;
    private String clientID_;


    private boolean purposeDisconnection_ = false;

}
