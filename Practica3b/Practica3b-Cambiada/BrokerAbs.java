import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.List;

public interface BrokerAbs extends Remote {

    // Lista todos los servicios registrados
    public abstract List<Servicio> obtener_servicios() throws RemoteException;

    // Registra un nuevo servidor en el broker
    public abstract void registrar_servidor(String nombre_servidor, String host_remoto_IP_port) throws RemoteException;

    // Da de alta un servicio ofrecido por un servidor ya registrado
    public abstract void alta_servicio(Servicio service) throws RemoteException;

    // Da de baja un servicio previamente registrado
    public abstract void baja_servicio(String nom_servicio) throws RemoteException;

    // Ejecuta un servicio de manera sincrónica
    public abstract String ejecutar_servicio(String nombre_servicio, Vector<String> parametros) throws RemoteException;

    // Ejecuta un servicio de forma asíncrona (el cliente deberá recoger luego el resultado)
    public abstract void ejecutar_servicio_asinc(String nom_cliente, String nombre_servicio, Vector<String> parametros) throws RemoteException;

    // Permite al cliente recuperar el resultado de una llamada asíncrona
    public abstract String obtener_respuesta_asinc(String nom_cliente, String nombre_servicio) throws RemoteException;

};