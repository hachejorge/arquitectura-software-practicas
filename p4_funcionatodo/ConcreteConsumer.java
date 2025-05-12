import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ConcreteConsumer extends UnicastRemoteObject implements Callback {

    private final BrokerInterface broker; // Referencia al broker remoto

    //Constructor: recibe e inyecta la referencia al broker.
    protected ConcreteConsumer(BrokerInterface broker) throws RemoteException {
        super();
        this.broker = broker;
    }

    // Método invocado por el broker cuando se entrega un mensaje.
    // Simula un procesamiento (durmiendo 15 segundos) y luego hace ACK.
    @Override
    public void procesarMensaje(String id_mensaje, String contenido_mensaje, String cola) throws RemoteException {
        System.out.println("[C] Recibido de '" + cola + "': " + contenido_mensaje);
        
        try {
            Thread.sleep(15000); // Simula procesamiento
            broker.ack(id_mensaje, cola); // Confirmación
        } catch (InterruptedException e) {
            System.out.println("Procesamiento interrumpido.");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Error al hacer ACK: " + e.getMessage());
        }
    }

    //Método principal: se conecta al broker, registra colas, y queda esperando mensajes.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita la IP:puerto del broker
        System.out.print("Introduce la IP y puerto del broker (ej. 127.0.0.1:1099): ");
        String ip_broker = scanner.nextLine();

        try {
             // Localiza el broker en el registro RMI
            BrokerInterface broker = (BrokerInterface) Naming.lookup("//" + ip_broker + "/Queue");
            System.out.println("Conectado al broker");

            // Pide al usuario las colas a las que desea suscribirse
            System.out.print("\nIntroduce los nombres de las colas de las que quieres consumir (separadas por coma): ");
            String[] queue_names = scanner.nextLine().split(",");

            // Crea el consumidor RMI
            Callback consumer = new ConcreteConsumer(broker);

            // Se registra como consumidor en cada cola
            for (String raw_name : queue_names) {
                String queue_name = raw_name.trim();
                if (!queue_name.isEmpty()) {
                    broker.declare_queue(queue_name);
                    broker.consume(queue_name, consumer);
                    System.out.println("Consumidor registrado en cola: " + queue_name);
                }
            }

            // Ciclo infinito para mantener vivo al consumidor
            System.out.println("\nEsperando mensajes de todas las colas...\nPresiona Ctrl+C para salir.");
            while (true) {
                Thread.sleep(1000); // mantener vivo
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
