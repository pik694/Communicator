package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;
import java.lang.String;

/**
 * Created by piotr on 20.05.2017.
 */
public class ClientIDSignal extends Message{

     public ClientIDSignal (String sender, String receiver, String clientID){
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
