package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;

/**
 * Signal informs that given ID has not been approved and the client should try another one.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class ClientIDRejectedSignal extends Message {

    public ClientIDRejectedSignal (String sender, String receiver){
        super(sender, receiver);
    }

    public void acceptDispatcher (Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }
}
