import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Broker extends UnicastRemoteObject {
    
    // Nombre Servidor -> IP
    private Map<String, String> servidores;

    // Nombre Servicio -> Nombre Servidor
    private List<Servicio> servicios;

    // Constructor del Broker
    public Broker() throws RemoteException {
        super();
        servidores = new HashMap<String, String>();
        servicios = new List<Servicio>();
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
            if (!servicios.containsKey(service.getNombreServicio())) {
                servicios.put(service);
                System.out.println("Servicio '" + service.getNombreServicio() + "' registrado en servidor '" + service.getNombreServidor() + "'");
            } else {
                System.out.println("El servicio '" + service.getNombreServicio() + "' ya está registrado.");
            }
        } else {
            System.out.println("El servidor '" + service.getNombreServidor() + "' no está registrado.");
        }
    }

    // Dar de baja un servicio
    public void baja_servicio(String nom_servicio) throws RemoteException {
        if (servicios.containsKey(nom_servicio)) {
            servicios.remove(nom_servicio);
            System.out.println("Servicio '" + nom_servicio + "' eliminado.");
        } else {
            System.out.println("El servicio '" + nom_servicio + "' no está registrado.");
        }
    }

    public List<Servicio> obtener_servicios() {
        return servicios; // Retorna todos los valores del mapa como una lista
    }

    // Ejecutar un servicio
    public String ejecutar_servicio(String nombre_servicio, Vector<String> parametros) throws RemoteException {
        if (servicios.containsKey(nombre_servicio)) {
            Servicio service = servicios.get(nombre_servicio);
            String direccionServidor = servidores.get(service.getNombreServidor());

            System.out.println("Ejecutando servicio '" + nombre_servicio + "' en servidor '" + service.getNombreServidor() +
                            "' con dirección: " + direccionServidor + " y parámetros: " + parametros);

            try {
                // Buscar el servicio en el servidor remoto
                ServidorJL15 servidor = (ServidorJL15) Naming.lookup("//" + direccionServidor + "/" + service.getNombreServidor());
                return servidor.{service.getFuncion()}(parametros);
            } catch (Exception e) {
                System.out.println("Error al ejecutar el servicio: " + e.getMessage());
                return "Error al ejecutar el servicio";
            }
        } else {
            return "El servicio '" + service.getNombreServidor() + "' no está registrado.";
        }
    }

    // Método main que arranca el servidor RMI
    public static void main(String[] args) {
        String hostName = "localhost"; // Aquí puedes poner la IP o nombre del host remoto
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
