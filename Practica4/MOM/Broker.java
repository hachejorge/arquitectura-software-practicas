package MOM;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Broker extends UnicastRemoteObject implements BrokerInterface {
    private List<Queue> queues;

    public Broker() throws RemoteException {
        super();
        queues = new ArrayList<Queue>();
    }

    private int getQueueIndex(String name) {
        for (int i = 0; i < queues.size(); i++) {
            if (queues.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void declare_queue(String name) throws RemoteException {
        int index = getQueueIndex(name);
        if (index == -1) {
            Queue newQueue = new Queue(name);
            queues.add(newQueue);
        }
    }

    public void publish(String queueName, Message message) throws RemoteException {
        int index = getQueueIndex(queueName);
        if (index != -1) {
            Queue queue = queues.get(index);
            queue.addMessage(message);
        } else {
            System.out.println(queueName + " not found");
        }
    }

    public void consume(String queueName, Consumer callback) throws RemoteException {
        int index = getQueueIndex(queueName);
        if (index != -1) {
            Queue queue = queues.get(index);
            queue.registerConsumer(callback);
        }
    }

    public static void main(String[] args) {
        String ip_port = args[0];
        try {
            Broker stub = new Broker();
            System.out.println("Broker stub created!");

            Naming.rebind("//" + ip_port + "/Queue", stub);
            System.out.println("Broker is ready and bound to " + ip_port);
        } catch (Exception e) {
            System.err.println("Broker exception: " + e.toString());
            e.printStackTrace();
        }

    }
}
