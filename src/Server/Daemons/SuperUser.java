package Server.Daemons;

import Messages.Signals.CloseServerSignal;
import Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Daemon responsible for committing commands to the server. <br>
 * Accepts commands : <br>
 * - clients - writes currently connected clients to standard output <br>
 * - close - closes server <br>
 * Created by piotr on 21.05.2017.
 * @author piotr
 * @version 1.0
 */
public class SuperUser implements Runnable{


    /**
     * Main loop
     */
    public void run(){
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in))
                )
        {
            while (true){

                if (Thread.currentThread().isInterrupted()) break;

                String command = input.readLine();

                switch (command){
                    case "clients":
                        getClients();
                        break;
                    case "close":
                        close();
                        break;
                    default:
                        System.out.println("Accepted commands : clients, close");
                        break;
                }
            }
        }
        catch (IOException e){
            System.err.println("Unexpected exception: " + e);
            System.exit(3);
        }

    }

    /**
     * Default constructor. Starts wrapped thread.
     */
    public SuperUser (){
        thread_ = new Thread(this);
        thread_.start();
    }


    /**
     * Waits for the thread to finish
     * @throws InterruptedException thrown while thread is interrupted during wait.
     */
    public void join() throws InterruptedException{
        thread_.join();
    }

    private void getClients(){

        Vector <String> clients = Server.instance.getClients();

        for (String client : clients){
            System.out.println(client);
        }

    }

    private void close() {

        try {
            Server.instance.send(new CloseServerSignal(name));
        }
        catch (InterruptedException e){
            System.err.println("Unexpected exception: " + e);
            System.exit(3);
        }

        thread_.interrupt();

    }

    private final String name = "__root_";
    private final Thread thread_;
}
