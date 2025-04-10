import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerJL15 extends Remote {
    public abstract String ejecutar_servicio(String nombre_servicio, Object... parametros) throws RemoteException;
}