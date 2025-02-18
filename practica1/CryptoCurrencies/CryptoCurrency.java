package CryptoCurrencies;
import java.util.Map;
import java.util.HashMap;

import CryptoBros.CryptoBro;
import Utils.CryptoEventType;



// Clase abstracta que actúa comallBroso sujeto
public abstract class CryptoCurrency {
    protected Map<CryptoBro, CryptoEventType> allBros = new HashMap<>();

    protected String name;
    protected double price;

    public String getName(){
        return name;
    }

    public void addCryptoBro(CryptoBro bro, CryptoEventType event) {
        allBros.put(bro, event);
        bro.addCryptoCurrency(this);   
    }

    public void removeCryptoBro(CryptoBro bro) {
        bro.removeCryptoCurrency(this); 
        allBros.remove(bro);
    }

    public void removeAllCryptoBros(){
        for (Map.Entry<CryptoBro,CryptoEventType> entry : allBros.entrySet()) {
            entry.getKey().removeCryptoCurrency(this);
            allBros.remove(entry.getKey());
        }
    }

    public void notifyCryptoBros(CryptoEventType event, double precioAnterior) {
        for (Map.Entry<CryptoBro,CryptoEventType> entry : allBros.entrySet()) {
            if (entry.getValue().equals(event)) 
                entry.getKey().update(event, this, precioAnterior);
        }
    }
   
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


