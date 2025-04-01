public class ServidorJL15_01 extends UnicastRemoteObject implements ServidorJL15 {

    public int ejecutar_suma(int a, int b) {
        return a + b;
    }

    public static void main ( String args []) {
    
        String host = ""; // Dirección del servidor
        String remote_broker = ""; // Dirección del broker

        // Por defecto , RMI usa el puerto 1099
        try {
            // Crear objeto remoto
            ServidorJL15_01 server = new ServidorJL15_01() ;
            System.out.println (" Creado !") ;
            // Registrar el objeto remoto
            Naming.rebind ("//" + host + "/MyServerJL15_01", server ) ;
            System.out.println (" Estoy registrado !") ;

            // Crear objeto remoto
            Broker broker = new Broker();
            System.out.println ("Creado!") ;
            // Registrar el objeto remoto
            Naming.rebind ("//" + remote_broker + "/MyBrokerJL15", broker ) ;
            System.out.println ("Estoy registrado!") ;

            broker.registrar_servidor("ServidorJL15_01", host);
            Servicio service = new Servicio("ServidorJL15_01", "ejecutar_suma", {"int", "int"}, "int")
            broker.alta_servicio("suma", service);

        }
        catch ( Exception ex ) {
            System.out.println ( ex );
        }
    }
}