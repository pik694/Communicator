package Messages;

import java.io.Serializable;
import Interfaces.Dispatcher;

/**
 * Created by piotr on 13.04.2017.
 */
public class Message implements Serializable{

    /**
     * Message constructor.
     *
     * @param sender who sent the message
     * @param receiver to whom send the message
     * @param message actual message
     * @throws NullPointerException throws whenever sender or receiver parameter equals to null.
     */
    public Message (String sender, String receiver, String message) throws NullPointerException{

        if (sender == null || receiver == null){
            throw  new NullPointerException("Null value is invalid");
        }

        this.receiver = receiver;
        this.sender = sender;
        this.message = message;

    }

    public String toString(){
        return sender + " " + receiver + " " + message;
    }


    /**
     * Accepts visitor which dispatches the message.
     * @param dispatcher object that is responsible for invoking the appropriate dispatching method
     */
    public void acceptADispatcher(Dispatcher dispatcher) throws InterruptedException{
        dispatcher.dispatch(this);
    }

    /**
     * Attribute that specifies who sent the message.
     */
    public final String sender;
    /**
     * Attribute that specifies to whom message should be sent.
     */
    public final String receiver;
    /**
     * Actual message.
     */
    public final String message;

}
