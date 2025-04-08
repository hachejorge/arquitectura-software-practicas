import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ServidorJL15_01 extends UnicastRemoteObject implements ServerJL15 {

    public ServidorJL15_01() throws RemoteException {
    }

    public int suma(String a, String b) {
        return Integer.parseInt(a) + Integer.parseInt(b);
    }

    public int resta(String a, String b) {
        return Integer.parseInt(a) - Integer.parseInt(b);
    }

    public int multiplicar(String a, String b) {
        return Integer.parseInt(a) * Integer.parseInt(b);
    }

    public int cuadrado(String a) {
        return Integer.parseInt(a) * Integer.parseInt(a);
    }

    public String ejecutar_servicio(String nombre_servicio, Object... parametros) throws RemoteException {
        try {

            Class<?> clase = this.getClass();

            // Construir el arreglo de tipos de parámetros
            Class<?>[] tiposParametros = new Class<?>[parametros.length];
            for (int i = 0; i < parametros.length; i++) {
                tiposParametros[i] = parametros[i].getClass();
            }

            // Buscar el método por nombre y tipos de parámetro
            Method metodo = clase.getMethod(nombre_servicio, tiposParametros);

            // Invocar el método con los parámetros
            Object resultado = metodo.invoke(this, parametros);

            // Retornar el resultado como String
            return resultado != null ? resultado.toString() : "null";

        } catch (NoSuchMethodException e) {
            return "Método no encontrado: " + nombre_servicio;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al invocar el método: " + e.getMessage();
        }
    }

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

            broker.registrar_servidor("ServidorJL15_01", host);
            Vector<String> parametrosSuma = new Vector<>();
            parametrosSuma.add("int");
            parametrosSuma.add("int");
            Servicio serviceSuma = new Servicio("suma", "ServidorJL15_01", parametrosSuma, "int");
            Servicio serviceResta = new Servicio("resta", "ServidorJL15_01", parametrosSuma, "int");
            Servicio serviceMul = new Servicio("multiplicacion", "ServidorJL15_01", parametrosSuma, "int");

            Vector<String> parametrosCuad = new Vector<>();
            parametrosCuad.add("int");
            Servicio serviceCuad = new Servicio("cuadrado", "ServidorJL15_01", parametrosCuad, "int");

            broker.alta_servicio(serviceSuma);
            broker.alta_servicio(serviceResta);
            broker.alta_servicio(serviceMul);
            broker.alta_servicio(serviceCuad);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}