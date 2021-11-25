package pr5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class View {
    // Expresión regular para comprobar si un String se puede parsear a int
    private String numberRegex = "\\d+";

    private Scanner keyboard = new Scanner(System.in);

    private DAO<Client> DAOClient = new DAO<>();
    private DAO<Supplier> DAOSupplier = new DAO<>();
    private DAO<Product> DAOProduct = new DAO<>();

    // TODO variables de ayuda
    //TODO static
    //precio como loquesea
    public void run() {
        ArrayList<Integer> idProdList = new ArrayList<>();
        Product pr = new Product(1, "prod1", 12, 12);
        Product pr2 = new Product(2, "prod2", 1, 2);
        Pack pa = new Pack(idProdList, 5, 3, "pack1", 3);
        Pack pa2 = new Pack(idProdList, 4, 4, "pack2", 45);
        DAOProduct.add(pr);
        DAOProduct.add(pr2);
        DAOProduct.add(pa);
        DAOProduct.add(pa2);
        String menuOption;

        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Salir");
            System.out.println("[1] Productos");
            System.out.println("[2] Clientes");
            System.out.println("[3] Proveedores");
            System.out.print("Opción: ");
            menuOption = keyboard.nextLine();

            switch (menuOption) {
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
        } while (!"0".equals(menuOption));
    }

    /*--------------------------------------PRODUCTOS------------------------------------------*/
    public void menuProduct() {
        String menuOption;
        String answer;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir producto");
            System.out.println("[2] Buscar producto");
            System.out.println("[3] Modificar producto");
            System.out.println("[4] Borrar producto");
            System.out.println("[5] Mostrar todos los productos");
            System.out.print("Opción: ");
            menuOption = keyboard.nextLine();

            switch (menuOption) {
            // Add
            case "1":
                System.out.println("\nQué quieres añadir? (PRODUCTO/pack)");
                String tmp = keyboard.nextLine();
                if (tmp.equals("")) {
                    answer = "producto";
                } else {
                    answer = tmp;
                }

                if (answer.equalsIgnoreCase("producto")) {
                    addProduct(false);
                } else if (answer.equalsIgnoreCase("pack")) {
                    packMenu();
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
            // List all
            case "5":
                printClassObjects(DAOProduct);
                break;
            case "0":
                System.out.println("\n");
                break;
            default:
                deleteLine(9);
                System.out.println("Introduce una opción correcta!");
                break;
            }
        } while (!"0".equals(menuOption));
    }

    public void packMenu() {
        String idProduct;
        String idPack;
        // String name;
        // String price;
        Product prod;
        Pack pack;
        // String discount;
        Object obj;
        boolean inputIncorrect = false;
        boolean exists = false;

        int id;
        String name;
        int price;
        int stock;
        int discount;

        // Variable del menú
        String menuOption;
        System.out.println("\n");
        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir un pack");
            System.out.println("[2] Añadir un producto a un pack");
            System.out.print("Opción: ");
            menuOption = keyboard.nextLine();

            if ("1".equals(menuOption)) {
                addProduct(true);
            } else if ("2".equals(menuOption)) {
                // Bucle para añadir más productos
                System.out.println("");
                System.out.println("");
                // Obtener el producto que añadir
                // TODO ! pack exists
                do {
                    id = getExistingId(DAOProduct, "ID del producto que añadir al pack: ");
                } while (DAOProduct.get(id) instanceof Pack);

                prod = DAOProduct.get(id);

                System.out.println("");
                // Obtener el pack que añadir al producto
                // TODO ! !prod exists
                do {
                    id = getExistingId(DAOProduct, "ID del pack al que añadir el producto:");
                } while (!(DAOProduct.get(id) instanceof Pack));

                pack = (Pack) DAOProduct.get(id);

                if (pack.addProduct(prod.getId())) {
                    System.out.println("\nProducto añadido al pack!");
                } else {
                    System.out.println("No se ha podido añadir el producto al pack");
                }
            } else if (!"0".equals(menuOption)) {
                deleteLine(6);
                System.out.println("Introduce una opción correcta!");
            }
        } while (!"0".equals(menuOption));
    }

    public void addProduct(boolean isPack) {
        String idProduct;
        // String name;
        // String price;
        // String stock;
        Product prod;
        int discount;

        boolean exists = false;

        int id;
        String name;
        int price;
        int stock;

        System.out.println("Introduce las propiedades del producto:\n");
        // Pedir un ID de un producto que exista
        // TODO ! PROD exists done
        id = getFreeId(DAOProduct, "ID del producto: ");

        name = getString("Nombre: ", false);
        price = getInteger("Precio: ", false);
        if (!isPack) {
            stock = getInteger("Stock: ", false);
            prod = new Product(id, name, price, stock);
        } else {
            discount = getDiscount("Descuento (0-100): ", false);

            // lista de productos
            ArrayList<Integer> idProdList = new ArrayList<>();

            prod = new Pack(idProdList, discount, id, name, price);
        }

        if (DAOProduct.add(prod) != null) {
            System.out.println("\nProducto añadido!");
            System.out.println(prod.toString() + "\n");
        } else {
            System.out.println("El producto ya está añadido, prueba otro id\n ");
        }

    }

    public void searchProduct() {
        Product prod;
        int id;
        System.out.println();
        id = getInteger("ID del producto: ", false);
        prod = DAOProduct.get(id);
        if (prod != null) {
            System.out.println("\n" + prod.toString() + "\n");
        } else {
            System.out.println("No existe el producto\n");
        }
    }

    public void modifyProduct() {
        Product prod;
        boolean inputIncorrect = false;
        String idProduct;
        String name;
        Integer price;
        Integer stock;
        Integer discount;
        boolean exists = false;

        Integer id;
        System.out.println("");
        // Pedir un ID de un producto que exista
        // TODO ! prod exists
        id = getExistingId(DAOProduct, "ID del producto que quieres modificar: ");

        prod = DAOProduct.get(id);

        name = getString("Nombre [" + prod.getName() + "]: ", true);
        if (!name.equals("")) {
            prod.setName(name);
        }

        price = getInteger("Precio [" + prod.getPrice() + "]: ", true);
        if (price != null) {
            prod.setPrice(price);
        }

        if (prod instanceof Pack pack) {
            discount = getDiscount("Descuento (0-100)[" + pack.getDiscount() + "]: ", true);
            if (discount != null) {
                pack.setDiscount(discount);
            }

            DAOProduct.modify(pack);
            System.out.println("Producto modificado!\n");
            System.out.println(pack.toString() + "\n");
        } else {
            stock = getInteger("Stock [" + prod.getStock() + "]: ", true);
            if (stock != null) {
                prod.setStock(stock);
            }
            DAOProduct.modify(prod);
            System.out.println("Producto modificado!\n");
            System.out.println(prod.toString() + "\n");
        }
    }

    public void deleteProduct() {
        String idProduct;
        int id;
        Product prod;
        boolean exists;

        System.out.println("\nIntroduce el id del producto que quieres borrar:");
        // Pedir un ID de un producto que exista
        id = getInteger("ID del producto: ", false);
        if (DAOProduct.get(id) != null) {
            DAOProduct.delete(DAOProduct.get(id));
            System.out.println("\nProducto borrado!\n");
        } else {
            System.out.println("\nNo existe el producto\n");
        }
    }

    /*--------------------------------------PERSONAS------------------------------------------*/
    public void menuCliente() {
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
                printClassObjects(DAOClient);
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

    public void menuSupplier() {
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
                printClassObjects(DAOSupplier);
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
        // String id;
        int id;
        String dni;
        String name;
        String surname;

        Person person;
        boolean exists;

        System.out.println("\nIntroduce los datos de la persona:");
        // Pedir un ID de un producto que exista
        // TODO ! clie exists
        if (isClient) {
            id = getFreeId(DAOClient, "ID: ");
        } else {
            id = getFreeId(DAOSupplier, "ID: ");
        }

        // TODO dni
        dni = getString("DNI: ", false);
        name = getString("Nombre: ", false);
        surname = getString("Apellido: ", false);

        Address address = askAddress();

        // Según si es cliente o no se llama a una clase diferente
        if (isClient) {
            Client client = new Client(id, dni, name, surname, address);

            if (DAOClient.add(client) != null) {
                System.out.println("\nCliente añadido!\n");
                System.out.println(client.toString() + "\n");
            } else {
                System.out.println("El cliente ya existe, prueba otro id\n");
            }
        } else {
            Supplier supplier = new Supplier(id, dni, name, surname, address);

            if (DAOSupplier.add(supplier) != null) {
                System.out.println("\nProveedor añadido!\n");
                System.out.println(supplier.toString() + "\n");
            } else {
                System.out.println("El proveedor ya existe, prueba otro id\n");
            }
        }
    }

    public Address askAddress() {
        String zipRegex = "\\d{5}";

        String locality;
        String province;
        String zipCode;
        String address;

        System.out.println("Introduce los datos de la dirección:");
        do {
            locality = getString("Localidad: ", false);

            province = getString("Provincia: ", false);

            // TODO bucle CP
            zipCode = getString("Código Postal (número de 5 cifras): ", false);

            address = getString("Dirección: ", false);

            if (!zipCode.matches(zipRegex)) {
                System.out.println("El Código Postal tiene que ser un número de 5 cifras! Vuelve intentarlo\n");
            }
        } while (!zipCode.matches(zipRegex));

        return new Address(locality, province, zipCode, address);
    }

    private void searchPerson(boolean isClient) {
        int id;

        id = getInteger("\nID de la persona: ", false);

        // Según si es cliente o no busca en una clase u otra
        if (isClient) {
            Client client = (Client) DAOClient.get(id);
            if (client != null) {
                System.out.println("\n"+client.toString() + "\n");
            } else {
                System.out.println("\nNo existe el cliente\n");
            }
        } else {
            Supplier supplier = (Supplier) DAOSupplier.get(id);
            if (supplier != null) {
                System.out.println("\n"+supplier.toString() + "\n");
            } else {
                System.out.println("\nNo existe el proveedor\n");
            }
        }
    }

    private void modifyPerson(boolean isClient) {
        int id;
        String dni;
        String name;
        String surname;
        Person person;
        boolean exists;

        // Pedir un ID de un producto que exista
        if (isClient) {
            person = DAOClient.get(getExistingId(DAOClient, "ID: "));
        } else {
            person = DAOSupplier.get(getExistingId(DAOSupplier, "ID: "));
        }
        // TODO ! clie exists
        dni = getString("DNI [" + person.getDni() + "]: ", true);
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

        String zipRegex = "\\d{5}";

        String locality;
        String province;
        String zipCode;
        String address;
        Address add;
        add = person.getFullAddress();

        System.out.println("Introduce los datos de la dirección:");
        locality = getString("Localidad [" + add.getLocality() + "]: ", true);
        if (!locality.equals("")) {
            add.setLocality(locality);
        }

        province = getString("Provincia [" + add.getProvince() + "]: ", true);
        if (!province.equals("")) {
            add.setProvince(province);
        }

        // TODO bucle CP
        zipCode = getString("Código Postal [" + add.getZipCode() + "]: ", true);
        if (!zipCode.equals("")) {
            add.setZipCode(zipCode);
        }

        address = getString("Dirección [" + add.getAddress() + "]: ", true);
        if (!address.equals("")) {
            add.setAddress(address);
        }

        person.setFullAddress(add);

        if (isClient) {
            DAOClient.modify((Client) person);
            System.out.println("Datos actualizados:");
            System.out.println(person.toString() + "\n");
        } else {
            DAOSupplier.modify((Supplier) person);
            System.out.println("Datos actualizados:");
            System.out.println(person.toString() + "\n");
        }
    }

    private void deletePerson(boolean isClient) {
        int id;
        Person person;
        boolean exists;

        System.out.println("");
        // Pedir un ID de un producto que exista
        id = getInteger("ID del cliente que borrar: ", false);

        if (isClient) {
            DAOClient.delete(DAOClient.get(id));
        } else {
            DAOSupplier.delete(DAOSupplier.get(id));
        }
        System.out.println("\nCliente borrado!\n");
    }
    /**********************************
     * UTILS
     *******************************************/
    private void printClassObjects(DAO p) {
        System.out.println("");
        HashMap<Integer, Object> hashMap = p.getMap();
        for (Object values : hashMap.values()) {
            System.out.println(values.toString() + "\n");
        }
    }

    public void deleteLine(int num) {
        int count = num;
        System.out.print(String.format("\033[%dA", count)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    public Integer getInteger(String question, boolean returnNull) {
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

    public String getString(String question, boolean returnEmpty) {
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

    public boolean isNumber(String num) {
        return num.matches(numberRegex);
    }

    public Integer getDiscount(String question, boolean returnEmpty) {
        Integer num;
        do {
            num = getInteger(question, returnEmpty);
        } while ((num > 100) && !returnEmpty);
        return num;
    }

    public int getFreeId(DAO dao, String question) {
        int id;
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

    public int getExistingId(DAO dao, String question) {
        int id;
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
    // TODO mejor nombre
    // public String optionalString(String type, String question) {
    // String result = "";
    // if (type.equals("int")) {
    // do {
    // System.out.print(question);
    // result = keyboard.nextLine();
    // if (!isInt(result) && !result.equals("")) {
    // deleteLine();
    // }
    // } while (!isInt(result) && !result.equals(""));
    // } else if (type.equals("String")) {
    // System.out.print(question);
    // result = keyboard.nextLine();
    // }

    // return result;
    // }
}
