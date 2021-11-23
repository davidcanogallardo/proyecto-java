package pr5;

import java.io.ObjectInputValidation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class View {
    // Expresión regular para comprobar si un String se puede parsear a int
    private String numberRegex = "\\d+";

    private Scanner keyboard = new Scanner(System.in);

    private Manager<Client> managerClient = new Manager<>();
    private Manager<Supplier> managerSupplier = new Manager<>();
    private Manager<Product> managerProduct = new Manager<>();

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
                //TODO opcion 0 que no diga lo de opcion incorrecta
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
                //TODO bucle al preguntar
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
        // TODO el nombre de la funcion
        String idProduct;
        String name;
        String price;
        String stock;
        Product prod;

        System.out.println("Introduce las propiedades del producto:");
        //ID
        do {
            System.out.print("ID: ");
            idProduct = keyboard.nextLine();
            if (!idProduct.matches(numberRegex)) {
                deleteLine();
            }
        } while (!idProduct.matches(numberRegex));

        //name
        do {
            System.out.print("Nombre: ");
            name = keyboard.nextLine();
            if (name.equals("")) {
                deleteLine();
            }
        } while (name.equals(""));

        //price
        do {
            System.out.print("Precio: ");
            price = keyboard.nextLine();
            if (!price.matches(numberRegex)) {
                deleteLine();
            }
        } while (!price.matches(numberRegex));

        //stock
        do {
            System.out.print("Stock: ");
            stock = keyboard.nextLine();
            if (!stock.matches(numberRegex)) {
                deleteLine();
            }
        } while (!stock.matches(numberRegex));

        prod = new Product(Integer.parseInt(idProduct), name, Integer.parseInt(price), Integer.parseInt(stock));

        if (managerProduct.add(prod) != null) {
            System.out.println("\nProducto añadido!");
            System.out.println(prod.toString()+ "\n");
        } else {
            System.out.println("El producto ya está añadido, prueba otro id\n ");
        }
    }

    public void addPack() {
        String idProduct;
        String idPack;
        String name;
        String price;
        Product prod;
        Pack pack;
        String discount;
        Object obj;
        boolean inputIncorrect = false;

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
                //ID
                do {
                    System.out.print("ID: ");
                    idProduct = keyboard.nextLine();
                    if (!idProduct.matches(numberRegex)) {
                        deleteLine();
                    }
                } while (!idProduct.matches(numberRegex));

                //discount
                do {
                    System.out.print("Descuento (0-100): ");
                    discount = keyboard.nextLine();
                    if (!discount.matches(numberRegex)) {
                        deleteLine();
                    }
                } while (!discount.matches(numberRegex));

                //name
                do {
                    System.out.print("Nombre: ");
                    name = keyboard.nextLine();
                    if (name.equals("")) {
                        deleteLine();
                    }
                } while (name.equals(""));

                //price
                do {
                    System.out.print("Precio: ");
                    price = keyboard.nextLine();
                    if (!price.matches(numberRegex)) {
                        deleteLine();
                    }
                } while (!price.matches(numberRegex));

                //lista de productos 
                ArrayList<Integer> idProdList = new ArrayList<>();

                // TODO descuento formato
                pack = new Pack(idProdList, Integer.parseInt(discount), Integer.parseInt(idProduct), name,
                        Integer.parseInt(price));

                if (managerProduct.add(pack) != null) {
                    System.out.println("\nPack añadido!");
                    System.out.println(pack.toString()+"\n");
                } else {
                    System.out.println("Ya existe un pack con ese ID\n");
                }

            } else if ("2".equals(menuOption)) {
                do {
                    System.out.print("ID del producto que añadir al pack: ");
                    idProduct = keyboard.nextLine();
                    if (!idProduct.matches(numberRegex)) {
                        deleteLine();
                        inputIncorrect = true;
                    } else {
                        obj = (Object) managerProduct.get(Integer.parseInt(idProduct));
                        if (obj == null) {
                            inputIncorrect = true;
                            deleteLine();
                            System.out.println("No existe!");
                        } else if (obj instanceof Pack) {
                            inputIncorrect = true;
                            deleteLine();
                            System.out.println("No es un producto!");
                        } else {
                            inputIncorrect = false;
                        }
                    }
                } while (inputIncorrect);

                prod = (Product) managerProduct.get(Integer.parseInt(idProduct));

                do {
                    System.out.print("ID del pack al que añadir el producto:");
                    idPack = keyboard.nextLine();
                    if (!idPack.matches(numberRegex)) {
                        deleteLine();
                        inputIncorrect = true;
                    } else {
                        obj = (Object) managerProduct.get(Integer.parseInt(idPack));
                        if (obj == null) {
                            inputIncorrect = true;
                            deleteLine();
                            System.out.println("No existe!");
                        } else if (!(obj instanceof Pack)) {
                            inputIncorrect = true;
                            deleteLine();
                            System.out.println("No es un pack!");
                        } else {
                            inputIncorrect = false;
                        }
                    }
                } while (inputIncorrect);
                pack = (Pack) managerProduct.get(Integer.parseInt(idPack));
                if (pack.addProduct(prod.getId())) {
                    System.out.println("Producto añadido al pack!\n");
                } else {
                    System.out.println("No se ha podido añadir el producto al pack\n");
                }
            }
        } while (!"0".equals(menuOption));
    }

    public void searchProduct() {
        String idProduct;
        Product prod;

        System.out.print("ID: ");
        idProduct = keyboard.nextLine();

        if (idProduct.matches(numberRegex)) {
            prod = managerProduct.get(Integer.parseInt(idProduct));
            if (prod != null) {
                System.out.println(prod.toString() + "\n");
            } else {
                System.out.println("No existe el producto\n");
            }
        } else {
            System.out.println("Introduce un número!\n");
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
        do {
            System.out.print("ID del producto: ");
            idProduct = keyboard.nextLine();
            if (!idProduct.matches(numberRegex)) {
                deleteLine();
                inputIncorrect = true;
            } else {
                prod = managerProduct.get(Integer.parseInt(idProduct));
                if (prod == null) {
                    inputIncorrect = true;
                    deleteLine();
                    System.out.println("No existe!");
                }
            }
        } while (inputIncorrect);

        prod = managerProduct.get(Integer.parseInt(idProduct));

        System.out.print("Nombre [" + prod.getName() + "]: ");
        name = keyboard.nextLine();
        if (!name.equals("")) {
            prod.setName(name);
        }

        do {
            System.out.print("Precio [" + prod.getPrice() + "]: ");
            price = keyboard.nextLine();
            if (!price.matches(numberRegex) && !price.equals("")) {
                deleteLine();
            }
        } while (!price.matches(numberRegex) && !price.equals(""));
        if (!price.equals("")) {
            prod.setPrice(Integer.parseInt(price));
        }

        if (prod instanceof Pack pack) {
            do {
                System.out.print("Descuento (0-100)[" + pack.getDiscount() + "]: ");
                discount = keyboard.nextLine();
                if (!discount.matches(numberRegex) && !discount.equals("")) {
                    deleteLine();
                }
            } while (!discount.matches(numberRegex) && !discount.equals(""));
            if (!discount.equals("")) {
                pack.setDiscount(Integer.parseInt(discount));
            }
            managerProduct.modify(pack);
        } else {
            do {
                System.out.print("Stock [" + prod.getStock() + "]: ");
                stock = keyboard.nextLine();
                if (!stock.matches(numberRegex) && !stock.equals("")) {
                    deleteLine();
                }
            } while (!stock.matches(numberRegex) && !stock.equals(""));
            if (!stock.equals("")) {
                prod.setStock(Integer.parseInt(stock));
            }
            managerProduct.modify(prod);
        }
    }

    public void deleteProduct() {
        String idProduct;

        System.out.println("Introduce el id del producto que quieres borrar:");
        idProduct = keyboard.nextLine();

        if (idProduct.matches(numberRegex)) {
            int id = Integer.parseInt(idProduct);
            if (managerProduct.delete(id) != null) {
                System.out.println("Producto borrado!\n");
            } else {
                System.out.println("No existe el producto\n");
            }
        } else {
            System.out.println("Introduce un número!\n");
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
            // Add
            case "1":
                addPerson(true);
                break;
            // Search
            case "2":
                searchPerson(true);
                break;
            // Modify
            case "3":
                modifyPerson(true);
                break;
            // Delete
            case "4":
                deletePerson(true);
                break;
            // List
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
            // Add
            case "1":
                addPerson(false);
                break;
            // Search
            case "2":
                searchPerson(false);
                break;
            // Modify
            case "3":
                modifyPerson(false);
                break;
            // Delete
            case "4":
                deletePerson(false);
                break;
            // List
            case "5":
                printClassObjects(managerSupplier);
                break;
            }
        } while (!"0".equals(option));
    }

    private void addPerson(boolean isClient) {
        String id;
        String dni;
        String name;
        String surname;

        System.out.println("Introduce el id");
        id = keyboard.nextLine();

        if (id.matches(numberRegex)) {
            System.out.println("Introduce el DNI");
            dni = keyboard.nextLine();

            System.out.println("Introduce el nombre");
            name = keyboard.nextLine();

            System.out.println("Introduce el apellido");
            surname = keyboard.nextLine();

            Address address = askAddress();

            // Según si es cliente o no se llama a una clase diferente
            if (isClient) {
                Client client = new Client(Integer.parseInt(id), dni, name, surname, address);

                // if (managerClient.add(client, Integer.parseInt(id)) != null) {
                if (true) {
                    System.out.println("Cliente añadido!\n");
                } else {
                    System.out.println("El cliente ya existe, prueba otro id\n");
                }
            } else {
                Supplier supplier = new Supplier(Integer.parseInt(id), dni, name, surname, address);

                // if (managerSupplier.add(supplier, Integer.parseInt(id)) != null) {
                if (true) {
                    System.out.println("Proveedor añadido!\n");
                } else {
                    System.out.println("El proveedor ya existe, prueba otro id\n");
                }
            }

        } else {
            System.out.println("El id solo puedes ser un número\n");
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
            System.out.println("Introduce la localidad");
            locality = keyboard.nextLine();

            System.out.println("Introduce la provincia");
            province = keyboard.nextLine();

            System.out.println("Introduce el Código Postal (número de 5 cifras)");
            zipCode = keyboard.nextLine();

            System.out.println("Introduce la dirección (calle puerta y piso)");
            address = keyboard.nextLine();

            if (!zipCode.matches(zipRegex)) {
                System.out.println("El Código Postal tiene que ser un número de 5 cifras! Vuelve intentarlo\n");
            }
        } while (!zipCode.matches(zipRegex));

        return new Address(locality, province, zipCode, address);
    }

    private void searchPerson(boolean isClient) {
        String id;

        System.out.println("Introduce el id");
        id = keyboard.nextLine();

        if (id.matches(numberRegex)) {
            // Según si es cliente o no busca en una clase u otra
            if (isClient) {
                Client client = (Client) managerClient.get(Integer.parseInt(id));
                if (client != null) {
                    System.out.println(client.toString() + "\n");
                } else {
                    System.out.println("No existe el cliente\n");
                }
            } else {
                Supplier supplier = (Supplier) managerSupplier.get(Integer.parseInt(id));
                if (supplier != null) {
                    System.out.println(supplier.toString() + "\n");
                } else {
                    System.out.println("No existe el proveedor\n");
                }
            }

        } else {
            System.out.println("El id solo puedes ser un número\n");
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

    private void printClassObjects(Manager p) {
        HashMap<Integer, Object> hashMap = p.getMap();
        for (Object values : hashMap.values()) {
            System.out.println(values.toString() + "\n");
        }
    }

    public void deleteLine() {
        int count = 7;
        System.out.print(String.format("\033[%dA", count)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }
}
