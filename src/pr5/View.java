package pr5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class View {
    // Expresión regular para comprobar si un String se puede parsear a int
    private String numberRegex = "\\d+";

    private Scanner keyboard = new Scanner(System.in);

    private DAO<Client> managerClient = new DAO<>();
    private DAO<Person> managerSupplier = new DAO<>();
    private DAO<Product> managerProduct = new DAO<>();

    public void run() {
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
                printClassObjects(managerProduct);
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
            id = getInt("ID del producto: ");
            prod = managerProduct.get(id);
            if (prod != null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("Ya existe un producto con ese ID!");
            } else {
                exists = true;
            }
        } while (!exists);

        name = getString("Nombre: ");
        price = getInt("Precio: ");
        stock = getInt("Stock: ");

        prod = new Product(id, name, price, stock);

        if (managerProduct.add(prod) != null) {
            System.out.println("\nProducto añadido!");
            System.out.println(prod.toString() + "\n");
        } else {
            System.out.println("El producto ya está añadido, prueba otro id\n ");
        }
    }

    public void addPack() {
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
                    id = getInt("ID del producto: ");
                    prod = managerProduct.get(id);
                    if (prod != null) {
                        exists = false;
                        deleteLine();
                        deleteLine();
                        System.out.println("Ya existe un producto con ese ID!");
                    } else {
                        exists = true;
                    }
                } while (!exists);
        
                name = getString("Nombre: ");
                price = getInt("Precio: ");
                stock = getInt("Stock: ");
                discount = getInt("Descuento (0-100): ");

                // lista de productos
                ArrayList<Integer> idProdList = new ArrayList<>();

                // TODO descuento formato
                pack = new Pack(idProdList, discount, id, name, price);

                if (managerProduct.add(pack) != null) {
                    System.out.println("\nPack añadido!");
                    System.out.println(pack.toString() + "\n");
                } else {
                    System.out.println("\nYa existe un pack con ese ID\n");
                }

            } else if ("2".equals(menuOption)) {
                //Bucle para añadir más productos
                System.out.println("");
                System.out.println("");
                // Obtener el producto que añadir
                do {
                    id = getInt("ID del producto que añadir al pack: ");
                    obj = managerProduct.get(id);
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

                prod = managerProduct.get(id);

                System.out.println("");
                System.out.println("");
                // Obtener el pack que añadir al producto
                do {
                    id = getInt("ID del pack al que añadir el producto:");
                    obj = managerProduct.get(id);
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

                pack = (Pack) managerProduct.get(id);

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

        id = getInt("ID: ");
        prod = managerProduct.get(id);
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
        String price;
        String stock;
        String discount;
        boolean exists = false;

        int id;
        System.out.println("");
        System.out.println("");
        // Pedir un ID de un producto que exista
        do {
            id = getInt("ID del producto: ");
            prod = managerProduct.get(id);
            if (prod == null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("No existe!");
            } else {
                exists = true;
            }
        } while (!exists);


        prod = managerProduct.get(id);

        name = optionalString("String", "Nombre [" + prod.getName() + "]: ");
        if (!name.equals("")) {
            prod.setName(name);
        }
        price = optionalString("int", "Precio [" + prod.getPrice() + "]: ");
        if (!price.equals("")) {
            prod.setPrice(Integer.parseInt(price));
        }

        if (prod instanceof Pack pack) {
            discount = optionalString("int", "Descuento (0-100)[" + pack.getDiscount() + "]: ");
            if (!discount.equals("")) {
                pack.setDiscount(Integer.parseInt(discount));
            }

            managerProduct.modify(pack);
        } else {
            stock = optionalString("int", "Stock [" + prod.getStock() + "]: ");
            if (!stock.equals("")) {
                prod.setStock(Integer.parseInt(stock));
            }
            managerProduct.modify(prod);
        }
    }

    public void deleteProduct() {
        String idProduct;
        int id;
        Product prod;

        System.out.println("Introduce el id del producto que quieres borrar:");
        id = getInt("ID del producto: ");
        prod = managerProduct.get(id);
        if (managerProduct.delete(prod) != null) {
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
                printClassObjects(managerClient);
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
                printClassObjects(managerSupplier);
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
            id = getInt("ID del producto: ");
            if (isClient) {
                person = managerClient.get(id);
            } else {
                person = managerSupplier.get(id);
            }
            if (person != null) {
                exists = false;
                deleteLine();
                deleteLine();
                System.out.println("Ya existe un producto con ese ID!");
            } else {
                exists = true;
            }
        } while (!exists);

        //TODO dni
        dni = getString("DNI: ");
        name = getString("Nombre: ");
        surname = getString("Apellido: ");

        Address address = askAddress();

        // Según si es cliente o no se llama a una clase diferente
        if (isClient) {
            Client client = new Client(id, dni, name, surname, address);

            if (managerClient.add(client) != null) {
                System.out.println("Cliente añadido!\n");
            } else {
                System.out.println("El cliente ya existe, prueba otro id\n");
            }
        } else {
            Supplier supplier = new Supplier(id, dni, name, surname, address);

            if (managerSupplier.add(supplier) != null) {
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
            locality = getString("localidad:");

            province = getString("Provincia: ");

            //TODO bucle CP
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

        id = getInt("ID del producto: ");

        // Según si es cliente o no busca en una clase u otra
        if (isClient) {
            Client client = (Client) managerClient.get(id);
            if (client != null) {
                System.out.println(client.toString() + "\n");
            } else {
                System.out.println("No existe el cliente\n");
            }
        } else {
            Supplier supplier = (Supplier) managerSupplier.get(id);
            if (supplier != null) {
                System.out.println(supplier.toString() + "\n");
            } else {
                System.out.println("No existe el proveedor\n");
            }
        }
    }

    private void modifyPerson(boolean isClient) {
        String id;
        String dni;
        String name;
        String surname;

        System.out.println("Introduce el id");
        id = keyboard.nextLine();

        if (id.matches(numberRegex)) {
            System.out.println("\nSi no quieres modificar un campo introduce un espacio (menos el Código Postal)");
            System.out.println("Introduce el nuevo DNI");
            dni = keyboard.nextLine();

            System.out.println("Introduce el nuevo nombre");
            name = keyboard.nextLine();

            System.out.println("Introduce el nuevo apellido");
            surname = keyboard.nextLine();

            Address address = askAddress();

            if (managerClient.objExists(Integer.parseInt(id)) || managerSupplier.objExists(Integer.parseInt(id))) {
                // Según si es cliente o no modifica los datos en clase correspondiente
                if (isClient) {
                    // managerClient.modifyClient(Integer.parseInt(id), dni, name, surname,
                    // address);
                    System.out.println("Datos actualizados:");
                    System.out.println(managerClient.get(Integer.parseInt(id)).toString() + "\n");
                } else {
                    // managerSupplier.modifySupplier(Integer.parseInt(id), dni, name, surname,
                    // address);
                    System.out.println("Datos actualizados:");
                    System.out.println(managerSupplier.get(Integer.parseInt(id)).toString() + "\n");
                }

            } else {
                System.out.println("El cliente no existe");
            }
        } else {
            System.out.println("El id solo puedes ser un número\n");
        }
    }

    private void deletePerson(boolean isClient) {
        String id;
        // Según si es cliente o no lo borra de la clase correspondiente
        if (isClient) {
            System.out.println("Introduce el id del cliente");
            id = keyboard.nextLine();

            if (id.matches(numberRegex)) {
                if (managerClient.delete(Integer.parseInt(id)) != null) {
                    System.out.println("Cliente borrado!\n");
                } else {
                    System.out.println("El cliente no existe, prueba otro id\n");
                }
            } else {
                System.out.println("El id solo puedes ser un número\n");
            }

        } else {
            System.out.println("Introduce el id del proveedor");
            id = keyboard.nextLine();

            if (id.matches(numberRegex)) {
                if (managerSupplier.delete(Integer.parseInt(id)) != null) {
                    System.out.println("Proveedor borrado!\n");
                } else {
                    System.out.println("El proveedor no existe, prueba otro id\n");
                }
            } else {
                System.out.println("El id solo puedes ser un número\n");
            }
        }
    }

    private void printClassObjects(DAO p) {
        HashMap<Integer, Object> hashMap = p.getMap();
        for (Object values : hashMap.values()) {
            System.out.println(values.toString() + "\n");
        }
    }

    //TODO numero de lineas
    public void deleteLine() {
        int count = 1;
        System.out.print(String.format("\033[%dA", count)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    public int getInt(String question) {
        String num;
        do {
            System.out.print(question);
            num = keyboard.nextLine();
            if (!isInt(num)) {
                deleteLine();
            }
        } while (!isInt(num));

        return Integer.parseInt(num);
    }

    public String getString(String question) {
        String string;
        do {
            System.out.print(question);
            string = keyboard.nextLine();
            if (string.equals("")) {
                deleteLine();
            }
        } while (string.equals(""));
        return string;
    }

    public boolean isInt(String num) {
        return num.matches(numberRegex);
    }

    //TODO mejor nombre
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
