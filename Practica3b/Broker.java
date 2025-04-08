import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

public class Broker extends UnicastRemoteObject implements BrokerAbs {

    // Nombre Servidor -> IP
    private Map<String, String> servidores;

    // Servicios
    private List<Servicio> servicios;

    // Constructor del Broker
    public Broker() throws RemoteException {
        super();
        servidores = new HashMap<String, String>();
        servicios = new ArrayList<Servicio>();
    }

    // API SERVIDOR
    // Método para registrar un servidor en el Broker
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_port) throws RemoteException {
        if (!servidores.containsKey(nombre_servidor)) {
            servidores.put(nombre_servidor, host_remoto_IP_port);
            System.out.println("Servidor registrado: " + nombre_servidor + " en " + host_remoto_IP_port);
        } else {
            System.out.println("El servidor ya está registrado.");
        }
    }

    // Dar de alta un servicio
    public void alta_servicio(Servicio service) throws RemoteException {
        if (servidores.containsKey(service.getNombreServidor())) {
            Servicio s = obtenerServicioPorNombre(service.getNombreServicio());
            if (s != null) {
                System.out.println("El servicio '" + service.getNombreServicio() + "' ya está registrado.");
            } else {
                servicios.add(service);
            }
        } else {
            System.out.println("El servidor '" + service.getNombreServidor() + "' no está registrado.");
        }
    }

    // Dar de baja un servicio
    public void baja_servicio(String nom_servicio) throws RemoteException {
        Servicio s = obtenerServicioPorNombre(nom_servicio);
        if (s != null) {
            servicios.remove(s);
        }
    }

    public List<Servicio> obtener_servicios() throws RemoteException {
        return servicios; // Retorna todos los valores del mapa como una lista
    }

    // Ejecutar un servicio
    public String ejecutar_servicio(String nombre_servicio, Vector<String> parametros) throws RemoteException {
        Servicio s = obtenerServicioPorNombre(nombre_servicio);
        if (s != null) {
            String direccionServidor = servidores.get(s.getNombreServidor());

            System.out.println("Ejecutando servicio '" + nombre_servicio + "' en servidor '" + s.getNombreServidor() +
                    "' con dirección: " + direccionServidor + " y parámetros: " + parametros);
            try {
                // Buscar el servicio en el servidor remoto
                ServerJL15 servidor = (ServerJL15) Naming
                        .lookup("//" + direccionServidor + "/" + s.getNombreServidor());

                Object[] parametrosArray = parametros.toArray(new Object[0]);

                System.out.println("Parámetros:" + parametrosArray);

                return servidor.ejecutar_servicio(nombre_servicio, parametrosArray);

            } catch (Exception e) {
                System.out.println("Error al ejecutar el servicio: " + e.getMessage());
                return "Error al ejecutar el servicio";
            }
        } else {
            return "El servicio '" + nombre_servicio + "' no está registrado.";
        }
    }

    // Funciones Auxiliar
    private Servicio obtenerServicioPorNombre(String nombre) {
        for (Servicio s : servicios) {
            if (s.getNombreServicio().equals(nombre)) {
                return s;
            }
        }
        return null;
    }

    // Método main que arranca el servidor RMI
    public static void main(String[] args) {
        String hostName = "155.210.154.204:32003"; // Aquí puedes poner la IP o nombre del host remoto
        try {
            // Crear objeto remoto (Broker)
            Broker obj = new Broker();
            System.out.println("Broker creado!");

            // Registrar el objeto remoto en el registro RMI
            Naming.rebind("//" + hostName + "/MyBrokerJL15", obj);
            System.out.println("Broker registrado en RMI!");

        } catch (Exception ex) {
            System.out.println("Error en el servidor RMI: " + ex);
        }
    }
}
