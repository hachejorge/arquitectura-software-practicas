import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Cliente {
    public static void main(String[] args) {
        try {
            String brokerIP = "155.210.154.204:32003"; // IP:puerto del broker
            BrokerAbs broker = (BrokerAbs) Naming.lookup("//" + brokerIP + "/MyBrokerJL15");

            Scanner sc = new Scanner(System.in);

            while (true) {
                // Mostrar servicios disponibles
                List<Servicio> listaServicios = broker.obtener_servicios();
                System.out.println("\n=== Servicios disponibles en el Broker ===");
                for (Servicio s : listaServicios) {
                    System.out.println(s.servicioToString());
                }

                // Pedir al usuario el nombre del servicio
                System.out.print("\nIngresa el nombre del servicio que deseas ejecutar (o escribe 'salir'): ");
                String nombreServicio = sc.nextLine();

                if (nombreServicio.equalsIgnoreCase("salir")) {
                    System.out.println("Saliendo del cliente.");
                    break;
                }

                // Buscar el servicio seleccionado
                Servicio servicioSeleccionado = null;
                for (Servicio s : listaServicios) {
                    if (s.getNombreServicio().equals(nombreServicio)) {
                        servicioSeleccionado = s;
                        break;
                    }
                }

                if (servicioSeleccionado == null) {
                    System.out.println("Servicio no encontrado.");
                    continue;
                }

                // Pedir al usuario los parámetros del servicio
                Vector<String> parametros = new Vector<>();
                List<String> tiposParametros = servicioSeleccionado.getParametros();

                for (int i = 0; i < tiposParametros.size(); i++) {
                    System.out.print("Ingresa el parámetro " + (i + 1) + " de tipo " + tiposParametros.get(i) + ": ");
                    String valor = sc.nextLine();
                    parametros.add(valor);
                }

                // Llamar al broker para ejecutar el servicio
                String resultado = broker.ejecutar_servicio(nombreServicio, parametros);
                System.out.println("Resultado: " + resultado);
            }

            sc.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }
}
