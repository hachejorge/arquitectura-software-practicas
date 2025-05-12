import java.rmi.Naming;
import java.util.Scanner;

public class ConcreteProducer {

    //Productor que se conecta al broker y permite enviar mensajes a distintas colas, creando la cola si no existe.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita la IP y puerto donde está registrado el broker RMI
        System.out.print("Introduce la IP y puerto del broker (ej. 127.0.0.1:1099): ");
        String ip_broker = scanner.nextLine();

        try {
            // Obtiene una referencia remota al broker desde el registro RMI
            BrokerInterface broker = (BrokerInterface) Naming.lookup("//" + ip_broker + "/Queue");
            System.out.println("Conectado al broker en " + ip_broker);

            // Bucle principal del menú de opciones
            while (true) {
                System.out.println("\n--- MENÚ PRODUCTOR ---");
                System.out.println("1. Publicar mensaje");
                System.out.println("2. Salir");
                System.out.print("Selecciona una opción: ");
                String opcion = scanner.nextLine();

                if (opcion.equals("1")) {
                    // Solicita el nombre de la cola
                    System.out.print("Nombre de la cola: ");
                    String queue_name = scanner.nextLine();

                    // Solicita el contenido del mensaje
                    System.out.print("Contenido del mensaje: ");
                    String contenido = scanner.nextLine();

                    // Crea el mensaje y lo envía al broker
                    Message message = new Message(contenido);
                    broker.declare_queue(queue_name); // asegura existencia
                    broker.publish(queue_name, message);
                    System.out.println("Mensaje publicado.");
                } else if (opcion.equals("2")) {
                    System.out.println("Saliendo...");
                    break;
                } else {
                    System.out.println("Opción inválida.");
                }
            }

        } catch (Exception e) {
             // Captura cualquier error relacionado con RMI
            System.err.println("Error conectando al broker: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
