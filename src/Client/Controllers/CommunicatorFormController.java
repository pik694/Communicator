package Client.Controllers;


import Client.Model.Model;
import Messages.TextMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Single chat controller. Manages a chat with one user.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class CommunicatorFormController implements Initializable{


    /**
     * Constructs new window and shows it.
     * @param user the online client with whom the user will chat using this window
     */
    public CommunicatorFormController(String user){


        stage_ = new Stage();
        stage_.setTitle(user);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Views/CommunicatorForm.fxml"));
        fxmlLoader.setController(this);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        }
        catch (IOException e){
            System.err.println(e);
            System.exit(3);
        }

        stage_.setMinHeight(200);
        stage_.setMinWidth(200);

        stage_.setScene(new Scene(root));

        stage_.setOnShown((windowEvent) -> stageIsClosed = false);

        stage_.setOnCloseRequest((windowEvent) -> stageIsClosed = true);

        stage_.show();

    }

    /**
     * Get the online client's ID with whom this chat is connected with.
     * @return client's ID
     */
    public String getUser() {
        return stage_.getTitle();
    }


    /**
     * Set up the view.
     * @param url
     * @param resourceBundle
     */
    public void initialize (URL url, ResourceBundle resourceBundle){
        messagesHistoryListView.setSelectionModel(new DisabledSelectionModel<String>());

        //TODO: cell factory

    }


    /**
     * Shows window in case if it exist but has been closed by the user.
     */
    public void showWindow(){

        if (stageIsClosed) stage_.show();
    }

    /**
     * Handles sending message request.
     */
    @FXML
    protected void clickedSendButton (){

        String message = textField.getText();

        textField.setText("");

        if (message.isEmpty()) return;

        messagesHistoryListView.getItems().add("You wrote : " + message);


        try{
            Model.instance.send(new TextMessage(Model.instance.getClientID(), getUser(), message));
        }
        catch (InterruptedException e){
            System.err.println("Unexpected exception: " + e + "\n Program will terminate.");
            System.exit(2);
        }

    }

    /**
     * Handles pushed key event pushed while being in textField, checks if 'enter' if so tries to send the message.
     * @param event event connected with pushed key, tells which key has been pushed.
     */
    @FXML
    protected void tryToSend(KeyEvent event){
        if (event.getCode() == KeyCode.ENTER){
            clickedSendButton();
        }
    }


    /**
     * Receives a message and displays it on the screen.
     * @param message message to be displayed
     */
    public void sendMessage(TextMessage message){

        messagesHistoryListView.getItems().add(message.getSender() + " wrote : " + message.getMessage());

    }



    @FXML
    protected ListView<String> messagesHistoryListView;

    @FXML
    protected TextField textField;


    private final Stage stage_;
    private boolean stageIsClosed = true;

    protected static class DisabledSelectionModel<T> extends MultipleSelectionModel<T> {
        DisabledSelectionModel() {
            super.setSelectedIndex(-1);
            super.setSelectedItem(null);
        }
        @Override
        public ObservableList<Integer> getSelectedIndices() { return FXCollections.<Integer>emptyObservableList() ; }
        @Override
        public ObservableList<T> getSelectedItems() { return FXCollections.<T>emptyObservableList() ; }
        @Override
        public void selectAll() {}
        @Override
        public void selectFirst() {}
        @Override
        public void selectIndices(int index, int... indicies) {}
        @Override
        public void selectLast() {}
        @Override
        public void clearAndSelect(int index) {}
        @Override
        public void clearSelection() {}
        @Override
        public void clearSelection(int index) {}
        @Override
        public boolean isEmpty() { return true ; }
        @Override
        public boolean isSelected(int index) { return false ; }
        @Override
        public void select(int index) {}
        @Override
        public void select(T item) {}
        @Override
        public void selectNext() {}
        @Override
        public void selectPrevious() {}

    }


}
