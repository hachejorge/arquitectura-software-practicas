package MOM;

import java.rmi.Remote;
import java.io.Serializable;
import java.rmi.RemoteException;

/*public class Consumer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public Consumer(String name) {
        this.name = name;
    }

    public void notify(Message message) {
        // Logic to handle the message
        System.out.println("[" + this.name + "] receives: " + message.getContent());
    }
}*/

public interface Consumer extends Remote, Serializable {
    void procesarMensaje(String mensaje) throws RemoteException;
}
