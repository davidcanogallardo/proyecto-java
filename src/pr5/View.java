package pr5;

import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class View {
    private Scanner keyboard = new Scanner(System.in);

    private DAO<Client> daoClient = new DAO<>();
    private DAO<Supplier> daoSupplier = new DAO<>();
    private DAO<Product> daoProduct = new DAO<>();

    private Product prod;
    private Pack pack;
    private int id;
    private String name;
    private Integer discount;
    private double price;
    private Integer stock;
    private String surname;
    private String dni;
    private Person person;

    private static Logger logger = Logger.getLogger(View.class.getName());
    
    public void run() throws SecurityException, IOException, ClassNotFoundException, FileNotFoundException {
        FileInputStream fis = new FileInputStream("comanda_rebuda.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Product prod2 = (Product) ois.readObject();
        
        prod2.getId();

        ois.close();

        String option;
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Salir");
            System.out.println("[1] Productos");
            System.out.println("[2] Clientes");
            System.out.println("[3] Proveedores");
            System.out.print("Opción: ");
            option = keyboard.nextLine();

            switch (option) {
                // Productos
                case "1":
                    menuProduct();
                    break;
                // Clientes
                case "2":
                    menuCliente();
                    break;
                // Proveedores
                case "3":
                    menuSupplier();
                    break;
                default:
                    deleteLine(7);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    /*--------------------------------------PRODUCTOS------------------------------------------*/
    private void menuProduct() {
        String answer;
        String option;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir producto");
            System.out.println("[2] Buscar producto");
            System.out.println("[3] Modificar producto");
            System.out.println("[4] Borrar producto");
            System.out.println("[5] Gestionar stock producto");
            System.out.println("[6] Mostrar todos los productos");
            System.out.print("Opción: ");
            option = keyboard.nextLine();

            switch (option) {
                // Add
                case "1":
                    System.out.println("");
                    do {
                        option = getString("Qué quieres añadir? (producto/pack) [producto]", true);
                        if (option.equals("")) {
                            answer = "producto";
                        } else {
                            answer = option;
                        }
                    } while (!answer.equalsIgnoreCase("pack") && !answer.equalsIgnoreCase("producto"));

                    if (answer.equalsIgnoreCase("producto")) {
                        addProduct(false);
                    } else if (answer.equalsIgnoreCase("pack")) {
                        menuPack();
                    }
                    break;
                // Search
                case "2":
                    searchProduct();
                    break;
                // Modify
                case "3":
                    modifyProduct();
                    break;
                // Delete
                case "4":
                    deleteProduct();
                    break;
                // Stock
                case "5":
                    stockGestor();
                    break;
                // List all
                case "6":
                    printObjects(daoProduct);
                    break;
                case "0":
                    System.out.println("\n");
                    break;
                default:
                    deleteLine(9);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void menuPack() {
        String option;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir un pack");
            System.out.println("[2] Añadir un producto a un pack");
            System.out.print("Opción: ");
            option = keyboard.nextLine();

            if ("1".equals(option)) {
                addProduct(true);
            } else if ("2".equals(option)) {
                System.out.println("");
                System.out.println("");
                // Obtener el producto que añadir
                do {
                    id = getExistingId(daoProduct, "ID del producto que añadir al pack: ");
                    if (daoProduct.get(id) instanceof Pack) {
                        deleteLine(2);
                        System.out.println("El producto no puede ser un pack");
                    }
                } while (daoProduct.get(id) instanceof Pack);

                prod = daoProduct.get(id);

                System.out.println("");
                // Obtener el pack que añadir al producto
                do {
                    id = getExistingId(daoProduct, "ID del pack al que añadir el producto: ");
                    if (!(daoProduct.get(id) instanceof Pack)) {
                        deleteLine(2);
                        System.out.println("Elige un pack");
                    }
                } while (!(daoProduct.get(id) instanceof Pack));

                pack = (Pack) daoProduct.get(id);

                if (pack.addProduct(prod.getId())) {
                    System.out.println("\nProducto añadido al pack!");
                } else {
                    System.out.println("No se ha podido añadir el producto al pack");
                }
            } else if (!"0".equals(option)) {
                deleteLine(6);
                System.out.println("Introduce una opción correcta!");
            }
        } while (!"0".equals(option));
    }

    private void addProduct(boolean isPack) {
        System.out.println("Introduce las propiedades del producto:\n");
        // Pedir un ID de un producto que exista
        id = getFreeId(daoProduct, "ID del producto: ");

        name = getString("Nombre: ", false);
        price = getDouble("Precio: ", false);
        //Según si quiere añadir un pack o producto pide diferentes propiedades
        if (!isPack) {
            stock = getInteger("Stock: ", false);
            prod = new Product(id, name, price, stock);
        } else {
            discount = getDiscount("Descuento (0-100): ", false);
            // lista de productos de un pack (vacía por defecto)
            ArrayList<Integer> productList = new ArrayList<>();

            prod = new Pack(productList, discount, id, name, price);
        }

        if (daoProduct.add(prod) != null) {
            System.out.println("\nProducto añadido!");
            System.out.println(prod.toString() + "\n");
        } else {
            System.out.println("El producto ya está añadido, prueba otro id\n ");
        }

    }

    private void searchProduct() {
        System.out.println();
        id = getInteger("ID del producto: ", false);
        prod = daoProduct.get(id);
        if (prod != null) {
            System.out.println("\n" + prod.toString() + "\n");
        } else {
            System.out.println("No existe el producto\n");
        }
    }

    private void modifyProduct() {
        System.out.println("");
        // Pedir un ID de un producto que exista
        id = getExistingId(daoProduct, "ID del producto que quieres modificar: ");

        prod = daoProduct.get(id);

        //Reemplaza las propiedades del producto 
        //si el usuario introduce algo 
        name = getString("Nombre [" + prod.getName() + "]: ", true);
        if (!name.equals("")) {
            prod.setName(name);
        }

        price = getDouble("Precio [" + prod.getPrice() + "]: ", true);
        if (price != 0) {
            prod.setPrice(price);
        }

        if (prod instanceof Pack pack) {
            discount = getDiscount("Descuento (0-100)[" + pack.getDiscount() + "]: ", true);
            if (discount != null) {
                pack.setDiscount(discount);
            }

            daoProduct.modify(pack);
            System.out.println("Producto modificado!\n");
            System.out.println(pack.toString() + "\n");
        } else {
            stock = getInteger("Stock [" + prod.getStock() + "]: ", true);
            if (stock != null) {
                prod.setStock(stock);
            }
            daoProduct.modify(prod);
            System.out.println("Producto modificado!\n");
            System.out.println(prod.toString() + "\n");
        }
    }

    private void deleteProduct() {
        System.out.println("\nIntroduce el id del producto que quieres borrar:");
        // Pedir un ID de un producto que exista
        id = getInteger("ID del producto: ", false);
        if (daoProduct.get(id) != null) {
            daoProduct.delete(daoProduct.get(id));
            System.out.println("\nProducto borrado!\n");
        } else {
            System.out.println("\nNo existe el producto\n");
        }
    }

    private void stockGestor() {
        String option;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir stock a un producto");
            System.out.println("[2] Quitar stock a un producto");
            System.out.print("Opción: ");
            option = keyboard.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Elige una opción:");
                    System.out.println("[0] Volver");
                    System.out.println("[1] Añadir stock manualmente");
                    System.out.println("[2] Añadir stock mediante albarán");
                    option = keyboard.nextLine();
                    switch (option) {
                        case "1":
                            id = getExistingId(daoProduct, "ID de un producto: ");
                            stock = getInteger("Stock que añadir: ", false);
                            prod = daoProduct.get(id);
                            prod.putStock(stock);
                            break;
                        case "2":

                            break;
                    }
                    break;
                case "2":
                    id = getExistingId(daoProduct, "ID de un producto: ");
                    stock = getInteger("Stock que quitar: ", false);
                    try {
                        prod.takeStock(stock);
                    } catch (StockInsuficientException e) {
                        System.out.println("No puedes quitar tanto stock al producto!");
                        // e.printStackTrace();
                    }
                    System.out.println("");
                    break;
                case "0":
                    System.out.println("\n");
                    break;
                default:
                    deleteLine(6);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    /*--------------------------------------PERSONAS------------------------------------------*/
    private void menuCliente() {
        String option;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir cliente");
            System.out.println("[2] Buscar cliente");
            System.out.println("[3] Modificar cliente");
            System.out.println("[4] Borrar cliente");
            System.out.println("[5] Mostrar todos los clientes");
            System.out.print("Opción: ");

            option = keyboard.nextLine();

            switch (option) {
                case "1":
                    addPerson(true);
                    break;
                case "2":
                    searchPerson(true);
                    break;
                case "3":
                    modifyPerson(true);
                    break;
                case "4":
                    deletePerson(true);
                    break;
                case "5":
                    printObjects(daoClient);
                    break;
                case "0":
                    System.out.println("\n");
                    break;
                default:
                    deleteLine(9);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void menuSupplier() {
        String option;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir proveedor");
            System.out.println("[2] Buscar proveedor");
            System.out.println("[3] Modificar proveedor");
            System.out.println("[4] Borrar proveedor");
            System.out.println("[5] Mostrar todos los proveedor");
            System.out.print("Opción: ");

            option = keyboard.nextLine();

            switch (option) {
                case "1":
                    addPerson(false);
                    break;
                case "2":
                    searchPerson(false);
                    break;
                case "3":
                    modifyPerson(false);
                    break;
                case "4":
                    deletePerson(false);
                    break;
                case "5":
                    printObjects(daoSupplier);
                    break;
                case "0":
                    System.out.println("\n");
                    break;
                default:
                    deleteLine(9);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void addPerson(boolean isClient) {
        System.out.println("\nIntroduce los datos de la persona:");
        // Pedir un ID de un producto que exista
        if (isClient) {
            id = getFreeId(daoClient, "ID: ");
        } else {
            id = getFreeId(daoSupplier, "ID: ");
        }

        dni = getValidDni("DNI: ", false);
        name = getString("Nombre: ", false);
        surname = getString("Apellido: ", false);

        Address address = askAddress();

        // Según si es cliente o no se llama a una clase diferente
        if (isClient) {
            Client client = new Client(id, dni, name, surname, address);

            if (daoClient.add(client) != null) {
                System.out.println("\nCliente añadido!\n");
                System.out.println(client.toString() + "\n");
            } else {
                System.out.println("El cliente ya existe, prueba otro id\n");
            }
        } else {
            Supplier supplier = new Supplier(id, dni, name, surname, address);

            if (daoSupplier.add(supplier) != null) {
                System.out.println("\nProveedor añadido!\n");
                System.out.println(supplier.toString() + "\n");
            } else {
                System.out.println("El proveedor ya existe, prueba otro id\n");
            }
        }
    }

    private Address askAddress() {
        String locality;
        String province;
        String zipCode;
        String address;

        System.out.println("Introduce los datos de la dirección:");
        locality = getString("Localidad: ", false);

        province = getString("Provincia: ", false);

        zipCode = getZipCode("Código Postal (número de 5 cifras): ", false);

        address = getString("Dirección: ", false);

        return new Address(locality, province, zipCode, address);
    }

    private void searchPerson(boolean isClient) {
        id = getInteger("\nID de la persona: ", false);

        // Según si es cliente o no busca en una clase u otra
        if (isClient) {
            Client client = daoClient.get(id);
            if (client != null) {
                System.out.println("\n" + client.toString() + "\n");
            } else {
                System.out.println("\nNo existe el cliente\n");
            }
        } else {
            Supplier supplier = daoSupplier.get(id);
            if (supplier != null) {
                System.out.println("\n" + supplier.toString() + "\n");
            } else {
                System.out.println("\nNo existe el proveedor\n");
            }
        }
    }

    private void modifyPerson(boolean isClient) {
        // Pedir un ID de un producto que exista
        if (isClient) {
            person = daoClient.get(getExistingId(daoClient, "ID: "));
        } else {
            person = daoSupplier.get(getExistingId(daoSupplier, "ID: "));
        }

        dni = getValidDni("DNI [" + person.getDni() + "]: ", true);
        if (!dni.equals("")) {
            person.setDni(dni);
        }
        name = getString("Nombre [" + person.getName() + "]: ", true);
        if (!name.equals("")) {
            person.setName(name);
        }
        surname = getString("Apellidos [" + person.getSurname() + "]: ", true);
        if (!surname.equals("")) {
            person.setSurname(surname);
        }

        String locality;
        String province;
        String zipCode;
        String address;
        Address addr = person.getFullAddress();

        System.out.println("Introduce los datos de la dirección:");
        locality = getString("Localidad [" + addr.getLocality() + "]: ", true);
        if (!locality.equals("")) {
            addr.setLocality(locality);
        }

        province = getString("Provincia [" + addr.getProvince() + "]: ", true);
        if (!province.equals("")) {
            addr.setProvince(province);
        }

        zipCode = getZipCode("Código Postal [" + addr.getZipCode() + "]: ", true);
        if (!zipCode.equals("")) {
            addr.setZipCode(zipCode);
        }

        address = getString("Dirección [" + addr.getAddress() + "]: ", true);
        if (!address.equals("")) {
            addr.setAddress(address);
        }

        person.setFullAddress(addr);

        if (isClient) {
            daoClient.modify((Client) person);
            System.out.println("Datos actualizados:");
            System.out.println(person.toString() + "\n");
        } else {
            daoSupplier.modify((Supplier) person);
            System.out.println("Datos actualizados:");
            System.out.println(person.toString() + "\n");
        }
    }

    private void deletePerson(boolean isClient) {
        System.out.println("");
        // Pedir un ID de un producto que exista
        id = getInteger("ID del cliente que borrar: ", false);

        
        if (isClient) {
            if (daoClient.get(id) == null) {
                System.out.println("Ese id no corresponde a ningún cliente");
            } else {
                daoClient.delete(daoClient.get(id));
                System.out.println("\nCliente borrado!\n");
            }
        } else {
            if (daoSupplier.get(id) == null) {
                System.out.println("Ese id no corresponde a ningún proveedor");
            } else {
                daoSupplier.delete(daoSupplier.get(id));
                System.out.println("\nCliente borrado!\n");
            }
        }
    }

    private void printObjects(DAO p) {
        System.out.println("");
        HashMap<Integer, Object> hashMap = p.getMap();
        for (Object values : hashMap.values()) {
            System.out.println(values.toString() + "\n");
        }
    }

    /******************************** UTILS **************************************/
    private void deleteLine(int linesToDelete) {
        System.out.print(String.format("\033[%dA", linesToDelete)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    private Integer getInteger(String question, boolean returnNull) {
        String num;
        do {
            System.out.print(question);
            num = keyboard.nextLine();
            if (returnNull && num.equals("")) {
                return null;
            } else {
                if (!isNumber(num)) {
                    deleteLine(1);
                }
            }
        } while (!isNumber(num));

        return Integer.parseInt(num);
    }

    private double getDouble(String question, boolean returnNull) {
        boolean invalidDouble = true;
        double value = 0;
        String doubleStr;
        System.out.println("\n");
        do {
            System.out.print(question);
            doubleStr = keyboard.nextLine();
            try {
                invalidDouble = false;
                if (returnNull && doubleStr.equals("")) {
                    return 0;
                }
                value = Double.parseDouble(doubleStr);
            } catch (Exception InputMismatchException) {
                deleteLine(3);
                logger.warning("Input error");
                // System.out.println("Precio incorrecto");
                invalidDouble = true;
            }
        } while (invalidDouble);

        return value;
    }

    private String getString(String question, boolean returnEmpty) {
        String string;
        do {
            System.out.print(question);
            string = keyboard.nextLine();
            if (returnEmpty && string.equals("")) {
                return "";
            } else {
                if (string.equals("")) {
                    deleteLine(1);
                }
            }
        } while (string.equals(""));
        return string;
    }

    private boolean isNumber(String num) {
        String numberRegex = "\\d{1,10}";
        return num.matches(numberRegex);
    }

    private Integer getDiscount(String question, boolean returnEmpty) {
        Integer num;
        do {
            num = getInteger(question, returnEmpty);
            if ((num > 100) && !returnEmpty) {
                deleteLine(1);
            }
        } while ((num > 100) && !returnEmpty);
        return num;
    }

    private int getFreeId(DAO dao, String question) {
        Object obj;
        boolean exists;
        do {
            id = getInteger(question, false);
            obj = dao.get(id);
            if (obj != null) {
                exists = true;
                deleteLine(2);
                System.out.println("Ya existe ese ID!");
            } else {
                exists = false;
            }
        } while (exists);

        return id;
    }

    private int getExistingId(DAO dao, String question) {
        Object obj;
        boolean exists;
        do {
            id = getInteger(question, false);
            obj = dao.get(id);
            if (obj != null) {
                exists = true;
            } else {
                exists = false;
                deleteLine(2);
                System.out.println("No existe ese ID!");
            }
        } while (!exists);

        return id;
    }

    private String getValidDni(String question, boolean returnEmpty) {
        String dniRegex = "\\d{8}[a-zA-Z]{1}";
        System.out.println("");
        do {
            do {
                dni = getString(question, returnEmpty);
                if (dni.equals("") && returnEmpty) {
                    return "";
                }
                if (!dni.matches(dniRegex)) {
                    deleteLine(2);
                    System.out.println("Introduce un DNI con 7 números y una letra");
                }
            } while (!dni.matches(dniRegex));
            if (!isDniLetterValid(dni.substring(8), Integer.parseInt(dni.substring(0, 8)))) {
                deleteLine(2);
                System.out.println("Introduce un DNI correcto");
            }
        } while (!isDniLetterValid(dni.substring(8), Integer.parseInt(dni.substring(0, 8))));

        return dni;
    }

    private boolean isDniLetterValid(String letter, int num) {
        String[] letters = {
                "t", "r", "w", "a", "g", "m", "y", "f", "p", "d", "x", "b",
                "n", "j", "z", "s", "q", "v", "h", "l", "c", "k", "e"
        };
        return letter.equalsIgnoreCase(letters[num % 23]);
    }

    private String getZipCode(String question, boolean returnEmpty) {
        String zipRegex = "\\d{5}";
        String zipCode;
        System.out.println("");
        do {
            zipCode = getString(question, returnEmpty);
            if (zipCode.equals("") && returnEmpty) {
                return "";
            }
            if (!zipCode.matches(zipRegex)) {
                deleteLine(3);
                System.out.println("El código postal es un número de 5 cifras");
            }
        } while (!zipCode.matches(zipRegex));

        return zipCode;
    }
}
