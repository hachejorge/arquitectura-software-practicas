/* Módulo main con ejemplo del funcionamiento del programa
 * Autores: Jorge Hernández y Laura Hernández
 */

package Main;

import CryptoBros.CryptoBroConcrete;
import CryptoCurrencies.CryptoCurrencyConcrete;
import Utils.CryptoEventType;

public class Main {
    public static void main(String[] args) {

        // Se crean 2 crypto monedas
        CryptoCurrencyConcrete bitcoin = new CryptoCurrencyConcrete("Bitcoin", 10);
        CryptoCurrencyConcrete etherium = new CryptoCurrencyConcrete("Etherium", 5);
        
        // Se crean 4 crypto bros
        CryptoBroConcrete LauraBro = new CryptoBroConcrete("Laura");
        CryptoBroConcrete MartaBro = new CryptoBroConcrete("Marta");
        CryptoBroConcrete JorgeBro = new CryptoBroConcrete("Jorge");
        CryptoBroConcrete MarioBro = new CryptoBroConcrete("Mario");

        // Se establecen los eventos que atienden en bitcoin
        bitcoin.addCryptoBro(LauraBro, CryptoEventType.SUBIDA);
        bitcoin.addCryptoBro(JorgeBro, CryptoEventType.BAJADA);
        bitcoin.addCryptoBro(MarioBro, CryptoEventType.ALL);

        // Se establecen los eventos que atienden en etherium        
        etherium.addCryptoBro(LauraBro, CryptoEventType.SUBIDA);
        etherium.addCryptoBro(MartaBro, CryptoEventType.SUBIDA);
        etherium.addCryptoBro(MarioBro, CryptoEventType.BAJADA);

        // Aumenta el precio de bitcoin, 
        // Mario(ALL) recibe notificación
        // Laura(SUBIDA) recibe notificación
        bitcoin.setPrice(20);

        // Disminuye el precio de bitcoin, 
        // Mario(ALL) recibe notificación
        // Jorge(BAJADA) recibe notificación
        bitcoin.setPrice(15);
        
        // Aumenta el precio de etherium, 
        // Laura(SUBIDA) recibe notificación
        // Marta(SUBIDA) recibe notificación
        etherium.setPrice(30);
    }
}