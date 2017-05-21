package Messages.Signals;

import Interfaces.Dispatcher;
import Messages.Message;
import Server.Server;

/**
 * Signel is being sent from SuperUser object to start server closing procedure.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class CloseServerSignal extends Message{

    public CloseServerSignal(String sender){
        super(sender, Server.instance.name);
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

}
