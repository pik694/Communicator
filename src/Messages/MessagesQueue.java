package Messages;

import java.util.Vector;

/** Messages Queue is a keeps a collection of messages and signals.
 *  Messages are returned in FIFO order.
 * Created by piotr on 13.04.2017.
 */
public class MessagesQueue  {


    /**
     * Adds message to the collection.
     *
     * @param message is a message or signal that will be added to the queue.
     */
    public synchronized void addMessage(Message message) throws InterruptedException{

        if (queue.size() == MAX_QUEUE_SIZE){
            wait();
        }
        queue.add(message);
        notify();

//        synchronized (lock){
//            lock.notify();
//        }


    }

    /**
     * Returns a valid message and removes it from the collection.
     *
     * @return returns a valid message
    //TODO: fix documentation * @throws NullPointerException throws exception in case there is not any message to be read
     */
    public synchronized Message getMessage() throws InterruptedException {


        if (queue.isEmpty()) wait();

            Message message = queue.firstElement();
            queue.remove(message);
            notify();

            return message;

//        if (queue.isEmpty()){
//            synchronized (lock){
//                lock.wait();
//            }
//        }
//
//        synchronized (this){
//            Message message = queue.firstElement();
//            queue.remove(message);
//
//            return message;
//        }
    }

    public synchronized boolean isEmpty(){
        return queue.isEmpty();
    }


    static final int MAX_QUEUE_SIZE = 1;
    private Vector<Message> queue = new Vector<>(MAX_QUEUE_SIZE);

    private Object lock = new Object();
}
