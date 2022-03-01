package project.DAOs;

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
import project.Classes.Supplier;

import project.Models.Identificable;
import project.Models.Persistable;


public class SupplierDAO implements Persistable<Supplier>, Serializable {
    private HashMap<Integer, Supplier> hashMap = new HashMap<>();

    public Supplier add(Supplier obj) {
        if (hashMap.containsKey(obj.getId())) {
            return null;
        } else {
            hashMap.put(obj.getId(), obj);
            return obj;
        }
    }

    public Supplier delete(Supplier obj) {
        if (hashMap.containsKey(obj.getId())) {
            hashMap.remove(obj.getId());
            return obj;
        } else {
            return null;
        }
    }

    public Supplier get(Integer id) {
        if (hashMap.containsKey(id)) {
            return (Supplier) hashMap.get(id);
        } else {
            return null;
        }
    }

    public HashMap<Integer, Supplier> getMap() {
        return new HashMap<>(hashMap);
    }

    public void modify(Supplier obj) {
        hashMap.replace(obj.getId(), get(obj.getId()), obj);
    }

    public void save(String file) throws IOException {
        System.out.println("guardando dao proveedor...");
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
                this.hashMap = (HashMap<Integer, Supplier>) ois.readObject();
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
