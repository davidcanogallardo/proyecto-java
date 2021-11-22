package pr5;

import java.util.HashMap;

public class ManagerSupplier<T> implements Persistable<T> {
    private HashMap<Integer, T> supplierHashMap = new HashMap<Integer, T>();

    @Override
    public T add(Object obj, int id) {
        if (obj != null) {
            if (obj instanceof Supplier supplier) {
                if (supplierHashMap.containsKey(supplier.getIdPersona())) {
                    return null;
                } else {
                    supplierHashMap.put(supplier.getIdPersona(), (T) supplier);
                    return (T)obj;
                }
            }
        }

        return null;
    }

    @Override
    public T delete(T id) {
        if (supplierHashMap.containsKey(id)) {
            T supplier = supplierHashMap.get(id);
            supplierHashMap.remove(id);
            return supplier;
        } else {
            return null;
        }
    }

    @Override
    public T get(T id) {
        if (supplierHashMap.containsKey(id)) {
            return supplierHashMap.get(id);
        } else {
            return null;
        }
    }

    @Override
    public HashMap<Integer, T> getMap() {
        return new HashMap<>(supplierHashMap);
    }

    public void modifySupplier(Integer idPersona, String dni, String name, String surname, Address address) {
        Supplier supplier = (Supplier) get((T) idPersona);
        // Guardo la dirección del cliente
        Address addSupplier = supplier.getFullAddress();

        // Solo cambia el atributo si su valor no es " "
        if (!" ".equals(dni)) {
            supplier.setDni(dni);
        }

        if (!" ".equals(name)) {
            supplier.setName(name);
        }

        if (!" ".equals(surname)) {
            supplier.setSurname(surname);
        }

        // Para cambiar los datos de la dirección utilizo
        // la variable que he obtenido antes
        if (!" ".equals(address.getLocality())) {
            addSupplier.setLocality(address.getLocality());
        }

        if (!" ".equals(address.getProvince())) {
            addSupplier.setProvince(address.getProvince());
        }

        if (!" ".equals(address.getZipCode())) {
            addSupplier.setZipCode(address.getZipCode());
        }

        if (!" ".equals(address.getAddress())) {
            addSupplier.setAddress(address.getAddress());
        }
        // Finalmente cambio la dirección por la dirección modificada
        supplier.setFullAddress(addSupplier);
    }

    public boolean supplierExists(int id) {
        return supplierHashMap.containsKey(id);
    }
}