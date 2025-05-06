package MOM;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConcreteConsumer extends UnicastRemoteObject implements Consumer {

    protected ConcreteConsumer() throws RemoteException {
        super();
    }

    public void procesarMensaje(String mensaje) throws RemoteException {
        System.out.println("[C1] Recibido: " + mensaje);

    }

    public static void main(String[] args) {
        String ip_broker = args[0];
        String queue_name = "testQueue";

        try {
            BrokerInterface broker = (BrokerInterface) Naming.lookup("//" + ip_broker + "/Queue");
            System.out.println("Connected to broker at " + ip_broker);

            broker.declare_queue(queue_name);
            System.out.println("Queue declared!");

            Consumer consumer = new ConcreteConsumer();

            broker.consume(queue_name, consumer);

            while (true) {
            }

        } catch (Exception e) {
            System.err.println("Error connecting to broker: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
