package Interfaces;

import Messages.Message;

/**
 * Interface that ensures that the class can receive a message.
 *
 * Created by piotr on 13.04.2017.
 * @version 1.0
 * @author piotr
 */
public interface Receiver {


    void send(Message message) throws InterruptedException;

}
