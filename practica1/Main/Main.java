package Main;

import CryptoBros.CryptoBroConcrete;
import CryptoCurrencies.CryptoCurrencyConcrete;
import Utils.CryptoEventType;

public class Main {
    public static void main(String[] args) {

        CryptoCurrencyConcrete bitcoin = new CryptoCurrencyConcrete("Bitcoin", 10);
        CryptoCurrencyConcrete etherium = new CryptoCurrencyConcrete("Etherium", 5);
        
        CryptoBroConcrete LauraBro = new CryptoBroConcrete("Laura");
        CryptoBroConcrete MartaBro = new CryptoBroConcrete("Marta");
        CryptoBroConcrete JorgeBro = new CryptoBroConcrete("Jorge");
        CryptoBroConcrete MarioBro = new CryptoBroConcrete("Mario");

        bitcoin.addCryptoBro(LauraBro, CryptoEventType.SUBIDA);
        bitcoin.addCryptoBro(JorgeBro, CryptoEventType.BAJADA);
        bitcoin.addCryptoBro(MarioBro, CryptoEventType.ALL);
        
        etherium.addCryptoBro(LauraBro, CryptoEventType.SUBIDA);
        etherium.addCryptoBro(MartaBro, CryptoEventType.SUBIDA);
        etherium.addCryptoBro(MarioBro, CryptoEventType.BAJADA);

        bitcoin.removeAllCryptoBros();

        bitcoin.setPrice(20);

        bitcoin.setPrice(15);
        
        etherium.setPrice(30);
    }
}