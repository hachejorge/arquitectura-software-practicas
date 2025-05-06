package MOM;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrokerInterface extends Remote {
    void declare_queue(String name) throws RemoteException;

    void publish(String queueName, Message message) throws RemoteException;

    void consume(String queueName, Consumer consumer) throws RemoteException;

}
