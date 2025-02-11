public class Bitcoin extends CryptoCurrency {

    Bitcoin(double _price){
        name = "BitCoin";
        price = _price;
    }

    public void setPrice(double newPrice){
        price = newPrice;
    }

    @Override
    public double getPrice(){
        return price;
    }
}
