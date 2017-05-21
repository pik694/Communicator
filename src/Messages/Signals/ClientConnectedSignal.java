package Messages.Signals;

import Interfaces.Dispatcher;
import Server.Client.Client;
import Messages.Message;
import Server.Server;

/**
 * Signal that informs Server's main thread about newly connected client.
 * Created by piotr on 20.05.2017.
 * @version 1.0
 * @author piotr
 */
public class ClientConnectedSignal extends Message{

    /**
     * Default constructor. Signal is always sent to the server.
     * @param sender should always be ConnectionEstablisher
     * @param client client that has just connected
     */
    public ClientConnectedSignal(String sender, Client client){
        super(sender, Server.instance.name);
        client_ = client;
    }

    /**
     * Returns client with whom this signal is connected.
     * @return valid client
     */
    public Client getClient() {
        return client_;
    }

    public void acceptDispatcher(Dispatcher dispatcher){
        dispatcher.dispatch(this);
    }

    private final Client client_;
}
