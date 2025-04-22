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

    // Estructura: Map<nombreServicio, Map<nombreCliente, Resultado>>
    private Map<String, Map<String, String>> respuestasPendientes = new HashMap<>();

    // Constructor del Broker
    public Broker() throws RemoteException {
        super();
        servidores = new HashMap<String, String>();
        servicios = new ArrayList<Servicio>();
    }

    // API SERVIDOR

    // Método para registrar un servidor en el Broker. Asocia el nombre del servidor con su dirección IP y puerto
    public void registrar_servidor(String nombre_servidor, String host_remoto_IP_port) throws RemoteException {
        if (!servidores.containsKey(nombre_servidor)) {
            servidores.put(nombre_servidor, host_remoto_IP_port);
            System.out.println("Servidor registrado: " + nombre_servidor + " en " + host_remoto_IP_port);
        } else {
            System.out.println("El servidor ya está registrado.");
        }
    }

    // Da de alta un nuevo servicio, si el servidor está registrado y el servicio aún no existe
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

    // Da de baja un servicio
    public void baja_servicio(String nom_servicio) throws RemoteException {
        Servicio s = obtenerServicioPorNombre(nom_servicio);
        if (s != null) {
            servicios.remove(s);
        }
    }


    // API CLIENTE

    public List<Servicio> obtener_servicios() throws RemoteException {
        return servicios; // Retorna todos los valores del mapa como una lista
    }

    // Ejecuta el servicio de forma sincrónica llamando al servidor correspondiente por RMI
    // Convierte el Vector de parámetros a arreglo y hace un lookup al servidor remoto
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

    // Ejecuta el servicio de forma asíncrona y guarda el resultado en un mapa para que el cliente lo recoja más tarde
    // Controla duplicados para evitar solicitudes pendientes múltiples del mismo cliente al mismo servicio
    public void ejecutar_servicio_asinc(String nom_cliente, String nombre_servicio, Vector<String> parametros)
            throws RemoteException {
        Servicio s = obtenerServicioPorNombre(nombre_servicio);
        if (s == null) {
            System.out.println("El servicio '" + nombre_servicio + "' no está registrado.");
            return;
        }

        // Verificamos si ya hay una solicitud pendiente para este cliente y servicio (3)
        if (respuestasPendientes.containsKey(nombre_servicio) &&
                respuestasPendientes.get(nombre_servicio).containsKey(nom_cliente)) {
            System.out.println("Ya existe una solicitud pendiente para este cliente y servicio.");
            return;
        }

        String direccionServidor = servidores.get(s.getNombreServidor());

        try {
            ServerJL15 servidor = (ServerJL15) Naming
                    .lookup("//" + direccionServidor + "/" + s.getNombreServidor());

            Object[] parametrosArray = parametros.toArray(new Object[0]);
            String resultado = servidor.ejecutar_servicio(nombre_servicio, parametrosArray);

            // Guardamos el resultado para que el cliente lo recoja más tarde
            respuestasPendientes
                    .computeIfAbsent(nombre_servicio, k -> new HashMap<>())
                    .put(nom_cliente, resultado);

            System.out.println("Solicitud asíncrona procesada para cliente " + nom_cliente);

        } catch (Exception e) {
            System.out.println("Error al ejecutar el servicio asíncrono: " + e.getMessage());
        }
    }

    // Recupera la respuesta previamente almacenada de una ejecución asíncrona y la borra (entrega única)
    public String obtener_respuesta_asinc(String nom_cliente, String nombre_servicio) throws RemoteException {
        // Error si el cliente no habia solicitado el servicio, (2-i)
        //     o si no es mismo cliente (2-ii)
        //     o si ya se ha recogido la respuesta (2-iii)
        if (!respuestasPendientes.containsKey(nombre_servicio) ||
                !respuestasPendientes.get(nombre_servicio).containsKey(nom_cliente)) {
            return "Error: El cliente no ha solicitado el servicio '" + nombre_servicio + "' o ya ha recogido la respuesta.";
        }

        // Se puede entregar la respuesta
        String resultado = respuestasPendientes.get(nombre_servicio).get(nom_cliente);
        
        // Respuesta ya entregada, la borramos
        respuestasPendientes.get(nombre_servicio).remove(nom_cliente);

        return resultado;
    }

    // Funciones Auxiliares
    private Servicio obtenerServicioPorNombre(String nombre) {
        for (Servicio s : servicios) {
            if (s.getNombreServicio().equals(nombre)) {
                return s;
            }
        }
        return null;
    }

    // Método main que  arranca el servidor RMI del Broker y lo registra en el registro RMI con la ruta especificada
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
