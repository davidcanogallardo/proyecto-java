package pr5;

import java.util.HashMap;

public class ManagerClient<T> implements Persistable<T> {
    private HashMap<Integer, T> clientHashMap = new HashMap<Integer, T>();

    @Override
    public T add(T cli) {
        if (cli != null && cli instanceof Client client) {
            if (clientHashMap.containsKey(client.getIdPersona())) {
                return null;
            } else {
                clientHashMap.put(client.getIdPersona(), (T)client);
                return cli;
            }
        }
        return null;
    }

    @Override
    public T delete(T id) {
        if (clientHashMap.containsKey(id)) {
            Client c = (Client)clientHashMap.get(id);
            clientHashMap.remove(id);
            return (T) c;
        } else {
            return null;
        }
    }

    @Override
    public T get(T id) {
        if (clientHashMap.containsKey(id)) {
            return (T) clientHashMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, T> getMap() {
        return new HashMap<>(clientHashMap);
    }

    public void modifyClient(Integer idPersona, String dni, String name, String surname, Address address) {
        Client client = (Client) get((T)idPersona);
        // Guardo la dirección del cliente
        Address addClient = client.getFullAddress();
        // Solo cambia el atributo si su valor no es " "
        if (!" ".equals(dni)) {
            client.setDni(dni);
        }

        if (!" ".equals(name)) {
            client.setName(name);
        }

        if (!" ".equals(surname)) {
            client.setSurname(surname);
        }

        // Para cambiar los datos de la dirección utilizo
        // la variable que he obtenido antes
        if (!" ".equals(address.getLocality())) {
            addClient.setLocality(address.getLocality());
        }

        if (!" ".equals(address.getProvince())) {
            addClient.setProvince(address.getProvince());
        }

        if (!" ".equals(address.getZipCode())) {
            addClient.setZipCode(address.getZipCode());
        }

        if (!" ".equals(address.getAddress())) {
            addClient.setAddress(address.getAddress());
        }
        // Finalmente cambio la dirección por la dirección modificada
        client.setFullAddress(addClient);
    }

    public boolean clientExists(int id) {
        return clientHashMap.containsKey(id);
    }

}
