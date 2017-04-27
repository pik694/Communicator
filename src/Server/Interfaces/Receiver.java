package Server.Interfaces;

import Messages.Message;

/**
 * Created by piotr on 13.04.2017.
 */
public interface Receiver {


    void send(Message message) throws InterruptedException;

}
