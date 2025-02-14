import java.util.ArrayList;
import java.util.List;



// Clase abstracta que actúa como sujeto
abstract class CryptoCurrency {
    protected List<CryptoBro> cryptobros = new ArrayList<>();
    protected String name;
    protected double price;

    public String getName(){
        return name;
    }

    public void addCryptoBro(CryptoBro bro) {
        //System.out.println("dentro addCryptoBro");
        cryptobros.add(bro);
        bro.addCryptoCurrency(this);   
    }

    public void removeCryptoBro(CryptoBro bro) {
        cryptobros.remove(bro);
        bro.removeCryptoCurrency(this); 
    }

    public void notifyCryptoBros(CryptoEventType event) {
        for (CryptoBro bro : cryptobros) {
            bro.update(event, name);
        }
    }
   
    public void setPrice(double newPrice){
        //System.out.println("dentro setPrice");
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


