package Messages;

import Interfaces.Dispatcher;

import java.lang.String;

/**
 * Represents a text message send between two clients.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class TextMessage extends Message {

    /**
     * Default constructor
     * @param sender
     * @param receiver
     * @param message
     * @throws NullPointerException
     */
    public TextMessage (String sender, String receiver, String message) throws NullPointerException{
        super(sender, receiver);

        if (message == null) throw new NullPointerException("Null value is invalid");

        this.message_ = message;
    }

    /**
     * Returns text message
     * @return
     */
    public String getMessage() {
        return message_;
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

    private final String message_;
}
