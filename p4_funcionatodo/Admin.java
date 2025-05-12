import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class Admin {

    //Cliente administrativo que permite:
        //- Listar todas las colas registradas en el broker
        //- Eliminar una cola específica
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita al usuario la IP y puerto del broker RMI
        System.out.print("Introduce la IP y puerto del broker (ej. 127.0.0.1:1099): ");
        String ip_broker = scanner.nextLine();

        try {
            // Se conecta al objeto remoto 'Queue' del broker a través de RMI
            BrokerInterface broker = (BrokerInterface) Naming.lookup("//" + ip_broker + "/Queue");
            System.out.println("Conectado al broker");

             // Bucle principal del menú
            while (true) {
                System.out.println("\nOpciones:");
                System.out.println("1. Listar colas");
                System.out.println("2. Eliminar una cola");
                System.out.println("3. Salir");
                System.out.print("Selecciona una opción: ");

                String opcion = scanner.nextLine();

                switch (opcion) {
                    case "1":
                    // Opción para listar todas las colas activas
                        List<String> colas = broker.listarColas();
                        System.out.println("\nColas disponibles:");
                        for (String c : colas) {
                            System.out.println(" - " + c);
                        }
                        break;

                    case "2":
                    // Opción para eliminar una cola por nombre
                        System.out.print("Introduce el nombre de la cola a eliminar: ");
                        String cola = scanner.nextLine();
                        broker.eliminarCola(cola);
                        System.out.println("Cola '" + cola + "' eliminada.");
                        break;

                    case "3":
                    // Salir del programa
                        System.out.println("Saliendo...");
                        return;

                    default:
                        System.out.println("Opción no válida.");
                }
            }

        } catch (Exception e) {
            // Captura errores de conexión o invocación remota
            System.err.println("Error al conectarse al broker: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
