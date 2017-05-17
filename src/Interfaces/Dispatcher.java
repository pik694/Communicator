package Interfaces;

import Messages.Message;
import Messages.Signal;


/**
 * Created by piotr on 17.05.2017.
 */
public interface Dispatcher {

    void dispatch(Message message);
    void dispatch(Signal signal);

}
