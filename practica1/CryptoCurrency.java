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
        cryptobros.add(bro);
        
    }

    public void removeCryptoBro(CryptoBro bro) {
        cryptobros.remove(bro);
    }

    public void notifyCryptoBros(CryptoEventType event) {
        for (CryptoBro bro : cryptobros) {
            bro.update(event, name);
        }
    }

    abstract double getPrice();
}


