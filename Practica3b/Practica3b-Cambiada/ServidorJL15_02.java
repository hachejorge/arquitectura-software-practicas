// ServidorJL15_02.java
// Este servidor da de alta un servicio en el broker:
// cuenta_vocales, que cuenta el número de vocales en una palabra.

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

public class ServidorJL15_02 extends ServerJL15Impl {

    public ServidorJL15_02() throws RemoteException {
    }

     // Método que cuenta el número de vocales en una palabra
    public int cuenta_vocales(String palabra) {
        int contador = 0;
        String vocales = "aeiouAEIOU";

        // Iteramos sobre cada caracter de la palabra
        for (int i = 0; i < palabra.length(); i++) {
            // Si el caracter es una vocal, aumentamos el contador
            if (vocales.indexOf(palabra.charAt(i)) != -1) {
                contador++;
            }
        }

        return contador;
    }

    //Método principal para iniciar el servidor. Registra el servidor y los servicios en el broker RMI.
    public static void main(String args[]) {

        String host = "155.210.154.206:32003"; // Dirección del servidor
        String remote_broker = "155.210.154.204:32003"; // Dirección del broker

        // Por defecto , RMI usa el puerto 1099
        try {
            // Crear objeto remoto
            ServidorJL15_02 server = new ServidorJL15_02();
            System.out.println(" [Servidor] Creado !");
            // Registrar el objeto remoto
            Naming.rebind("//" + host + "/ServidorJL15_02", server);
            System.out.println(" [Servidor] Registrado !");

            // Crear objeto remoto
            BrokerAbs broker = (BrokerAbs) Naming.lookup("//" + remote_broker + "/MyBrokerJL15");

            System.out.println("[Broker] Conexión exitosa!");

            // Registrar el servidor en el Broker
            broker.registrar_servidor("ServidorJL15_02", host);

             // Definir los parámetros del servicio
            Vector<String> parametros = new Vector<>();
            parametros.add("string");

            // Crear el servicio a registrar en el Broker
            Servicio serviceVocales = new Servicio("cuenta_vocales", "ServidorJL15_02", parametros, "int");

            // Dar de alta el servicio en el Broker
            broker.alta_servicio(serviceVocales);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}