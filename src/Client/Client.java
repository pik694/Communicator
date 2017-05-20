package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import Client.Model.Model;


/**
 * Created by piotr on 27.04.2017.
 */
public class Client extends Application {

    public static void main(String [] args){

//        try (
//                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//                )
//        {
//
//            Model.instance.setServer("localhost", 65256);
//
//            String name;
//
//
//            do{
//                name = stdIn.readLine();
//            } while (Model.instance.setClientId(name) == false);
//
//
//
////            String another = stdIn.readLine();
////
////            while (true){
////
////                String  text = stdIn.readLine();
////
////                Model.instance.send(new Message(name, another, text));
////
////
////                if (text.equals("exit")) {
////                    Model.instance.disconnect();
////                    System.exit(0);
////                }
////            }
//
//
//        }
//        catch (Exception e){
//            System.err.println(e);
//            System.exit(1);
//        }


        //Model.instance.setServer("localhost", 65256);
        //Model.instance.setClientId("żaba");

        launch(args);

        Model.instance.disconnect();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Communicator - Client Side");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/MainView.fxml"));

        //fxmlLoader.setController(MainController.instance);

        Parent root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        Stage serverStage = new Stage();
        serverStage.setTitle("Select the server");

        Parent options = FXMLLoader.load(getClass().getResource("Views/OptionsForm.fxml"));

        serverStage.setScene(new Scene(options));

        serverStage.initOwner(primaryStage);
        serverStage.initModality(Modality.WINDOW_MODAL);

        serverStage.showAndWait();


    }



}
