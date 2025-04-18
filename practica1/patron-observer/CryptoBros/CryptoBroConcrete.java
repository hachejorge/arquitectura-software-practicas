/* Implementación de la clase específica CryptoBroConcrete
 * Autores: Jorge Hernández y Laura Hernández
 */

package CryptoBros;

import CryptoCurrencies.CryptoCurrency;
import Utils.CryptoEventType;

public class CryptoBroConcrete extends CryptoBro {

    // Constructor 
    public CryptoBroConcrete(String _name){
        name = _name;
    }

    // Metodo para actualizar el valor de una moneda
    @Override
    public void update(CryptoEventType event, CryptoCurrency c, double lastPrice) {
        // Buscar la moneda en la lista de criptomonedas
        cryptoPrices.put(c.getName(), c.getPrice());
        
        System.out.println(this.name + " (Escucha evento de " + event +  ")" + ": El precio anterior de " + c.getName() + " era " + lastPrice + " y el nuevo precio es " + c.getPrice());

    }
}
