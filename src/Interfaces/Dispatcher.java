package Interfaces;

import Messages.Signals.*;
import Messages.TextMessage;


/**
 * Created by piotr on 17.05.2017.
 */
public interface Dispatcher {

    void dispatch(NewClientSignal signal);
    void dispatch(RemoveClientSignal signal);
    void dispatch(ClientIDSignal signal);
    void dispatch(ClientIDAcceptedSignal signal);
    void dispatch(ClientIDRejectedSignal signal);

    void dispatch(ClientThreadsFinishedSignal signal);
    void dispatch(EstablisherThreadFinishedSignal signal);
    void dispatch(ClientConnectedSignal signal);
    void dispatch(CloseServerSignal signal);

    void dispatch(TextMessage textMessage);


}
