import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


// Clase base que implementa la interfaz ServerJL15.
public class ServerJL15Impl extends UnicastRemoteObject implements ServerJL15 {

    //Constructor requerido que exporta el objeto para recibir llamadas RMI
    protected ServerJL15Impl() throws RemoteException {
        super();
    }


    //Implementación genérica del método ejecutar_servicio que busca dinámicamente
    //un método con el nombre y tipos de parámetros dados, y lo ejecuta.
    @Override
    public String ejecutar_servicio(String nombre_servicio, Object... parametros) throws RemoteException {
        try {

            Class<?> clase = this.getClass();

            // Construir el arreglo de tipos de parámetros
            Class<?>[] tiposParametros = new Class<?>[parametros.length];
            for (int i = 0; i < parametros.length; i++) {
                tiposParametros[i] = parametros[i].getClass();
            }

            // Buscar el método por nombre y tipos de parámetro
            Method metodo = clase.getMethod(nombre_servicio, tiposParametros);

            // Invocar el método con los parámetros
            Object resultado = metodo.invoke(this, parametros);

            // Retornar el resultado como String
            return resultado != null ? resultado.toString() : "null";

        } catch (NoSuchMethodException e) {
            return "Método no encontrado: " + nombre_servicio;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al invocar el método: " + e.getMessage();
        }
    }
}
