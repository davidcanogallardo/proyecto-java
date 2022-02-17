package project.Models;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class Client extends Person implements Serializable {
    public Client(Integer id, String dni, String name, String surname, Address fullAddress) {
        super(id, dni, name, surname, fullAddress);
    }
    public Client(Integer id, String dni, String name, String surname, Address fullAddress, LinkedHashSet<String> phoneNumber) {
        super(id, dni, name, surname, fullAddress, phoneNumber);
    }
}
