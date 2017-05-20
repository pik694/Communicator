package Messages;

import java.util.Vector;

/** Messages Queue is a keeps a collection of messages and signals.
 *  Messages are returned in FIFO order.
 * Created by piotr on 13.04.2017.
 */
public class MessagesQueue  {


    public synchronized void addMessage(Message message) throws InterruptedException{

        while (queue.size() == MAX_QUEUE_SIZE) wait();

        queue.add(message);

        notify();


    }
    public synchronized Message getMessage() throws InterruptedException {


        while (isEmpty()) wait();

        Message message = queue.firstElement();
        queue.remove(message);

        notify();

        return message;

    }

    public synchronized boolean isEmpty(){
        return queue.isEmpty();
    }


    static final int MAX_QUEUE_SIZE = 1024;
    private Vector<Message> queue = new Vector<>(MAX_QUEUE_SIZE);

}
