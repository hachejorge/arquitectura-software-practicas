package MOM;

import java.rmi.Naming;

public class ConcreteProducer {
    public static void main(String[] args) {
        String ip_broker = args[0];

        String queue_name = "testQueue";

        Message message = new Message("Hello, World!");

        try {
            BrokerInterface broker = (BrokerInterface) Naming.lookup("//" + ip_broker + "/Queue");
            System.out.println("Connected to broker at " + ip_broker);

            broker.declare_queue(queue_name);
            System.out.println("Queue declared!");

            broker.publish(queue_name, message);
            System.out.println("Message published!");

        } catch (Exception e) {
            System.err.println("Error connecting to broker: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
