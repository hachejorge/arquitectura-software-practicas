import java.rmi.Naming;

public class Cliente {
    public static void main ( String [] args ) {
        try {
            // Paso 1 - Obtener una referencia al objeto servidor creado anteriormente
            // Nombre del host servidor o su IP. Es dó nde se buscar á al objeto remoto
            String brokerIP = ""; // se puede usar "IP:puerto "
            Broker broker = (Collection) Naming.lookup ("//"+ brokerIP + "/MyBroker") ;
            // Paso 2 - Invocar remotamente los metodos del objeto servidor :
            List<Servicio> listaServicios = broker.obtener_servicios();
            System.out.println("Servicios disponibles en el Broker:");
            for (Servicio s : listaServicios) {
                System.out.println(s.servicioToString());
            }

            
            Vector<Int> v = {1, 2};
            resultado = broker.ejecutar_servicio(suma, v);
            System.out.println("La suma de " + v[0] " y " + v[1] + " da " + resultado);

        }
        catch ( Exception ex ) {
            System.out.println ( ex );
        }
    }
}
