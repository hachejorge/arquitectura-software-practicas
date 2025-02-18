package CryptoCurrencies;
import java.util.ArrayList;
import java.util.List;

import CryptoBros.CryptoBro;
import Utils.CryptoEventType;



// Clase abstracta que actúa como sujeto
public abstract class CryptoCurrency {
    protected List<CryptoBro> allBros = new ArrayList<>();

    protected String name;
    protected double price;

    public String getName(){
        return name;
    }

    public void addCryptoBro(CryptoBro bro) {
        allBros.add(bro);
        bro.addCryptoCurrency(this);   
    }

    public void removeCryptoBro(CryptoBro bro) {
        allBros.remove(bro);
        bro.removeCryptoCurrency(this); 
    }

    public void notifyCryptoBros(CryptoEventType event) {
        for (CryptoBro bro : allBros) {
            bro.update(event, name);
        }
    }
   
    public void setPrice(double newPrice){
        double lastPrice = price;
        price = newPrice;
        if (lastPrice < price) {
            notifyCryptoBros(CryptoEventType.SUBIDA);
        } else if (lastPrice > price){
            notifyCryptoBros(CryptoEventType.BAJADA);
        }
    }

    public double getPrice(){
        return price;
    }

}


