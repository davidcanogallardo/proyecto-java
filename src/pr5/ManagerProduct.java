package pr5;

import java.util.ArrayList;
import java.util.HashMap;

public class ManagerProduct<T> implements Persistable<T> {
    // Para poder almancenar Productos o Pack el HashMaps es de Object y se casteará
    // a Pack o Product
    private HashMap<Integer, T> productsHashMap = new HashMap<Integer, T>();

    // Interfaces
    @Override
    public Object add(Object obj) {
        if (obj != null) {
            if (obj instanceof Product) {
                if (obj instanceof Pack pack) {
                    if (productsHashMap.containsKey(pack.getIdProduct())) {
                        return null;
                    } else {
                        productsHashMap.put(pack.getIdProduct(), (T) obj);
                        return obj;
                    }
                } else {
                    if (productsHashMap.containsKey(((Product) obj).getIdProduct())) {
                        return null;
                    } else {
                        productsHashMap.put(((Product) obj).getIdProduct(), (T) obj);
                        return obj;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public T delete(T id) {
        if (productsHashMap.containsKey(id)) {
            Object prod = productsHashMap.get(id);
            productsHashMap.remove(id);
            return (T) prod;
        } else {
            return null;
        }
    }

    @Override
    public T get(T id) {
        if (productsHashMap.containsKey(id)) {
            return productsHashMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, T> getMap() {
        return productsHashMap;
    }

    public boolean addProduct(Product prod) {
        if (productsHashMap.containsKey(prod.getIdProduct())) {
            return false;
        } else {
            productsHashMap.put(prod.getIdProduct(), (T) prod);
            return true;
        }
    }

    public boolean addPack(Pack pack) {
        if (productsHashMap.containsKey(pack.getIdProduct())) {
            return false;
        } else {
            productsHashMap.put(pack.getIdProduct(), (T) pack);
            return true;
        }
    }

    // Busca un producto en el HashMap, con el segundo parámetro "type"
    // Puedes especificar si buscas un producto o pack
    public Object searchProduct(int idProduct, String type) {
        if (productsHashMap.containsKey(idProduct)) {
            if ("both".equals(type)) {
                return productsHashMap.get(idProduct);
            } else if (productsHashMap.get(idProduct) instanceof Pack pack && "pack".equals(type)) {
                return pack;
            } else if (!(productsHashMap.get(idProduct) instanceof Pack) && "product".equals(type)) {
                return (Product) productsHashMap.get(idProduct);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void modifyProduct(int idProduct, String name, int price, int stock) {
        Product prod = (Product) productsHashMap.get(idProduct);
        prod.setName(name);
        prod.setPrice(price);
        prod.setStock(stock);
    }

    public void modifyPack(int idProduct, String name, int price, float discount) {
        Pack pack = (Pack) productsHashMap.get(idProduct);
        pack.setName(name);
        pack.setPrice(price);
        pack.setDiscount(discount);
    }
}
