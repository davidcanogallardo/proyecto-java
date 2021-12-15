package project.Models;

import java.io.Serializable;

public class Client extends Person implements Serializable {
    public Client(Integer idPersona, String dni, String name, String surname, Address fullAddress) {
        super(idPersona, dni, name, surname, fullAddress);
    }
}
