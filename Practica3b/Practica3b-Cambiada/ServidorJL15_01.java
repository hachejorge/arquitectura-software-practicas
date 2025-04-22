// ServidorJL15_01.java
// Este servidor da de alta 4 cuatro servicios en el broker:
// suma, resta, multiplicar y cuadrado.


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

public class ServidorJL15_01 extends ServerJL15Impl {

    public ServidorJL15_01() throws RemoteException {
    }

    //Servicio de suma que recibe dos números como cadenas y retorna su suma.
    public int suma(String a, String b) {
        return Integer.parseInt(a) + Integer.parseInt(b);
    }

    // Servicio de resta que recibe dos números como cadenas y retorna su resta.
    public int resta(String a, String b) {
        return Integer.parseInt(a) - Integer.parseInt(b);
    }

    //Servicio de multiplicación que recibe dos números como cadenas y retorna su multiplicación.
    public int multiplicar(String a, String b) {
        return Integer.parseInt(a) * Integer.parseInt(b);
    }

    //Servicio de cuadrado que recibe un número como cadena y retorna su cuadrado.
    public int cuadrado(String a) {
        return Integer.parseInt(a) * Integer.parseInt(a);
    }


    //Método principal para iniciar el servidor. Registra el servidor y los servicios en el broker RMI.
    public static void main(String args[]) {

        String host = "155.210.154.206:32003"; // Dirección del servidor
        String remote_broker = "155.210.154.204:32003"; // Dirección del broker

        // Por defecto , RMI usa el puerto 1099
        try {
            // Crear objeto remoto
            ServidorJL15_01 server = new ServidorJL15_01();
            System.out.println(" [Servidor] Creado !");
            // Registrar el objeto remoto
            Naming.rebind("//" + host + "/ServidorJL15_01", server);
            System.out.println(" [Servidor] Registrado !");

            // Crear objeto remoto
            BrokerAbs broker = (BrokerAbs) Naming.lookup("//" + remote_broker + "/MyBrokerJL15");

            System.out.println("[Broker] Conexión exitosa!");

            // Registrar el servidor en el Broker
            broker.registrar_servidor("ServidorJL15_01", host);
            
            // Definir los parámetros de los servicios
            Vector<String> parametrosSuma = new Vector<>();
            parametrosSuma.add("int");
            parametrosSuma.add("int");

            // Crear los servicios a registrar en el Broker
            Servicio serviceSuma = new Servicio("suma", "ServidorJL15_01", parametrosSuma, "int");
            Servicio serviceResta = new Servicio("resta", "ServidorJL15_01", parametrosSuma, "int");
            Servicio serviceMul = new Servicio("multiplicacion", "ServidorJL15_01", parametrosSuma, "int");

            Vector<String> parametrosCuad = new Vector<>();
            parametrosCuad.add("int");
            Servicio serviceCuad = new Servicio("cuadrado", "ServidorJL15_01", parametrosCuad, "int");

            // Dar de alta los servicios en el Broker
            broker.alta_servicio(serviceSuma);
            broker.alta_servicio(serviceResta);
            broker.alta_servicio(serviceMul);
            broker.alta_servicio(serviceCuad);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}