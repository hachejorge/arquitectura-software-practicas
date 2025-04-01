import java.util.Vector;

public class Servicio {
    private String nombre_servicio;
    private String nombre_servidor;
    private String funcion;
    private Vector<T> parametros;
    private String retorno;

    // Constructor por defecto
    public Servicio() {
        this.parametros = new Vector<>();
    }

    // Constructor con parámetros
    public Servicio(String nombre_servicio, String nombre_servidor String funcion, Vector<T> parametros, String retorno) {
        this.nombre_servicio = nombre_servicio;
        this.nombre_servidor = nombre_servidor;
        this.funcion = funcion;
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

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Vector<T> getParametros() {
        return parametros;
    }

    public void setParametros(Vector<T> parametros) {
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
