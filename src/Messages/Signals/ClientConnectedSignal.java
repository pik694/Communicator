package Messages.Signals;

import Interfaces.Dispatcher;
import Server.Client.Client;
import Messages.Message;
import Server.Server;

/**
 * Created by piotr on 20.05.2017.
 */
public class ClientConnectedSignal extends Message{

    public ClientConnectedSignal(String sender, Client client){
        super(sender, Server.instance.name);
        client_ = client;
    }

    public Client getClient() {
        return client_;
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

    private final Client client_;
}
