package Client;

import Client.Controllers.MainController;
import Client.Model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Main client's side class.
 * Created by piotr on 27.04.2017.
 * @version 1.0
 * @author piotr
 */
public class Client extends Application {

    /**
     * Where client's process starts
     * @param args
     */
    public static void main(String [] args){
        launch(args);
    }

    /**
     * Here primary stage is being set and dialog form responsible for connecting to the server is being shown.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Communicator - Client Side");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/MainView.fxml"));

        fxmlLoader.setController(MainController.instance);

        Parent root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest((windowEvent)->{
            Model.instance.disconnect();
            System.exit(0);
        });


        primaryStage.setMinHeight(200);
        primaryStage.setMinWidth(150);

        primaryStage.show();


        Stage serverStage = new Stage();
        serverStage.setOnCloseRequest((windowEvent)-> {
            if (Model.instance.getClientID() == null) windowEvent.consume();
        });

        serverStage.setTitle("Select the server");

        Parent options = FXMLLoader.load(getClass().getResource("Views/OptionsForm.fxml"));

        serverStage.setScene(new Scene(options));
        serverStage.setResizable(false);

        serverStage.initOwner(primaryStage);
        serverStage.initModality(Modality.WINDOW_MODAL);

        serverStage.showAndWait();

        primaryStage.setTitle("User:" + Model.instance.getClientID());

    }



}
