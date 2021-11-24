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
            // TODO opcion 0 que no diga lo de opcion incorrecta
            default:
                // deleteLine();
                System.out.println("Introduce una opción correcta!");
                break;
            }
            deleteLine();
        } while (!"0".equals(menuOption));
    }

    /*--------------------------------------PRODUCTOS------------------------------------------*/
    public void menuProduct() {
        String menuOption;
        String answer;

        do {
            System.out.println("\nElige una opción:");
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
                // TODO bucle al preguntar
                System.out.println("Qué quiere añadir? (PRODUCTO/pack)");
                String tmp = keyboard.nextLine();
                if (tmp.equals("")) {
                    answer = "producto";
                } else {
                    answer = tmp;
                }

                if (answer.equalsIgnoreCase("producto")) {
                    addProduct();
                } else if (answer.equalsIgnoreCase("pack")) {
                    addPack();
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
            default:
                // deleteLine();
                break;
            }
        } while (!"0".equals(menuOption));
    }

    public void addProduct() {
        String idProduct;
        // String name;
        // String price;
        // String stock;
        Product prod;

        boolean exists = false;

        int id;
        String name;
        int price;
        int stock;

        System.out.println("Introduce las propiedades del producto:");
        // Pedir un ID de un producto que exista
        do {
            id = getInt("ID del producto: ", false);
            prod = DAOProduct.get(id);
            if (prod != null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("Ya existe un producto con ese ID!");
            } else {
                exists = true;
            }
        } while (!exists);

        name = getString("Nombre: ", false);
        price = getInt("Precio: ", false);
        stock = getInt("Stock: ", false);

        prod = new Product(id, name, price, stock);

        if (DAOProduct.add(prod) != null) {
            System.out.println("\nProducto añadido!");
            System.out.println(prod.toString() + "\n");
        } else {
            System.out.println("El producto ya está añadido, prueba otro id\n ");
        }
    }

    public void addPack() {
        // TODO addPerson
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

        do {
            System.out.println("\nElige una opción:");
            System.out.println("[0] Volver");
            System.out.println("[1] Añadir un pack");
            System.out.println("[2] Añadir un producto a un pack");
            System.out.print("Opción: ");
            menuOption = keyboard.nextLine();

            if ("1".equals(menuOption)) {
                System.out.println("\nIntroduce las propiedades del pack:");

                // Pedir un ID de un producto que exista
                do {
                    id = getInt("ID del producto: ", false);
                    prod = DAOProduct.get(id);
                    if (prod != null) {
                        exists = false;
                        deleteLine();
                        deleteLine();
                        System.out.println("Ya existe un producto con ese ID!");
                    } else {
                        exists = true;
                    }
                } while (!exists);

                name = getString("Nombre: ", false);
                price = getInt("Precio: ", false);
                stock = getInt("Stock: ", false);
                discount = getInt("Descuento (0-100): ", false);

                // lista de productos
                ArrayList<Integer> idProdList = new ArrayList<>();

                // TODO descuento formato
                pack = new Pack(idProdList, discount, id, name, price);

                if (DAOProduct.add(pack) != null) {
                    System.out.println("\nPack añadido!");
                    System.out.println(pack.toString() + "\n");
                } else {
                    System.out.println("\nYa existe un pack con ese ID\n");
                }

            } else if ("2".equals(menuOption)) {
                // Bucle para añadir más productos
                System.out.println("");
                System.out.println("");
                // Obtener el producto que añadir
                do {
                    id = getInt("ID del producto que añadir al pack: ", false);
                    obj = DAOProduct.get(id);
                    if (obj == null) {
                        inputIncorrect = true;
                        deleteLine();
                        deleteLine();
                        System.out.println("No existe!");
                    } else if (obj instanceof Pack) {
                        inputIncorrect = true;
                        deleteLine();
                        deleteLine();
                        System.out.println("No es un producto!");
                    } else {
                        inputIncorrect = false;
                    }
                } while (inputIncorrect);

                prod = DAOProduct.get(id);

                System.out.println("");
                System.out.println("");
                // Obtener el pack que añadir al producto
                do {
                    id = getInt("ID del pack al que añadir el producto:", false);
                    // TODO poner el get en la creacion
                    obj = DAOProduct.get(id);
                    if (obj == null) {
                        inputIncorrect = true;
                        deleteLine();
                        deleteLine();
                        System.out.println("No existe!");
                    } else if (!(obj instanceof Pack)) {
                        inputIncorrect = true;
                        deleteLine();
                        deleteLine();
                        System.out.println("No es un pack!");
                    } else {
                        inputIncorrect = false;
                    }
                } while (inputIncorrect);

                pack = (Pack) DAOProduct.get(id);

                if (pack.addProduct(prod.getId())) {
                    System.out.println("Producto añadido al pack!");
                } else {
                    System.out.println("No se ha podido añadir el producto al pack");
                }
            }
        } while (!"0".equals(menuOption));
    }

    public void searchProduct() {
        Product prod;
        int id;

        id = getInt("ID: ", false);
        prod = DAOProduct.get(id);
        if (prod != null) {
            System.out.println(prod.toString() + "\n");
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
        System.out.println("");
        // Pedir un ID de un producto que exista
        do {
            id = getInt("ID del producto: ", false);
            prod = DAOProduct.get(id);
            if (prod == null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("No existe!");
            } else {
                exists = true;
            }
        } while (!exists);

        prod = DAOProduct.get(id);

        name = getString("Nombre [" + prod.getName() + "]: ", true);
        if (!name.equals("")) {
            prod.setName(name);
        }
        price = getInt("Precio [" + prod.getPrice() + "]: ", true);
        if (price == null) {
            prod.setPrice(price);
        }

        if (prod instanceof Pack pack) {
            discount = getInt("Descuento (0-100)[" + pack.getDiscount() + "]: ", true);
            if (discount == null) {
                pack.setDiscount(discount);
            }

            DAOProduct.modify(pack);
        } else {
            stock = getInt("Stock [" + prod.getStock() + "]: ", true);
            if (stock == null) {
                prod.setStock(stock);
            }
            DAOProduct.modify(prod);
        }
    }

    public void deleteProduct() {
        String idProduct;
        int id;
        Product prod;
        boolean exists;

        System.out.println("Introduce el id del producto que quieres borrar:");
        // Pedir un ID de un producto que exista
        id = getInt("ID del producto: ", false);
        if (DAOProduct.get(id) != null) {
            DAOProduct.delete(DAOProduct.get(id));
            System.out.println("Producto borrado!\n");
        } else {
            System.out.println("No existe el producto\n");
        }
    }

    /*--------------------------------------PERSONAS------------------------------------------*/
    public void menuCliente() {
        String option;

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
            }
        } while (!"0".equals(option));
    }

    public void menuSupplier() {
        String option;

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

        // Pedir un ID de un producto que exista
        do {
            id = getInt("ID: ");
            if (isClient) {
                person = DAOClient.get(id);
            } else {
                person = DAOSupplier.get(id);
            }
            if (person != null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("Ya existe una persona con ese ID!");
            } else {
                exists = true;
            }
        } while (!exists);

        // TODO dni
        dni = getString("DNI: ");
        name = getString("Nombre: ");
        surname = getString("Apellido: ");

        Address address = askAddress();

        // Según si es cliente o no se llama a una clase diferente
        if (isClient) {
            Client client = new Client(id, dni, name, surname, address);

            if (DAOClient.add(client) != null) {
                System.out.println("Cliente añadido!\n");
            } else {
                System.out.println("El cliente ya existe, prueba otro id\n");
            }
        } else {
            Supplier supplier = new Supplier(id, dni, name, surname, address);

            if (DAOSupplier.add(supplier) != null) {
                System.out.println("Proveedor añadido!\n");
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
            locality = getString("Localidad: ");

            province = getString("Provincia: ");

            // TODO bucle CP
            zipCode = getString("Código Postal (número de 5 cifras): ");

            address = getString("Dirección: ");

            if (!zipCode.matches(zipRegex)) {
                System.out.println("El Código Postal tiene que ser un número de 5 cifras! Vuelve intentarlo\n");
            }
        } while (!zipCode.matches(zipRegex));

        return new Address(locality, province, zipCode, address);
    }

    private void searchPerson(boolean isClient) {
        int id;

        id = getInt("ID: ");

        // Según si es cliente o no busca en una clase u otra
        if (isClient) {
            Client client = (Client) DAOClient.get(id);
            if (client != null) {
                System.out.println(client.toString() + "\n");
            } else {
                System.out.println("No existe el cliente\n");
            }
        } else {
            Supplier supplier = (Supplier) DAOSupplier.get(id);
            if (supplier != null) {
                System.out.println(supplier.toString() + "\n");
            } else {
                System.out.println("No existe el proveedor\n");
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
        do {
            id = getInt("ID : ");
            if (isClient) {
                person = DAOClient.get(id);
            } else {
                person = DAOSupplier.get(id);
            }
            if (person == null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("No existe!");
            } else {
                exists = true;
            }
        } while (!exists);
        dni = optionalString("String", "DNI [" + person.getDni() + "]: ");
        name = optionalString("String", "Nombre [" + person.getName() + "]: ");
        surname = optionalString("String", "Apellidos [" + person.getSurname() + "]: ");

        Address address = askAddress();

        if (DAOClient.exists(id) || DAOSupplier.exists(id)) {
            // Según si es cliente o no modifica los datos en clase correspondiente
            if (isClient) {
                Client c = new Client(id, dni, name, surname, address);
                DAOClient.modify(c);
                System.out.println("Datos actualizados:");
                System.out.println(c.toString() + "\n");
            } else {
                Supplier s = new Supplier(id, dni, name, surname, address);
                DAOSupplier.modify(s);
                System.out.println("Datos actualizados:");
                System.out.println(s.toString() + "\n");
            }
        } else {
            System.out.println("El cliente no existe");
        }
    }

    private void deletePerson(boolean isClient) {
        int id;
        Person person;
        boolean exists;

        System.out.println("Introduce el id del cliente");

        // Pedir un ID de un producto que exista
        do {
            id = getInt("ID: ");
            if (isClient) {
                person = DAOClient.get(id);
            } else {
                person = DAOSupplier.get(id);
            }
            if (person != null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("Ya existe una persona con ese ID!");
            } else {
                exists = true;
            }
        } while (!exists);

        if (isClient) {
            DAOClient.delete(DAOClient.get(id));
        } else {
            DAOSupplier.delete(DAOClient.get(id));
        }
        System.out.println("bORRADO1");
    }

    private void printClassObjects(DAO p) {
        HashMap<Integer, Object> hashMap = p.getMap();
        for (Object values : hashMap.values()) {
            System.out.println(values.toString() + "\n");
        }
    }

    // TODO numero de lineas
    public void deleteLine() {
        int count = 1;
        System.out.print(String.format("\033[%dA", count)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    public Integer getInt(String question, boolean returnEmpty) {
        String num;
        do {
            System.out.print(question);
            num = keyboard.nextLine();
            if (returnEmpty && num.equals("")) {
                return null;
            } else {
                if (!isInt(num)) {
                    deleteLine();
                }
            }
        } while (!isInt(num));

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
                    deleteLine();
                }
            }
        } while (string.equals(""));
        return string;
    }

    public boolean isInt(String num) {
        return num.matches(numberRegex);
    }

    // TODO mejor nombre
    public String optionalString(String type, String question) {
        String result = "";
        if (type.equals("int")) {
            do {
                System.out.print(question);
                result = keyboard.nextLine();
                if (!isInt(result) && !result.equals("")) {
                    deleteLine();
                }
            } while (!isInt(result) && !result.equals(""));
        } else if (type.equals("String")) {
            System.out.print(question);
            result = keyboard.nextLine();
        }

        return result;
    }
}
