package project.Models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsDAO<Product extends Identificable> implements Persistable<Product>, Serializable {
    private HashMap<Integer, Product> hashMap = new HashMap<>();

    public Product add(Product obj) {
        if (hashMap.containsKey(obj.getId())) {
            return null;
        } else {
            hashMap.put(obj.getId(), obj);
            return obj;
        }
    }

    public Product delete(Product obj) {
        if (hashMap.containsKey(obj.getId())) {
            hashMap.remove(obj.getId());
            return obj;
        } else {
            return null;
        }
    }

    public Product get(Integer id) {
        if (hashMap.containsKey(id)) {
            return hashMap.get(id);
        } else {
            return null;
        }
    }

    public HashMap<Integer, Product> getMap() {
        return new HashMap<>(hashMap);
    }

    public void modify(Product obj) {
        hashMap.replace(obj.getId(), get(obj.getId()), obj);
    }

    public void save(String file) throws IOException {
        System.out.println("guardando...");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.hashMap);
        oos.close();
    }

    public void load(String file) throws IOException {
        System.out.println("cargando....");
        FileInputStream fis = new FileInputStream(file);
        try {
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                this.hashMap = (HashMap<Integer, Product>) ois.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ois.close();
        } catch (Exception EOFException) {
            //TODO: handle exception
        }

    }


}
