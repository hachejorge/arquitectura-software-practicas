import java.rmi.Naming;

public class Cliente {
    public static void main ( String [] args ) {
        try {
            // Paso 1 - Obtener una referencia al objeto servidor creado anteriormente
            // Nombre del host servidor o su IP. Es dó nde se buscar á al objeto remoto
            String hostname = "172.20.10.4"; // se puede usar "IP:puerto "
            Collection server = (Collection) Naming.lookup ("//"+ hostname + "/MyCollection") ;
            // Paso 2 - Invocar remotamente los metodos del objeto servidor :

            // Obtener nombre y número de libros
            String nombreColeccion = server.name_of_collection();
            int numeroLibros = server.number_of_books();
            System.out.println("Nombre de la colección: " + nombreColeccion);
            System.out.println("Número de libros: " + numeroLibros);

            // Cambiar nombre de la colección
            server.name_of_collection("Nueva colección de aventuras");
            System.out.println("Nombre cambiado remotamente...");

            // Verificar que el cambio funcionó
            String nuevoNombre = server.name_of_collection();
            System.out.println("Nuevo nombre de la colección: " + nuevoNombre);

        }
        catch ( Exception ex ) {
            System.out.println ( ex );
        }
    }
}
