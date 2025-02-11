import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

abstract class CryptoBro {
    protected Map<String, Double> cryptoPrices = new HashMap<>();

    protected List<CryptoCurrency> currencies = new ArrayList<>();

    abstract void update(CryptoEventType event, String name);
}