public class Main {
    public static void main(String[] args) {

        Bitcoin bit = new Bitcoin(10);
        Etherium et = new Etherium(5);
        OnlyUpCryptoBro LauraBro = new OnlyUpCryptoBro("Laura");
        OnlyUpCryptoBro MartaBro = new OnlyUpCryptoBro("Marta");
        OnlyDownCryptoBro JorgeBro = new OnlyDownCryptoBro("Jorge");

        bit.addCryptoBro(LauraBro);
        bit.addCryptoBro(JorgeBro);
        et.addCryptoBro(LauraBro);
        et.addCryptoBro(MartaBro);

        bit.setPrice(20);
        bit.setPrice(15);
        et.setPrice(30);
    }
}