package Messages;

import java.io.Serializable;
import Interfaces.Dispatcher;


/**
 * Abstract base class to all the messages being used to communication between either clients or server threads.
 * Created by piotr on 13.04.2017.
 * @version 1.0
 * @author piotr
 */
public abstract class Message implements Serializable{

    /**
     * Default constructor
     * @param sender who has sent this message
     * @param receiver to whom should this message get
     * @throws NullPointerException
     */
    public Message (String sender, String receiver) throws NullPointerException{

        if (sender == null || receiver == null){
            throw  new NullPointerException("Null value is invalid");
        }

        this.receiver_ = receiver;
        this.sender_ = sender;

    }

    public void acceptDispatcher(Dispatcher dispatcher){
        throw new RuntimeException("RTTI error");
    }


    public final String getReceiver() {
        return receiver_;
    }

    public final String getSender() {
        return sender_;
    }

    /**
     * Attribute that specifies who sent the message.
     */
    private final String sender_;
    /**
     * Attribute that specifies to whom message should be sent.
     */
    private final String receiver_;


}
