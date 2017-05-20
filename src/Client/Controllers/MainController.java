package Client.Controllers;

import Client.Model.Model;
import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.TextMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by piotr on 18.05.2017.
 */

//TODO: singleton
public class MainController implements Receiver, Initializable {

    public void send (Message message) throws InterruptedException {


    }
    public void send (TextMessage message) throws InterruptedException {
        System.out.println(message.getSender() + " says: " + message.getMessage());
    }




    public void initialize(URL url, ResourceBundle resourceBundle){

        otherClientsListView.setItems(Model.instance.getOtherClients());

    }


    @FXML
    private void selectedUser(){

        //TODO
        System.out.println(otherClientsListView.getSelectionModel().getSelectedItem());
    }



    //TODO: remove
    @FXML
    private void newItemButtonClicked(){
        otherClientsListView.getItems().add("new");
    }

    @FXML
    private void removeItemButtonClicked(){
        otherClientsListView.getItems().remove("new");
    }



    public  static final MainController instance = new MainController();

    public  MainController() {

    }

    @FXML
    private ListView<String> otherClientsListView;




}
