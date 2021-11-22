package pr5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class View {
    // Expresión regular para comprobar si un String se puede parsear a int
    private String numberRegex = "\\d+";

    private Scanner keyboard = new Scanner(System.in);
    private Manager managerClient = new Manager();
    private Manager managerSupplier = new Manager();
    private Manager managerProduct = new Manager();

    public void run() {
        String menuOption;

        do {
            System.out.println("Elige una opción:");
            System.out.println("0 Salir");
            System.out.println("1 Productos");
            System.out.println("2 Clientes");
            System.out.println("3 Proveedores");
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
                System.out.println("Introduce una opción correcta!");
                break;
            }
        } while (!"0".equals(menuOption));
    }

    /*--------------------------------------PRODUCTOS------------------------------------------*/
    public void menuProduct() {
        String menuOption;

        do {
            System.out.println("Elige una opción:");
            System.out.println("0 Volver");
            System.out.println("1 Añadir producto");
            System.out.println("2 Buscar producto");
            System.out.println("3 Modificar producto");
            System.out.println("4 Borrar producto");
            System.out.println("5 Mostrar todos los productos");
            menuOption = keyboard.nextLine();

            switch (menuOption) {
            // Add
            case "1":
                addProduct();
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
            }
        } while (!"0".equals(menuOption));
    }

    public void addProduct() {
        String idProduct;
        String name;
        String price;
        String stock;
        Product prod;

        System.out.println("El producto es un pack? (s/n)");
        String answer = keyboard.nextLine();

        if (answer.equalsIgnoreCase("S")) {
            addPack();
        } else if (answer.equalsIgnoreCase("N")) {
            System.out.println("Introduce el id del producto que quieres añadir:");
            idProduct = keyboard.nextLine();

            System.out.println("Introduce el nombre del producto que quieres añadir:");
            name = keyboard.nextLine();

            System.out.println("Introduce el precio del producto que quieres añadir:");
            price = keyboard.nextLine();

            System.out.println("Introduce el stock del producto que quieres añadir:");
            stock = keyboard.nextLine();

            if (idProduct.matches(numberRegex) && price.matches(numberRegex) && stock.matches(numberRegex)) {
                prod = new Product(Integer.parseInt(idProduct), name, Integer.parseInt(price), Integer.parseInt(stock));
                if (managerProduct.add(prod, Integer.parseInt(idProduct)) != null) {
                    System.out.println("Producto añadido!\n");
                } else {
                    System.out.println("El producto ya está añadido, prueba otro id\n ");
                }
            } else {
                System.out.println("Introduce un número en el ID, precio y stock\n");
            }
        }
    }

    public void addPack() {
        String idProduct;
        String name;
        String price;
        Product prod;
        Pack pack;
        String discount;
        Object obj;

        // Variable del menú
        String menuOption;

        do {
            System.out.println("0 Volver");
            System.out.println("1 Añadir un pack");
            System.out.println("2 Añadir un producto a un pack");
            menuOption = keyboard.nextLine();

            if ("1".equals(menuOption)) {
                System.out.println("Introduce el id del pack que quieres añadir:");
                idProduct = keyboard.nextLine();

                System.out.println("Introduce el el descuento del pack:");
                discount = keyboard.nextLine();

                System.out.println("Introduce el nombre del pack que quieres añadir:");
                name = keyboard.nextLine();

                System.out.println("Introduce el precio del pack que quieres añadir:");
                price = keyboard.nextLine();

                ArrayList<Integer> idProdList = new ArrayList<Integer>();

                if (idProduct.matches(numberRegex) && price.matches(numberRegex) && discount.matches(numberRegex)) {
                    pack = new Pack(idProdList, Integer.parseInt(discount) / 100, Integer.parseInt(idProduct), name,
                            Integer.parseInt(price));

                    if (managerProduct.add(pack, Integer.parseInt(idProduct)) != null) {
                        System.out.println("Pack añadido!\n");
                    } else {
                        System.out.println("Ya existe un pack con ese ID\n");
                    }
                } else {
                    System.out.println("El id, precio y descuento tienen que ser un número!\n");
                }
            } else if ("2".equals(menuOption)) {
                System.out.println("Introduce el id de un producto que quieres añadir al pack:");
                idProduct = keyboard.nextLine();

                if (idProduct.matches(numberRegex)) {
                    obj = managerProduct.get(Integer.parseInt(idProduct));

                    if (obj != null) {
                        if (!(obj instanceof Pack)) {
                            prod = (Product) obj;

                            System.out.println("Introduce el id del pack al que quieres añadir el producto:");
                            idProduct = keyboard.nextLine();

                            if (idProduct.matches(numberRegex)) {
                                pack = (Pack) managerProduct.get(Integer.parseInt(idProduct));

                                if (pack != null && pack instanceof Pack) {
                                    if (pack.addProduct(prod.getIdProduct())) {
                                        System.out.println("Producto añadido al pack!\n");
                                    } else {
                                        System.out.println("No se ha podido añadir el producto al pack\n");
                                    }
                                } else {
                                    System.out.println("No existe el pack, prueba otro\n");
                                }
                            } else {
                                System.out.println("Introduce un número!\n");
                            }
                        } else {
                            System.out.println("El id no es de un producto");
                        }
                    }
                } else {
                    System.out.println("No existe el producto, prueba otro\n");
                }
            } else {
                System.out.println("Introduce un número!\n");
            }
        } while (!"0".equals(menuOption));
    }

    public void searchProduct() {
        String idProduct;
        Product prod;

        System.out.println("Introduce el id del producto que quieres buscar:");
        idProduct = keyboard.nextLine();

        if (idProduct.matches(numberRegex)) {
            prod = (Product) managerProduct.get(Integer.parseInt(idProduct));
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
        String idProduct;
        String name;
        String price;
        Integer stock;

        System.out.println("Introduce el id del producto que quieres modificar:");
        idProduct = keyboard.nextLine();

        if (idProduct.matches(numberRegex)) {
            Integer id = Integer.parseInt(idProduct);
            Object obj = managerProduct.get(id);

            if (obj != null) {
                System.out.println("Introduce el nuevo nombre del producto:");
                name = keyboard.nextLine();

                System.out.println("Introduce el nuevo precio del producto:");
                price = keyboard.nextLine();

                if (obj instanceof Pack) {
                    System.out.println("Introduce el el descuento del pack:");
                    Integer discount = keyboard.nextInt();
                    keyboard.nextLine();

                    if (price.matches(numberRegex)) {
                        // managerProduct.modifyPack(id, name, Integer.parseInt(price), discount / 100);
                        System.out.println("Producto modificado!\n");
                    } else {
                        System.out.println("Introduce un número!\n");
                    }
                } else {
                    System.out.println("Introduce el nuevo stock del producto:");
                    stock = keyboard.nextInt();

                    if (price.matches(numberRegex)) {
                        // managerProduct.modifyProduct(id, name, Integer.parseInt(price), stock);
                        System.out.println("Producto modificado!\n");
                    } else {
                        System.out.println("Introduce un número!\n");
                    }
                }
            } else {
                System.out.println("No existe el producto\n");
            }
        } else {
            System.out.println("Introduce un número!\n");
        }
        keyboard.nextLine();
    }

    public void deleteProduct() {
        String idProduct;

        System.out.println("Introduce el id del producto que quieres borrar:");
        idProduct = keyboard.nextLine();

        if (idProduct.matches(numberRegex)) {
            if (managerProduct.delete(Integer.parseInt(idProduct)) != null) {
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
            System.out.println("0 Volver");
            System.out.println("1 Añadir cliente");
            System.out.println("2 Buscar cliente");
            System.out.println("3 Modificar cliente");
            System.out.println("4 Borrar cliente");
            System.out.println("5 Mostrar todos los clientes");

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
            System.out.println("0 Volver");
            System.out.println("1 Añadir proveedor");
            System.out.println("2 Buscar proveedor");
            System.out.println("3 Modificar proveedor");
            System.out.println("4 Borrar proveedor");
            System.out.println("5 Mostrar todos los proveedor");

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

                if (managerClient.add(client, Integer.parseInt(id)) != null) {
                    System.out.println("Cliente añadido!\n");
                } else {
                    System.out.println("El cliente ya existe, prueba otro id\n");
                }
            } else {
                Supplier supplier = new Supplier(Integer.parseInt(id), dni, name, surname, address);

                if (managerSupplier.add(supplier, Integer.parseInt(id)) != null) {
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
                    // managerClient.modifyClient(Integer.parseInt(id), dni, name, surname, address);
                    System.out.println("Datos actualizados:");
                    System.out.println(managerClient.get(Integer.parseInt(id)).toString() + "\n");
                } else {
                    // managerSupplier.modifySupplier(Integer.parseInt(id), dni, name, surname, address);
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
}
