package Messages.Signals;


import Interfaces.Dispatcher;
import Messages.Message;

import java.lang.String;

/**
 * Created by piotr on 13.04.2017.
 */
public class NewClientSignal extends Message {

    public NewClientSignal (String sender, String receiver, String clientID) throws NullPointerException {
        super(sender, receiver);
        clientID_ = clientID;
    }

    public String getClientID() {
        return clientID_;
    }

    public void acceptDispatcher (Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

    private final String clientID_;
}
