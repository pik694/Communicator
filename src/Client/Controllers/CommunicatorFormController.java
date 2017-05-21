package Client.Controllers;



import Client.Model.Model;
import Messages.TextMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by piotr on 20.05.2017.
 */
public class CommunicatorFormController implements Initializable{


    public CommunicatorFormController(String user) throws IOException{


        stage_ = new Stage();
        stage_.setTitle(user);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Views/CommunicatorForm.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        stage_.setScene(new Scene(root));

        stage_.setOnShown((windowEvent) -> stageIsClosed = false);

        stage_.setOnCloseRequest((windowEvent) -> stageIsClosed = true);

        stage_.show();

    }

    public String getUser() {
        return stage_.getTitle();
    }


    public void initialize (URL url, ResourceBundle resourceBundle){
        messagesHistoryListView.setSelectionModel(new DisabledSelectionModel<String>());
    }

    public void showWindow(){

        if (stageIsClosed) stage_.show();
    }

    @FXML
    protected void clickedSendButton (){

        String message = textArea.getText();

        message = "You wrote : " + message;

        textArea.setText(null);

        if (message.isEmpty()) return;

        messagesHistoryListView.getItems().add(message);



        //TODO: start sending messages
//        try{
//            Model.instance.send(new TextMessage(Model.instance.getClientID(), getUser(), message));
//        }
//        catch (InterruptedException e){
//            System.err.println("Unexpected exception: " + e + "\n Program will terminate.");
//            System.exit(2);
//        }

    }

    public void sendMessage(TextMessage message){

        messagesHistoryListView.getItems().add(message.getSender() + " wrote : " + message.getMessage());

    }






    @FXML
    protected ListView<String> messagesHistoryListView;

    @FXML
    protected TextArea textArea;


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
