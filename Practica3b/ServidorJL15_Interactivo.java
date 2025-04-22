// ServidorJL15_Interactivo.java
// Este servidor es una variación del ServidorJL15_01.java, 
// desde el cual podemos dar de alta y de baja distintos servicios en el broker.

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.Vector;

public class ServidorJL15_Interactivo extends UnicastRemoteObject implements ServerJL15 {

    private static BrokerAbs broker;
    private static String nombreServidor = "ServidorJL15_Interactivo";
    private static String host = "155.210.154.206:32003"; // Dirección del servidor
    private static String remote_broker = "155.210.154.204:32003"; // Dirección del broker

    public ServidorJL15_Interactivo() throws RemoteException {
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

    @Override
    public String ejecutar_servicio(String nombre_servicio, Object... parametros) throws RemoteException {
        try {
            Class<?> clase = this.getClass();
            Class<?>[] tiposParametros = new Class<?>[parametros.length];
            for (int i = 0; i < parametros.length; i++) {
                tiposParametros[i] = parametros[i].getClass();
            }
            Method metodo = clase.getMethod(nombre_servicio, tiposParametros);
            Object resultado = metodo.invoke(this, parametros);
            return resultado != null ? resultado.toString() : "null";
        } catch (NoSuchMethodException e) {
            return "Método no encontrado: " + nombre_servicio;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al invocar el método: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        try {
            // Crear objeto servidor
            ServidorJL15_Interactivo server = new ServidorJL15_Interactivo();
            System.out.println(" [Servidor] Creado !");
            Naming.rebind("//" + host + "/" + nombreServidor, server);
            System.out.println(" [Servidor] Registrado en RMI !");

            // Conectar con el broker
            broker = (BrokerAbs) Naming.lookup("//" + remote_broker + "/MyBrokerJL15");
            System.out.println("[Broker] Conexión exitosa!");

            // Registrar servidor en el broker
            broker.registrar_servidor(nombreServidor, host);
            System.out.println("[Broker] Servidor registrado.");

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("\n¿Qué quieres hacer?");
                System.out.println("1. Dar de alta un servicio");
                System.out.println("2. Dar de baja un servicio");
                System.out.println("3. Salir");
                System.out.print("Opción: ");
                String opcion = sc.nextLine();

                if (opcion.equals("1")) {
                    // Alta de servicio
                    System.out.print("Nombre del método (debe existir en el servidor): ");
                    String nombreMetodo = sc.nextLine();

                    System.out.print("Número de parámetros: ");
                    int numParametros = Integer.parseInt(sc.nextLine());

                    Vector<String> parametros = new Vector<>();
                    for (int i = 0; i < numParametros; i++) {
                        System.out.print("Tipo del parámetro " + (i + 1) + " (int, String, etc.): ");
                        parametros.add(sc.nextLine());
                    }

                    System.out.print("Tipo de retorno (int, String, etc.): ");
                    String tipoRetorno = sc.nextLine();

                    Servicio servicio = new Servicio(nombreMetodo, nombreServidor, parametros, tipoRetorno);
                    broker.alta_servicio(servicio);
                    System.out.println("Servicio '" + nombreMetodo + "' dado de alta exitosamente.");

                } else if (opcion.equals("2")) {
                    // Baja de servicio
                    System.out.print("Nombre del servicio a dar de baja: ");
                    String nombreBaja = sc.nextLine();

                    broker.baja_servicio(nombreBaja);
                    System.out.println("Servicio '" + nombreBaja + "' dado de baja exitosamente.");

                } else if (opcion.equals("3")) {
                    System.out.println("Cerrando servidor...");
                    break;
                } else {
                    System.out.println("Opción inválida.");
                }
            }
            sc.close();

        } catch (Exception ex) {
            System.out.println("[Error] " + ex);
            ex.printStackTrace();
        }
    }
}
