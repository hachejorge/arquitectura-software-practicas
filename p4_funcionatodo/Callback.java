import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback extends Remote {
    // Método invocado por el broker cuando se entrega un mensaje.
    // Simula un procesamiento (durmiendo 15 segundos) y luego hace ACK.
    void procesarMensaje(String id, String contenido, String cola) throws RemoteException;
}

