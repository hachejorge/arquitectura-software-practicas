import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.List;

public interface BrokerAbs extends Remote {
    public abstract List<Servicio> obtener_servicios() throws RemoteException;

    public abstract void registrar_servidor(String nombre_servidor, String host_remoto_IP_port) throws RemoteException;

    public abstract void alta_servicio(Servicio service) throws RemoteException;

    public abstract void baja_servicio(String nom_servicio) throws RemoteException;

    public abstract String ejecutar_servicio(String nombre_servicio, Vector<String> parametros) throws RemoteException;

    public abstract void ejecutar_servicio_asinc(String nom_cliente, String nombre_servicio, Vector<String> parametros)
            throws RemoteException;

    public abstract String obtener_respuesta_asinc(String nom_cliente, String nombre_servicio) throws RemoteException;

};