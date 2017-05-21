package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;

/**
 * Signal is being sent to the all the clients when a client disconnects and is no longer available.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
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
