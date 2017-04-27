package Server;


import Server.Messages.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by piotr on 27.04.2017.
 */
public class Server {


    public static void main (String [] args){

        try(
                ServerSocket serverSocket = new ServerSocket(65256);
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutput out = new ObjectOutputStream(socket.getOutputStream());
        )

        {
            while (true){
                Message message = Message.class.cast(in.readObject());

                System.out.println(message.toString());

                out.writeObject(message);

                if (message.message.equals("exit")) break;
            }
        }
        catch(Exception e){
            System.err.println(e);
        }

    }





}
