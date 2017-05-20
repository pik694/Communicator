package Messages;

import Interfaces.Dispatcher;

import java.lang.String;

/**
 * Created by piotr on 20.05.2017.
 */
public class TextMessage extends Message {

    public TextMessage (String sender, String receiver, String message) throws NullPointerException{
        super(sender, receiver);

        if (message == null) throw new NullPointerException("Null value is invalid");

        this.message_ = message;
    }

    public String getMessage() {
        return message_;
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

    private final String message_;
}
