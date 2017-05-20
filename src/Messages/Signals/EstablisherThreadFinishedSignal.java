package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;
import Server.Server;

/**
 * Created by piotr on 20.05.2017.
 */
public class EstablisherThreadFinishedSignal extends Message {

    public EstablisherThreadFinishedSignal(String sender){
        super(sender, Server.instance.name);
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }
}