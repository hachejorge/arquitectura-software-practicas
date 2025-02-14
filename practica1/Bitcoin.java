public class Bitcoin extends CryptoCurrency {

    Bitcoin(double _price){
        name = "BitCoin";
        price = _price;
    }

    /*@Override
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

    @Override
    public double getPrice(){
        return price;
    }*/
}
