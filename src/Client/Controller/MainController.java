package Client.Controller;

import Interfaces.Receiver;
import Messages.Message;
import Messages.MessagesQueue;

/**
 * Created by piotr on 18.05.2017.
 */
public class MainController implements Receiver {

    public void send (Message message) throws InterruptedException {

        System.out.println(message.sender + " says: " + message.message);

    }


    public  static final MainController instance = new MainController();
    private MainController() {
    }


    private final MessagesQueue messagesQueue_ = new MessagesQueue();
}
