package Client;


import Server.Messages.*;
import Server.Messages.Message;
import com.sun.corba.se.impl.protocol.giopmsgheaders.*;

import java.io.*;
import java.lang.String;
import java.net.Socket;

/**
 * Created by piotr on 27.04.2017.
 */
public class Client {

    public static void main(String [] args){


        Integer port = ((args.length != 0) ? Integer.parseInt(args[0]) : 65256);

        try (
                Socket socket = new Socket("localhost", port);
                //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                )
        {
            Thread inputThread =  new Thread (new InputThread(socket));
            inputThread.start();

            while (true){

                String text = stdIn.readLine();
                outputStream.writeObject(new Message("__client1__", "__server__", text));
                if (text.equals("exit")) break;
            }

            inputThread.interrupt();
            inputThread.join();

        }
        catch (IOException e){
            System.err.println(e);
            System.exit(1);
        }
        catch (InterruptedException e){
            System.err.println(e);
            System.exit(1);
        }
    }


    static class InputThread implements Runnable {
        public InputThread(Socket socket){
            this.socket = socket;
        }

        public void run(){

            try (
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                            )
            {

                while (true){

                    Message message = Message.class.cast(in.readObject());
                    System.out.println(message.toString());
                    if (Thread.interrupted()) break;
                }



            }
            catch(Exception e){
                System.err.println(e);
                System.exit(1);
            }

            System.err.println("Exited");

        }

        private Socket socket;
    }



    private static Client instance = new Client();
    private MessagesQueue messages = new MessagesQueue();

}
