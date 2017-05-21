package Messages;

import java.util.Vector;

/** Messages Queue is a keeps a collection of messages and signals.
 *  Messages are returned in FIFO order.
 * Created by piotr on 13.04.2017.
 * @version 1.0
 * @author piotr
 */
public class MessagesQueue  {


    /**
     * Adds a message to the queue in a synchronized way.
     * @param message will be added to the queue
     * @throws InterruptedException
     */
    public synchronized void addMessage(Message message) throws InterruptedException{

        while (queue.size() == MAX_QUEUE_SIZE) wait();

        queue.add(message);

        notify();


    }

    /**
     * Get message from queue in a FIFO order. Returned message will be removed from the queue.
     * @return next message
     * @throws InterruptedException
     */
    public synchronized Message getMessage() throws InterruptedException {


        while (isEmpty()) wait();

        Message message = queue.firstElement();
        queue.remove(message);

        notify();

        return message;

    }

    /**
     * Checks if there are any messages left.
     * @return true if messages queue is empty
     */
    public synchronized boolean isEmpty(){
        return queue.isEmpty();
    }


    static final int MAX_QUEUE_SIZE = 1024;
    private Vector<Message> queue = new Vector<>(MAX_QUEUE_SIZE);

}
