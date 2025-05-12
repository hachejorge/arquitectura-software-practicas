import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BrokerInterface extends Remote {

    // Crea una nueva cola si no existe ya
    void declare_queue(String name) throws RemoteException;

    // Publica un mensaje en una cola existente
    void publish(String queueName, Message message) throws RemoteException;

    // Registra un consumidor en una cola
    void consume(String queueName, Callback consumer) throws RemoteException;

    // Devuelve la lista de nombres de colas disponibles
    List<String> listarColas() throws RemoteException;

     // Elimina una cola, sus archivos, y su referencia en memoria
    void eliminarCola(String nombre) throws RemoteException;

     // Confirma la recepción de un mensaje procesado
    void ack(String idMensaje, String cola) throws RemoteException;

}
