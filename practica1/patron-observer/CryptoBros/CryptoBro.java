/* Implementación de la clase abstracta CryptoBro
 * Autores: Jorge Hernández y Laura Hernández
 */

package CryptoBros;
import java.util.HashMap;
import java.util.Map;

import CryptoCurrencies.CryptoCurrency;
import Utils.CryptoEventType;

// Clase que actua como observer en el patrón 
public abstract class CryptoBro {

    protected String name; // Nombre del CryptoBro

    protected Map<String, Double> cryptoPrices = new HashMap<>(); // Mapa con String siendo el nombre de una crypto moneda y su precio

    // Añade una crypto moneda y su precio a su registro
    public void addCryptoCurrency(CryptoCurrency moneda) { 
        cryptoPrices.put(moneda.getName(), moneda.getPrice()); 
    }

    // Elimina un crypto moneda de su registro
    public void removeCryptoCurrency(CryptoCurrency c) {
        System.out.println(this.name +  "Se ha eliminado la moneda " + c.getName());
        cryptoPrices.remove(c.getName()); 
    }

    public abstract void update(CryptoEventType event, CryptoCurrency c, double lastPrice);
}