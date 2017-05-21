package Client.Controllers;

import Client.Model.Model;
import Interfaces.Receiver;
import Messages.Message;
import Messages.TextMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Main controller. A singleton.
 * Controls main app window and is a proxy between chats controllers and Model.
 * Created by piotr on 18.05.2017.
 * @version 1.0
 * @author piotr
 */

public class MainController implements Receiver, Initializable {

    /**
     * @deprecated
     * @param message
     * @throws InterruptedException
     */
    public void send (Message message) throws InterruptedException {

    }

    /**
     * Receives message from model and sends it further to the proper chat controller.
     * @param message text message being received from model.
     * @throws InterruptedException
     */
    public void send (TextMessage message) throws InterruptedException{

        for (CommunicatorFormController chat : openedChats_){
            if (chat.getUser().equals(message.getSender())){
                chat.sendMessage(message);
                chat.showWindow();
                return;
            }
        }

        CommunicatorFormController newChat = new CommunicatorFormController(message.getSender());

        newChat.sendMessage(message);

        openedChats_.add(newChat);

    }


    /**
     * Sets up the view.
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle){

        otherClientsListView.setItems(Model.instance.getOtherClients());

    }

    /**
     * Handles event of selection of a user to talk to. Opens new chat or does nothing if the proper chat has already been opened.
     * @throws IOException
     */
    @FXML
    protected void selectedUser() throws IOException{

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


    /**
     * The only instance - singleton.
     */
    public  static final MainController instance = new MainController();

    private  MainController() {

    }

    @FXML
    protected ListView<String> otherClientsListView;


    private Vector<CommunicatorFormController> openedChats_ = new Vector<>();


}
