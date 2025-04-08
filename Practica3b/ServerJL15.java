import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ServerJL15 extends Remote {
    public abstract String ejecutar_servicio(String nombre_servicio, Object... parametros) throws RemoteException;
}