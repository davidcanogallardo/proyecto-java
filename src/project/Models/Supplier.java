package project.Models;

import java.io.Serializable;

public class Supplier extends Person implements Serializable {
    public Supplier(Integer idPersona, String dni, String name, String surname, Address fullAddress) {
        super(idPersona, dni, name, surname, fullAddress);
    }
}
