package Server.Messages;


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
    public synchronized void addMessage(Message message) {
        queue.add(message);
    }

    /**
     * Returns a valid message and removes it from the collection.
     *
     * @return returns a valid message
     * @throws NullPointerException throws exception in case there is not any message to be read
     */
    public synchronized Message getMessage() throws NullPointerException {
        if (queue.isEmpty()){
            throw new NullPointerException ("Queue is empty");
        }

        Message message = (Message) queue.firstElement();
        queue.remove(message);

        return message;
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    private Vector<Message> queue = new Vector<>(256);
}
