package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;

/**
 * Signal informs that given ID has been approved and now the client can use it as its own.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class ClientIDAcceptedSignal extends Message{

    public ClientIDAcceptedSignal(String sender, String receiver){
        super(sender, receiver);
    }

    public void acceptDispatcher (Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

}
