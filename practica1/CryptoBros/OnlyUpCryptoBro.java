package CryptoBros;

import CryptoCurrencies.CryptoCurrency;
import Utils.CryptoEventType;

public class OnlyUpCryptoBro extends CryptoBro {

    public OnlyUpCryptoBro(String _name){
        name = _name;
    }

    @Override
    public void update(CryptoEventType event, String name) {

        //System.out.println("dentro update");
        // Obtener el precio anterior usando el mapa
        double precioAnterior = cryptoPrices.get(name);

        // Buscar la moneda en la lista de criptomonedas
        for (CryptoCurrency c : currencies) {
            if (c.getName().equals(name)) {
                // Actualizar el precio en el mapa
                cryptoPrices.put(c.getName(), c.getPrice());
                break; // Salir del bucle una vez que se actualiza la moneda
            }
        }

        // Verificar si el evento es una subida
        if (event == CryptoEventType.SUBIDA) {
            System.out.println("OnlyUpCryptoBro " + this.name  + ": El precio anterior de " + name + " era " + precioAnterior + " y el nuevo precio es " + cryptoPrices.get(name));
        }
    }
}
