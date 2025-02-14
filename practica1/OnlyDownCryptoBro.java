public class OnlyDownCryptoBro extends CryptoBro {

    OnlyDownCryptoBro(String _name){
        name = _name;
    }

    @Override
    void update(CryptoEventType event, String name) {

        //System.out.println("dentro update");
        // Obtener el precio anterior usando el mapa
        double precioAnterior = cryptoPrices.get(name);

        // Buscar la moneda en la lista de criptomonedas
        for (CryptoCurrency c : currencies) {
            if (c.getName().equals(name)) {
                // Actualizar el precio en el mapa
                cryptoPrices.put(c.getName(), c.getPrice());
                break; // Salir del bucle una vez que se actualiza la moneda
            }
        }

        // Verificar si el evento es una subida
        if (event == CryptoEventType.BAJADA) {
            System.out.println("OnlyDownCryptoBro " + this.name + ": El precio anterior de " + name + " era " + precioAnterior + " y el nuevo precio es " + cryptoPrices.get(name));
        }
    }
}
