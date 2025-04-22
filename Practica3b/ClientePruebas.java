// ClientePruebas.java
// Este cliente realiza pruebas de distintos posibles escenarios de la ejeccución asíncrona de servicios

import java.rmi.Naming;
import java.util.List;
import java.util.Vector;

public class ClientePruebas {
    public static void main(String[] args) {
        try {
            String brokerIP = "155.210.154.204:32003"; // IP:puerto del broker
            BrokerAbs broker = (BrokerAbs) Naming.lookup("//" + brokerIP + "/MyBrokerJL15");

            String clienteID = "clientePrueba1";
            String clienteIDfalso = "clienteFalso";
            String nombreServicio = "";
            Vector<String> parametros = new Vector<>();

            // Obtener servicios disponibles
            List<Servicio> listaServicios = broker.obtener_servicios();
            if (listaServicios.isEmpty()) {
                System.out.println("No hay servicios disponibles.");
                return;
            }

            // Seleccionamos el primer servicio de la lista
            Servicio servicioSeleccionado = listaServicios.get(0);
            nombreServicio = servicioSeleccionado.getNombreServicio();
            System.out.println("Seleccionado servicio: " + nombreServicio);

            // Preparar parámetros de prueba (rellenar con valores genéricos)
            for (String tipo : servicioSeleccionado.getParametros()) {
                parametros.add("1"); 
            }

            // Ejecutar servicio de forma asíncrona
            System.out.println("Ejecutando servicio '" + nombreServicio + "' de forma asíncrona...");
            broker.ejecutar_servicio_asinc(clienteID, nombreServicio, parametros);
            
            // (3) Volver a solicitar el mismo servicio (debería dar error)
            System.out.println("Manando nueva solicitud sin haber recogido la respuesta ");
            broker.ejecutar_servicio_asinc(clienteID, nombreServicio, parametros);

            System.out.println("\nEsperando para recoger la respuesta...");
            Thread.sleep(2000); 

            // (2-ii) Otro cliente intenta recoger la respuesta (debería dar error)
            System.out.println("\nOtro cliente intenta recoger la respuesta (debería fallar)...");
            String resultadoFalso = broker.obtener_respuesta_asinc(clienteIDfalso, nombreServicio);
            System.out.println(resultadoFalso);

            String resultado = broker.obtener_respuesta_asinc(clienteID, nombreServicio);
            System.out.println("Resultado recogido correctamente: " + resultado);

            // (2-iii) Intentar recoger la respuesta otra vez (debería dar error)
            System.out.println("\nIntentando recoger la respuesta otra vez (debería fallar)...");
            String resultadoDuplicado = broker.obtener_respuesta_asinc(clienteID, nombreServicio);
            System.out.println(resultadoDuplicado);
           
            // (2-i) Intentar recoger un servicio no solicitado (nuevo servicio no pedido)
            System.out.println("\nIntentando recoger un servicio no solicitado...");
            String resultadoNoSolicitado = broker.obtener_respuesta_asinc(clienteID, listaServicios.get(1).getNombreServicio());
            System.out.println(resultadoNoSolicitado);

        } catch (Exception ex) {
            System.out.println("Error general: " + ex);
        }
    }
}
