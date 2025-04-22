import java.rmi.Remote;
import java.rmi.RemoteException;


// Interfaz remota que define el contrato que deben implementar los servidores RMI.
public interface ServerJL15 extends Remote {
    
    // Permite la ejecución de un servicio remoto especificando su nombre y parámetros.
    public abstract String ejecutar_servicio(String nombre_servicio, Object... parametros) throws RemoteException;
}