/* Implementación de la clase abstracta CryptoCurrencies
 * Autores: Jorge Hernández y Laura Hernández
 */

package CryptoCurrencies;
import java.util.Map;
import java.util.HashMap;

import CryptoBros.CryptoBro;
import Utils.CryptoEventType;

// Clase abstracta que actúa sujeto
public abstract class CryptoCurrency {
    protected Map<CryptoBro, CryptoEventType> allBros = new HashMap<>();

    protected String name;
    protected double price;

    public String getName(){
        return name;
    }

    // Añade a su registro el crypto bro con su tipo de evento que atiende
    public void addCryptoBro(CryptoBro bro, CryptoEventType event) {
        allBros.put(bro, event);
        bro.addCryptoCurrency(this);   
    }

    // Elimina el crypto bro de su registro
    public void removeCryptoBro(CryptoBro bro) {
        bro.removeCryptoCurrency(this); 
        allBros.remove(bro);
    }

    // Elimina todos los crypto bros de su registro
    public void removeAllCryptoBros(){
        for (Map.Entry<CryptoBro,CryptoEventType> entry : allBros.entrySet()) {
            this.removeCryptoBro(entry.getKey());
        }
    }

    // Noficia a los crypto bros mediante el método update, si se ha producido el tipo de evento que atendían
    public void notifyCryptoBros(CryptoEventType event, double precioAnterior) {
        for (Map.Entry<CryptoBro,CryptoEventType> entry : allBros.entrySet()) {
            if (entry.getValue().equals(event)) 
                entry.getKey().update(event, this, precioAnterior);
        }
    }
   
    // Establece el precio de una crypto moneda que acaba notifando a los crypto bros junto al tipo de evento
    public void setPrice(double newPrice){
        double lastPrice = price;
        price = newPrice;
        notifyCryptoBros(CryptoEventType.ALL, lastPrice);
        if (lastPrice < price) {
            notifyCryptoBros(CryptoEventType.SUBIDA, lastPrice);
        } else if (lastPrice > price){
            notifyCryptoBros(CryptoEventType.BAJADA, lastPrice);
        }
    }

    public double getPrice(){
        return price;
    }

}


