package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;

/**
 * Created by piotr on 20.05.2017.
 */
public class RemoveClientSignal extends Message {

    public RemoveClientSignal (String sender, String receiver, String clientID) throws NullPointerException {
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
