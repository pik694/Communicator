package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;
import java.lang.String;

/**
 * Signal is being sent from client to the server to approve chosen ID.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
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
