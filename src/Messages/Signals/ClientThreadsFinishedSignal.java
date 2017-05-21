package Messages.Signals;

import java.lang.String;

import Interfaces.Dispatcher;
import Messages.Message;
import Server.Server;

/**
 * Informs the server main thread that client has exited and the server should remove the client from clients' list.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class ClientThreadsFinishedSignal extends Message{

    public ClientThreadsFinishedSignal (String sender){
        super(sender, Server.instance.name);
    }

    public void  acceptDispatcher (Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

}
