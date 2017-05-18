package Client;


import Client.Model.Model;
import Messages.Message;
import Messages.MessagesQueue;
import Messages.Signal;
import Messages.SignalType;
import Server.Server;

import javax.jnlp.DownloadService;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by piotr on 27.04.2017.
 */
public class Client {

    public static void main(String [] args){

        try (
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                )
        {

            Model.instance.setServer("localhost", 65256);

            String name;


            do{
                name = stdIn.readLine();
            } while (Model.instance.setClientId(name) == false);

            String another = stdIn.readLine();

            while (true){

                String  text = stdIn.readLine();

                Model.instance.send(new Message(name, another, text));


                if (text.equals("exit")) {
                    Model.instance.disconnect();
                    System.exit(0);
                }
            }


        }
        catch (Exception e){
            System.err.println(e);
            System.exit(1);
        }

    }




}
