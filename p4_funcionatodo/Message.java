import java.io.Serializable;
import java.util.UUID;

/**
 * Clase que representa un mensaje dentro del sistema.
 * Cada mensaje contiene un contenido (texto) y un identificador único.
 */
public class Message implements Serializable {
    // Contenido del mensaje (texto que envía el productor)
    private final String contenido;
    // Identificador único del mensaje, generado automáticamente
    private final String id;

    // Constructor: genera un ID único y almacena el contenido
    public Message(String contenido) {
        this.id = UUID.randomUUID().toString();
        this.contenido = contenido;
    }

    // Devuelve el contenido del mensaje
    public String getContenido() {
        return contenido;
    }

    // Devuelve el identificador único del mensaje
    public String getId() {
        return id;
    }
}
