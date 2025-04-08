import java.util.Vector;
import java.io.Serializable;

public class Servicio implements Serializable {
    private String nombre_servicio;
    private String nombre_servidor;
    private Vector<String> parametros;
    private String retorno;

    // Constructor por defecto
    public Servicio() {
        this.parametros = new Vector<>();
    }

    // Constructor con parámetros
    public Servicio(String nombre_servicio, String nombre_servidor, Vector<String> parametros,
            String retorno) {
        this.nombre_servicio = nombre_servicio;
        this.nombre_servidor = nombre_servidor;
        this.parametros = parametros;
        this.retorno = retorno;
    }

    // Getters y Setters
    public String getNombreServicio() {
        return nombre_servicio;
    }

    public void setNombreServicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getNombreServidor() {
        return nombre_servidor;
    }

    public void setNombreServidor(String nombre_servidor) {
        this.nombre_servidor = nombre_servidor;
    }

    public Vector<String> getParametros() {
        return parametros;
    }

    public void setParametros(Vector<String> parametros) {
        this.parametros = parametros;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String servicioToString() {
        return "Servicio: " + nombre_servicio + ", parametros: " + parametros + ", retorno: " + retorno;
    }
}
