package Server.Messages;

import Server.Server;

/**
 * Created by piotr on 13.04.2017.
 */
public class Signal extends Message{
    /**
     * Signal constructor with additional message attached to the signal.
     *
     * @param sender who sent the signal
     * @param receiver to whom send the signal
     * @param signal signal type. Choose one value from enum SignalType.
     * @param object object attached to the signal.
     */
    public Signal (String sender, String receiver, SignalType signal,Object object){

        super(sender, receiver, (object instanceof String) ? (String) object : null);
        signalType = signal;
        this.object = object instanceof String ? null : object;
    }


    /**
     * Signal constructor without additional message.
     *
     * @param sender who sent the signal
     * @param receiver to whom send the signal
     * @param signal singal type. Choose one value from enum SignalType
     */
    public Signal (String sender, String receiver, SignalType signal){

        super(sender, receiver, null);
        signalType = signal;
        object = null;

    }

    /**
     * Accepts visitor which dispatches the signal.
     * @param dispatcher object that is responsible for invoking the appropriate dispatching method
     */
//    public void acceptADispatcher(Server.Server.Dispatcher dispatcher){
//        dispatcher.dispatch(this);
//    }

    /**
     * Attribute that specifies type of the signal.
     */
    public final SignalType signalType;
    public final Object object;

}
