import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Broker extends UnicastRemoteObject implements BrokerInterface {

    // Archivo donde se guardan los nombres de las colas persistidas
    private static final String ARCHIVO_COLAS = "colas.dat";

    // Mapa que mantiene todas las colas existentes
    private final ConcurrentHashMap<String, Cola> colas = new ConcurrentHashMap<>();

    // Constructor: carga colas persistidas desde disco
    public Broker() throws RemoteException {
        super();
        cargarColasPersistidas();
    }

    // Crea una nueva cola si no existe ya
    @Override
    public synchronized void declare_queue(String nombre) throws RemoteException {
        if (colas.computeIfAbsent(nombre, Cola::new) != null) {
            guardarListaDeColas(); // Guarda el nuevo estado si se ha creado una nueva cola
        }
    }

    // Publica un mensaje en una cola existente
    @Override
    public synchronized void publish(String nombreCola, Message mensaje) throws RemoteException {
        Cola cola = colas.get(nombreCola);
        if (cola != null) {
            cola.publicar(mensaje.getContenido());
        } else {
            System.out.println("Cola inexistente: " + nombreCola);
        }
    }

    // Registra un consumidor en una cola
    @Override
    public synchronized void consume(String nombreCola, Callback consumidor) throws RemoteException {
        Cola cola = colas.get(nombreCola);
        if (cola != null) {
            cola.consumir(consumidor);
        } else {
            System.out.println("Cola inexistente: " + nombreCola);
        }
    }

    // Devuelve la lista de nombres de colas disponibles
    @Override
    public synchronized List<String> listarColas() throws RemoteException {
        return new ArrayList<>(colas.keySet());
    }

     // Elimina una cola, sus archivos, y su referencia en memoria
    @Override
    public synchronized void eliminarCola(String nombre) throws RemoteException {
        Cola cola = colas.remove(nombre);
        if (cola != null) {
            cola.shutdown(); // Detiene hilos internos de la cola
            
            // Borra los archivos persistidos en disco
            File dir = new File(nombre);
            for (File f : dir.listFiles()) f.delete();
            dir.delete();
            guardarListaDeColas(); // Guarda nueva lista tras eliminación
        }
    }


    // Confirma la recepción de un mensaje procesado
    @Override
    public void ack(String idMensaje, String cola) throws RemoteException {
        Cola c = colas.get(cola);
        c.ack(idMensaje);  // Informa a la cola para eliminar el archivo del mensaje
    }


    // Carga nombres de colas previamente guardados en ARCHIVO_COLAS
    @SuppressWarnings("unchecked")
    private void cargarColasPersistidas() {
        File f = new File(ARCHIVO_COLAS);
        if (!f.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            List<String> nombres = (List<String>) in.readObject();
            for (String nombre : nombres) {
                colas.put(nombre, new Cola(nombre)); // Se reconstruye cada cola
            }
            System.out.println("Colas restauradas desde archivo.");
        } catch (Exception e) {
            System.out.println("Error restaurando colas: " + e.getMessage());
        }
    }

    // Guarda la lista actual de colas en disco
    private void guardarListaDeColas() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_COLAS))) {
            out.writeObject(new ArrayList<>(colas.keySet()));
        } catch (IOException e) {
            System.out.println("Error guardando lista de colas: " + e.getMessage());
        }
    }


    // Método principal: arranca el broker y lo registra en RMI
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Broker <ip:puerto>");
            return;
        }
        String ip_port = args[0];
        try {
            Broker broker = new Broker(); // Instancia del broker
            Naming.rebind("//" + ip_port + "/Queue", broker); // Registro en RMI
            System.out.println("Broker registrado en " + ip_port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
