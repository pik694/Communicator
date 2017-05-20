package Client.Controller;

import Client.Model.Model;
import Interfaces.Dispatcher;
import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signals.*;
import Messages.TextMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by piotr on 18.05.2017.
 */
public class MainController implements Receiver, Initializable {

    public void send (Message message) throws InterruptedException {

        messagesQueue_.

    }
    public void send (TextMessage message) throws InterruptedException {
        System.out.println(message.getSender() + " says: " + message.getMessage());
    }

    public void initialize(URL url, ResourceBundle resourceBundle){

        otherClientsListView.setItems(otherClients_);
    }


    public  static final MainController instance = new MainController();

    public  MainController() {
    }


    private final MessagesQueue messagesQueue_ = new MessagesQueue();


    @FXML
    private ListView<String> otherClientsListView;



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


    private final ObservableList<String> otherClients_ = FXCollections.observableList(new ArrayList<String>());


}
