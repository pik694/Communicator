package Client.Controllers;

import Client.Model.Model;
import Interfaces.Receiver;
import Messages.Message;
import Messages.TextMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

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

        otherClientsListView.setItems(otherClients_);

    }


    @FXML
    private void selectedUser() throws IOException{

        String selectedUser = otherClientsListView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) return;

        for (CommunicatorFormController chat : openedChats_){
            if (chat.getUser().equals(selectedUser)){

                chat.showWindow();

                return;
            }
        }

        openedChats_.add(new CommunicatorFormController(selectedUser));

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

    private  MainController() {

    }

    @FXML
    private ListView<String> otherClientsListView;
    private final ObservableList<String> otherClients_ = FXCollections.observableList(new ArrayList<String>());

    private Vector<CommunicatorFormController> openedChats_ = new Vector<>();


}
