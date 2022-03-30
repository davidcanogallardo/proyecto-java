package project.classes;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class Supplier extends Person implements Serializable {
    public Supplier(Integer id, String dni, String name, String surname, Address fullAddress) {
        super(id, dni, name, surname, fullAddress);
    }
    public Supplier(Integer id, String dni, String name, String surname, Address fullAddress, LinkedHashSet<String> phoneNumber) {
        super(id, dni, name, surname, fullAddress, phoneNumber);
    }
}
