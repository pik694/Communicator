package Messages;

/**
 * Created by piotr on 13.04.2017.
 */

//TODO: change into a hierarchy of classes
public enum SignalType {
    CLIENT_ID,
    CLIENT_ID_ACCEPTED,
    CLIENT_ID_REJECTED,
    CLIENT_THREADS_FINISHED,
    ESTABLISHER_THREAD_FINISHED,
    CLIENT_CONNECTED,
    CLOSE_SERVER,
    REMOVE_CLIENT,
    ADD_CLIENT
}
