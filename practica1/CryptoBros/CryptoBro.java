package CryptoBros;
import java.util.HashMap;
import java.util.Map;

import CryptoCurrencies.CryptoCurrency;
import Utils.CryptoEventType;

public abstract class CryptoBro {

    protected String name;
    protected Map<String, Double> cryptoPrices = new HashMap<>();

    public void addCryptoCurrency(CryptoCurrency moneda) {
        cryptoPrices.put(moneda.getName(), moneda.getPrice()); 
    }

    public void removeCryptoCurrency(CryptoCurrency c) {
        System.out.println(this.name +  "Se ha eliminado la moneda " + c.getName());
        cryptoPrices.remove(c.getName()); 
    }

    public abstract void update(CryptoEventType event, CryptoCurrency c, double lastPrice);
}