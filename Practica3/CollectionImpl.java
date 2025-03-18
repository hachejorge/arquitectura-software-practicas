import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CollectionImpl extends UnicastRemoteObject implements Collection {
    
    // Private member variables
    private int m_number_of_books ;
    private String m_name_of_collection ;
    
    // Constructor
    public CollectionImpl (String name, int booksNumber) throws RemoteException {
        super () ; // Llama al constructor de UnicastRemoteObject
        m_name_of_collection = name;
        m_number_of_books = booksNumber;
    }

    public int number_of_books () throws RemoteException {
        return m_number_of_books;
    }

    public String name_of_collection() throws RemoteException {
        return m_name_of_collection;
    }

    public void name_of_collection(String _new_value) throws RemoteException {
        m_name_of_collection = _new_value;
    }

    
    public static void main ( String args []) {
    
        // Nombre o IP del host donde reside el objeto servidor
        
        String hostName = "localhost"; // se puede usar IPhostremoto : puerto "

        // Por defecto , RMI usa el puerto 1099
        try {
            // Crear objeto remoto
            CollectionImpl obj = new CollectionImpl ("El diario de Greg", 20) ;
            System.out.println (" Creado !") ;
            // Registrar el objeto remoto
            Naming.rebind ("//" + hostName + "/MyCollection", obj ) ;
            System.out.println (" Estoy registrado !") ;
        }
        catch ( Exception ex ) {
            System.out.println ( ex );
        }
    }
}
