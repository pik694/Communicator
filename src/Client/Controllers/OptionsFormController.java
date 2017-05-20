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
 * Created by piotr on 20.05.2017.
 */
public class OptionsFormController implements Initializable{

    public void initialize(URL url, ResourceBundle resourceBundle){

        serverAddressTextField.setText("localhost");
        portNumberTextField.setText("65256");


    }


    public void clickedAcceptButton(ActionEvent event){



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
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
        else {
            warningText.setText("Error. At least one of the values is invalid.");
        }

        warningText.setVisible(true);
    }

    @FXML
    protected void clickedCancelButton(ActionEvent event){
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
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
