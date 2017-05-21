package Client.Controllers;

import Client.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible from connecting to the server.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class OptionsFormController implements Initializable{


    /**
     * Sets up the view
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle){

        serverAddressTextField.setText("localhost");
        portNumberTextField.setText("65256");


    }

    /**
     * Handles acceptButton pushed event. Validates data and tries to connect to given server. If successfully connected then closes dialog, if not - displays adequate message.
     */
    public void clickedAcceptButton(){



        Integer port;
        try{
            port = Integer.parseInt(portNumberTextField.getText());
        }
        catch (Exception e){
            port = null;
        }



        if (serverAddressTextField.getText().isEmpty()
                || portNumberTextField.getText().isEmpty()
                || clientIdTextField.getText().isEmpty())
        {
            warningText.setText("Error. At least one of the fields remains empty.");
        }
        else if (port == null){
            warningText.setText("Error. Invalid port number.");
        }
        else if (Model.instance.setServer(serverAddressTextField.getText(), port)
                && Model.instance.setClientId(clientIdTextField.getText()))
        {
            //Ugly cast but works.
            ((Stage) warningText.getScene().getWindow()).close();
        }
        else {
            warningText.setText("Error. At least one of the values is invalid.");
        }

        warningText.setVisible(true);
    }

    /**
     * Handles exitButton pushed event. Closes application.
     */
    @FXML
    protected void clickedExitButton(){
        System.exit(0);
    }


    @FXML
    protected TextField serverAddressTextField;
    @FXML
    protected TextField portNumberTextField;
    @FXML
    protected TextField clientIdTextField;
    @FXML
    protected Label warningText;

}
