package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;
import Server.Server;

/**
 * Singal is being sent to the server when connection establisher finishes.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class EstablisherThreadFinishedSignal extends Message {

    public EstablisherThreadFinishedSignal(String sender){
        super(sender, Server.instance.name);
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }
}
