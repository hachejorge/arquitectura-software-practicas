package Main;

import CryptoBros.AllCryptoBro;
import CryptoBros.OnlyDownCryptoBro;
import CryptoBros.OnlyUpCryptoBro;
import CryptoCurrencies.Bitcoin;
import CryptoCurrencies.Etherium;

public class Main {
    public static void main(String[] args) {

        Bitcoin bit = new Bitcoin(10);
        Etherium et = new Etherium(5);
        
        OnlyUpCryptoBro LauraBro = new OnlyUpCryptoBro("Laura");
        OnlyUpCryptoBro MartaBro = new OnlyUpCryptoBro("Marta");
        OnlyDownCryptoBro JorgeBro = new OnlyDownCryptoBro("Jorge");
        AllCryptoBro MarioBro = new AllCryptoBro("Mario");

        bit.addCryptoBro(LauraBro);
        bit.addCryptoBro(JorgeBro);
        bit.addCryptoBro(MarioBro);
        
        et.addCryptoBro(LauraBro);
        et.addCryptoBro(MartaBro);
        et.addCryptoBro(MarioBro);

        bit.setPrice(20);

        bit.setPrice(15);
        
        et.setPrice(30);
    }
}