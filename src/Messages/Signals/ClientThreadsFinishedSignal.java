package Messages.Signals;

import java.lang.String;

import Interfaces.Dispatcher;
import Messages.Message;
import Server.Server;

/**
 * Created by piotr on 20.05.2017.
 */
public class ClientThreadsFinishedSignal extends Message{

    public ClientThreadsFinishedSignal (String sender){
        super(sender, Server.instance.name);
    }

    public void  acceptDispatcher (Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

}
