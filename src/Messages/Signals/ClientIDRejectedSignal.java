package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;

/**
 * Created by piotr on 20.05.2017.
 */
public class ClientIDRejectedSignal extends Message {

    public ClientIDRejectedSignal (String sender, String receiver){
        super(sender, receiver);
    }

    public void acceptDispatcher (Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }
}
