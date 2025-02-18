package CryptoBros;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import CryptoCurrencies.CryptoCurrency;
import Utils.CryptoEventType;


import java.util.List;

public abstract class CryptoBro {

    protected String name;
    protected Map<String, Double> cryptoPrices = new HashMap<>();

    protected List<CryptoCurrency> currencies = new ArrayList<>();

    public void addCryptoCurrency(CryptoCurrency moneda) {
        //System.out.println("dentro addCryptoCurrency");
        currencies.add(moneda);
        cryptoPrices.put(moneda.getName(), moneda.getPrice()); 
    }

    public void removeCryptoCurrency(CryptoCurrency moneda) {
        currencies.remove(moneda);
        cryptoPrices.remove(moneda.getName()); 
    }

    public abstract void update(CryptoEventType event, String name);
}