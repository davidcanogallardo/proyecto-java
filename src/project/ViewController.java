package project;

import java.util.Scanner;
import java.util.logging.Logger;

import project.Classes.Address;
import project.Classes.Client;
import project.Classes.Pack;
import project.Classes.Person;
import project.Classes.Presence;
import project.Classes.Product;
import project.Classes.Supplier;
import project.DAOs.ClientDAO;
import project.DAOs.PresenceRegisterDAO;
import project.DAOs.ProductDAO;
import project.DAOs.SupplierDAO;
import project.Exceptions.StockInsuficientException;
import project.Models.Persistable;
import project.Utils.GenericFormatter;
import project.Utils.MenuUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.Locale.Category;

public class ViewController {
    private Scanner keyboard = new Scanner(System.in);

    // DAOs
    private ClientDAO daoClient = new ClientDAO();
    private SupplierDAO daoSupplier = new SupplierDAO();
    private ProductDAO daoProduct = new ProductDAO();
    private PresenceRegisterDAO prd = new PresenceRegisterDAO();

    // Variables para menús
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

    // Fechas
    private LocalDate startCatalog;
    private LocalDate endCatalog;
    private LocalDate date;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //
    private static final String PRODUCT_PATH = "products.dat";
    private static final String SUPPLIER_PATH = "suppliers.dat";
    private static final String CLIENT_PATH = "clients.dat";

    // Internacionalizacion
    private ResourceBundle text;

    // Logs
    private static Logger logger = Logger.getLogger(ViewController.class.getName());

    public void run() throws IOException {
        Locale lDefault = Locale.getDefault(Category.DISPLAY);
        System.out.println(lDefault.toLanguageTag());

        loadDAO();

        GenericFormatter.setLocale();
        text = GenericFormatter.getText();

        try {
            mainMenu();
        } catch (Exception e) {
            // guardo la excepcion
            logger.warning(e.toString());
            System.out.println("\n" + e);
            // Caza cualquier excepción y guarda todos los DAOs
            // TODO no guarda todos los daos
            saveDAO();
        }
    }

    public void loadDAO() throws IOException {
        File productFile = new File(PRODUCT_PATH);
        File supplierFile = new File(SUPPLIER_PATH);
        File clientFile = new File(CLIENT_PATH);

        if (!productFile.exists()) {
            if (!productFile.createNewFile()) {
                // TODO añadir al logger
                System.out.println("No se ha podido crear el archivo");
            }
        } else {
            daoProduct.load(PRODUCT_PATH);
        }

        if (!supplierFile.exists()) {
            if (!supplierFile.createNewFile()) {
                // TODO añadir al logger
                System.out.println("No se ha podido crear el archivo");
            }
        } else {
            daoSupplier.load(SUPPLIER_PATH);
        }

        if (!clientFile.exists()) {
            if (!clientFile.createNewFile()) {
                // TODO añadir al logger
                System.out.println("No se ha podido crear el archivo");
            }
        } else {
            daoClient.load(CLIENT_PATH);
        }
    }

    public void saveDAO() throws IOException {
        daoProduct.save(PRODUCT_PATH);
        daoSupplier.save(SUPPLIER_PATH);
        daoClient.save(CLIENT_PATH);
    }

    public void mainMenu() throws IOException {
        String option;
        do {
            System.out.println(text.getString("000") + ":");
            System.out.println("[0] " + text.getString("001"));
            System.out.println("[1] " + text.getString("002"));
            System.out.println("[2] " + text.getString("003"));
            System.out.println("[3] " + text.getString("004"));
            System.out.println("[4] " + text.getString("005"));
            System.out.print(text.getString("006") + ": ");
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
                case "4":
                    clockInOutMenu();
                    break;
                case "0":
                    saveDAO();
                    break;
                default:
                    MenuUtils.deleteLine(7);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void clockInOutMenu() {
        int id = 1;
        String option;

        do {
            System.out.println("Elige una opción:");
            System.out.println("[0] Salir");
            System.out.println("[1] Entrada");
            System.out.println("[2] Salida");
            System.out.println("[3] Consultar");
            System.out.println("El id de trabajador actual es: " + id);
            System.out.print("Opción: ");
            option = keyboard.nextLine();

            LocalDate today = LocalDate.now();

            switch (option) {
                case "1":
                    id = MenuUtils.getInteger("Id con el que quieres fichar: ", false);
                    LocalTime now = LocalTime.now();

                    Presence p = new Presence(id, today, now);
                    if (prd.add(p) != null) {
                        System.out.println("Has fichado de entrada");
                    } else {
                        System.out.println(
                                "No has podido fichar, para volver a fichar de entrada tiene que fichar de salida");
                    }
                    break;
                case "2":
                    id = MenuUtils.getInteger("Id con el que quieres fichar: ", false);
                    if (prd.addLeaveTime(id)) {
                        System.out.println("Has fichado de salida");
                    } else {
                        System.out.println("Ficha de entrada antes");
                    }
                    break;
                case "3":
                    prd.list();
                    break;
                case "0":
                    break;
                default:
                    MenuUtils.deleteLine(7);
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
            System.out.println("[6] Crear comanda");
            System.out.println("[7] Mostrar todos los productos");
            System.out.println("[8] Mostrar productos descatalogados");
            System.out.print("Opción: ");
            option = keyboard.nextLine();

            switch (option) {
                // Add
                case "1":
                    System.out.println("");
                    do {
                        option = MenuUtils.getString("Qué quieres añadir? (producto/pack) [producto] ", true);
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
                    try {
                        stockGestor();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case "6":
                    try {
                        saveOrderFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                // List all
                case "7":
                    listProducts();

                    break;
                case "8":
                    String today = LocalDate.now().format(dtf);
                    date = MenuUtils.getDate("Introduce una fecha [" + today + "]: ", true);
                    List<Product> list;
                    if (date == null) {
                        date = LocalDate.parse(today, dtf);
                    }

                    list = daoProduct.getDiscontinuedProducts(date);
                    for (Product prod : list) {
                        System.out.print("Días de diferencia: ");
                        System.out.println(ChronoUnit.DAYS.between(prod.getEndCatalog(), date));
                        System.out.println(prod.toString() + "\n");
                    }

                    break;
                case "0":
                    System.out.println("\n");
                    break;
                default:
                    MenuUtils.deleteLine(9);
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
                // TODO bucle para añadir multiples productos
                boolean isPackRepeated = false;
                boolean keepAddingProds = true;
                Pack packCopy;
                System.out.println("");
                System.out.println("");

                // Obtener el pack que añadir al producto
                do {
                    id = MenuUtils.getExistingId(daoProduct, "ID del pack al que añadir el producto: ");
                    if (!(daoProduct.get(id) instanceof Pack)) {
                        MenuUtils.deleteLine(2);
                        System.out.println("Elige un pack");
                    }
                } while (!(daoProduct.get(id) instanceof Pack));
                pack = (Pack) daoProduct.get(id);

                packCopy = new Pack((TreeSet<Product>) pack.getProdList().clone(), pack.getDiscount(), pack.getId(),
                        pack.getName(), pack.getPrice(), pack.getStartCatalog(), pack.getEndCatalog());

                System.out.println("");

                do {
                    // Obtener el producto que añadir
                    // TODO añadir aviso prod repetido
                    do {
                        id = MenuUtils.getExistingId(daoProduct, "ID del producto que añadir al pack: ");
                        if (daoProduct.get(id) instanceof Pack) {
                            MenuUtils.deleteLine(2);
                            System.out.println("El producto no puede ser un pack");
                        }
                    } while (daoProduct.get(id) instanceof Pack);

                    prod = (Product) daoProduct.get(id);

                    packCopy.addProduct(prod);

                    System.out.print("Quieres añadir otro producto? s/[n]: ");
                    String opt = keyboard.nextLine();
                    if (opt.equals("")) {
                        keepAddingProds = true;
                    } else if (opt.equals("s")) {
                        keepAddingProds = false;
                    } else {
                        keepAddingProds = true;
                    }
                } while (!keepAddingProds);

                System.out.println("Comprobando si el pack es repetido...");
                HashMap<Integer, Product> hm = daoProduct.getMap();
                for (Product prod2 : hm.values()) {
                    if (prod2 instanceof Pack) {
                        if (prod2.equals(packCopy)) {
                            isPackRepeated = true;
                            break;
                        }
                    }
                }

                if (!isPackRepeated) {
                    pack.setProdList(packCopy.getProdList());
                    System.out.println("\nProducto añadido al pack!");
                    System.out.println(pack);
                    // if (pack.addProduct(prod)) {
                    // } else {
                    // System.out.println("No se ha podido añadir el producto al pack");
                    // }
                } else {
                    System.out.println("Pack repetido");
                }
            } else if (!"0".equals(option)) {
                MenuUtils.deleteLine(6);
                System.out.println("Introduce una opción correcta!");
            }
        } while (!"0".equals(option));
    }

    private void listProducts() {
        String option;
        List<Product> list = new ArrayList<Product>(daoProduct.getMap().values());

        System.out.println("Elige un método de ordenación: ");
        System.out.println("[1] Sin orden");
        System.out.println("[2] Ordenado por nombre");
        System.out.println("[3] Ordenado por precio");
        System.out.println("[4] Ordenado por stock");
        System.out.print("Opción: ");
        option = keyboard.nextLine();

        switch (option) {
            case "1":
                printObjects(daoProduct);
                break;
            case "2":
                Collections.sort(list, (m1, m2) -> (m1.getName()).compareTo(m2.getName()));
                printList(list);
                break;
            case "3":
                Collections.sort(list, (m1, m2) -> ((Double) m1.getPrice()).compareTo((Double) m2.getPrice()));
                printList(list);
                break;
            case "4":
                Collections.sort(list, (m1, m2) -> ((Integer) m1.getStock()).compareTo((Integer) m2.getStock()));
                printList(list);
                break;
            default:
                System.out.println("\nTe has equivocado, vuelve a intentarlo.");
                break;
        }
    }

    private void printList(List<Product> list) {
        System.out.println("");
        for (Product product : list) {
            System.out.println(product.toString() + "\n");
        }
    }

    private void addProduct(boolean isPack) {
        System.out.println("Introduce las propiedades del producto:\n");
        // Pedir un ID de un producto que exista
        id = MenuUtils.getFreeId(daoProduct, "ID del producto: ");

        name = MenuUtils.getString("Nombre: ", false);
        price = MenuUtils.getDouble("Precio: ", false);
        startCatalog = MenuUtils.getDate("Introduce la fecha de inicio del catálogo (dd/MM/yyyy): ", false);
        endCatalog = MenuUtils.getDate("Introduce la fecha de fin del catálogo (dd/MM/yyyy): ", false);

        // Según si quiere añadir un pack o producto pide diferentes propiedades
        if (!isPack) {
            stock = MenuUtils.getInteger("Stock: ", false);
            prod = new Product(id, name, price, stock, startCatalog, endCatalog);
        } else {
            discount = MenuUtils.getDiscount("Descuento (0-100): ", false);
            // lista de productos de un pack (vacía por defecto)
            // ArrayList<Integer> productList = new ArrayList<>();
            TreeSet<Product> productList = new TreeSet<>();
            // TODO que te pregunte si quiere añadir ahora o mas tarde productos al pack

            prod = new Pack(productList, discount, id, name, price, startCatalog, endCatalog);
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
        id = MenuUtils.getInteger("ID del producto: ", false);
        prod = (Product) daoProduct.get(id);
        if (prod != null) {
            System.out.println("\n" + prod.toString() + "\n");
        } else {
            System.out.println("No existe el producto\n");
        }
    }

    private void modifyProduct() {
        // TODO modificar fecha catalogo
        System.out.println("");
        // Pedir un ID de un producto que exista
        id = MenuUtils.getExistingId(daoProduct, "ID del producto que quieres modificar: ");

        prod = (Product) daoProduct.get(id);

        // Reemplaza las propiedades del producto
        // si el usuario introduce algo
        name = MenuUtils.getString("Nombre [" + prod.getName() + "]: ", true);
        if (!name.equals("")) {
            prod.setName(name);
        }

        price = MenuUtils.getDouble("Precio [" + GenericFormatter.formatPrice(prod.getPrice()) + "]: ", true);
        if (price != 0) {
            prod.setPrice(price);
        }

        startCatalog = MenuUtils.getDate("Inicio catálogo [" + prod.getStartCatalog().format(dtf) + "]: ", true);
        if (startCatalog != null) {
            prod.setStartCatalog(startCatalog);
        }

        endCatalog = MenuUtils.getDate("Fin catálogo [" + prod.getEndCatalog().format(dtf) + "]: ", true);
        if (endCatalog != null) {
            prod.setEndCatalog(endCatalog);
        }

        if (prod instanceof Pack) {
            Pack pack = (Pack) prod;
            discount = MenuUtils.getDiscount("Descuento (0-100) [" + pack.getDiscount() + "% ]: ", true);
            if (discount != null) {
                pack.setDiscount(discount);
            }

            daoProduct.modify(pack);
            System.out.println("Producto modificado!\n");
            System.out.println(pack.toString() + "\n");
        } else {
            stock = MenuUtils.getInteger("Stock [" + GenericFormatter.formatNumber(prod.getStock()) + "]: ", true);
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
        id = MenuUtils.getInteger("ID del producto: ", false);
        if (daoProduct.get(id) != null) {
            daoProduct.delete(daoProduct.get(id));
            System.out.println("\nProducto borrado!\n");
        } else {
            System.out.println("\nNo existe el producto\n");
        }
    }

    private void stockGestor() throws FileNotFoundException, IOException {
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
                    System.out.println("\nElige una opción:");
                    System.out.println("[0] Volver");
                    System.out.println("[1] Añadir stock manualmente");
                    System.out.println("[2] Añadir stock de una archivo de comandas");
                    System.out.print("Opción: ");
                    option = keyboard.nextLine();
                    // TODO default case
                    // TODO el menu bien
                    switch (option) {
                        case "1":
                            id = MenuUtils.getExistingId(daoProduct, "ID de un producto: ");
                            stock = MenuUtils.getInteger("Stock que añadir: ", false);
                            prod = daoProduct.get(id);
                            prod.putStock(stock);
                            System.out.println("Stock añadido!");
                            break;
                        case "2":
                            String filePath = MenuUtils.getString("Nombre del archivo donde leer la comanda: ", false);
                            // System.out.println(filePath);
                            if (MenuUtils.fileIsValid(filePath)) {
                                putStockFromFile(filePath);
                                System.out.println("Stock añadido!");
                            }
                            break;
                    }
                    System.out.println("");
                    break;
                case "2":
                    id = MenuUtils.getExistingId(daoProduct, "ID de un producto: ");
                    stock = MenuUtils.getInteger("Stock que quitar: ", false);
                    prod = daoProduct.get(id);
                    try {
                        prod.takeStock(stock);
                        System.out.println("Stock quitado!");
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
                    MenuUtils.deleteLine(6);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void putStockFromFile(String filePathString) throws FileNotFoundException, IOException {
        String id;
        Integer idInt;
        int quantity;
        // TODO
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(filePathString)))) {
            while (dis.available() > 0) {
                id = dis.readUTF();
                quantity = dis.readInt();
                if (!id.equals("0")) {
                    // System.out.println(id + " " + quantity);
                    idInt = Integer.parseInt(id);
                    prod = daoProduct.get(idInt);
                    if (prod != null) {
                        prod.putStock(quantity);
                    } else {
                        // System.out.println("producto id: " + id + "no existe");
                    }
                }
            }
        }
        System.out.println("");
    }

    private void saveOrderFile() throws IOException {
        String id = "";
        String stock = "";
        String filePath = MenuUtils.getString("Nombre del archivo donde guardar la comanda: ", false);
        Path p = Paths.get(filePath);
        if (Files.notExists(p)) {
            ArrayList<Integer> products = new ArrayList<Integer>();
            System.out.print("\n");
            do {
                // ID
                do {
                    System.out.print("ID: ");
                    id = keyboard.nextLine();
                    if (id.equals("")) {
                        MenuUtils.deleteLine(1);
                    }
                } while (id.equals(""));

                if (!id.equalsIgnoreCase("q")) {
                    // STOCK
                    do {
                        System.out.print("Stock: ");
                        stock = keyboard.nextLine();
                        if (stock.equals("")) {
                            MenuUtils.deleteLine(1);
                        }
                    } while (stock.equals(""));

                    if (!stock.equalsIgnoreCase("q")) {
                        if (MenuUtils.isNumber(stock) && MenuUtils.isNumber(id) && !stock.equals("0")) {
                            products.add(Integer.parseInt(id));
                            products.add(Integer.parseInt(stock));
                            System.out.println("");
                        } else {
                            MenuUtils.deleteLine(3);
                            System.out.println("El id y el stock deben ser números y el stock no puede ser 0");
                        }
                    }
                }
            } while (!id.equalsIgnoreCase("q") && !stock.equalsIgnoreCase("q"));
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath));
            for (int i = 0; i < products.size(); i++) {
                // System.out.println(products.get(i));
                dos.writeInt(products.get(i));
            }
            dos.close();
        } else {
            System.out.println("\nEl archivo ya existe, elige otro\n");
        }
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
                    MenuUtils.deleteLine(9);
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
            System.out.println("[5] Mostrar todos los proveedores");
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
                    MenuUtils.deleteLine(9);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void addPerson(boolean isClient) {
        System.out.println("\nIntroduce los datos de la persona:");
        // Pedir un ID de un producto que exista
        if (isClient) {
            id = MenuUtils.getFreeId(daoClient, "ID: ");
        } else {
            id = MenuUtils.getFreeId(daoSupplier, "ID: ");
        }

        // Pedir datos del cliente/proveedor
        dni = MenuUtils.getValidDni("DNI: ", false);
        name = MenuUtils.getString("Nombre: ", false);
        surname = MenuUtils.getString("Apellido: ", false);
        Address address = askAddress();
        LinkedHashSet<String> phoneNumber = askPhoneNumber();

        // Según si es cliente o no se llama a una clase diferente
        if (isClient) {
            Client client = new Client(id, dni, name, surname, address, phoneNumber);
            if (daoClient.add(client) != null) {
                System.out.println("\nCliente añadido!\n");
                System.out.println(client.toString() + "\n");
            } else {
                System.out.println("El cliente ya existe, prueba otro id\n");
            }
        } else {
            Supplier supplier = new Supplier(id, dni, name, surname, address, phoneNumber);
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
        locality = MenuUtils.getString("Localidad: ", false);

        province = MenuUtils.getString("Provincia: ", false);

        zipCode = MenuUtils.getZipCode("Código Postal (número de 5 cifras): ", false);

        address = MenuUtils.getString("Dirección: ", false);

        return new Address(locality, province, zipCode, address);
    }

    private LinkedHashSet<String> askPhoneNumber() {
        LinkedHashSet<String> phoneNumber = new LinkedHashSet();
        boolean keepAddPhones = false;
        String opt;
        String phone;

        do {
            phone = MenuUtils.getPhoneNumber("Número de teléfono: ");
            if (phoneNumber.contains(phone)) {
                System.out.println("Número de teléfono repetido");
            } else {
                phoneNumber.add(phone);
            }

            System.out.print("Quieres añadir otro número? s/[n]: \n");
            opt = keyboard.nextLine();
            if (opt.equals("")) {
                keepAddPhones = true;
            } else if (opt.equals("s")) {
                keepAddPhones = false;
            } else {
                keepAddPhones = true;
            }
        } while (!keepAddPhones);

        return phoneNumber;
    }

    private void searchPerson(boolean isClient) {
        id = MenuUtils.getInteger("\nID de la persona: ", false);

        // Según si es cliente o no busca en una clase u otra
        if (isClient) {
            Client client = (Client) daoClient.get(id);
            if (client != null) {
                System.out.println("\n" + client.toString() + "\n");
            } else {
                System.out.println("\nNo existe el cliente\n");
            }
        } else {
            Supplier supplier = (Supplier) daoSupplier.get(id);
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
            person = (Person) daoClient.get(MenuUtils.getExistingId(daoClient, "ID: "));
        } else {
            person = (Person) daoSupplier.get(MenuUtils.getExistingId(daoSupplier, "ID: "));
        }

        // dni, nombre, apellidos
        dni = MenuUtils.getValidDni("DNI [" + person.getDni() + "]: ", true);
        if (!dni.equals("")) {
            person.setDni(dni);
        }
        name = MenuUtils.getString("Nombre [" + person.getName() + "]: ", true);
        if (!name.equals("")) {
            person.setName(name);
        }
        surname = MenuUtils.getString("Apellidos [" + person.getSurname() + "]: ", true);
        if (!surname.equals("")) {
            person.setSurname(surname);
        }

        // Dirección
        String locality;
        String province;
        String zipCode;
        String address;
        Address addr = person.getFullAddress();

        System.out.println("Introduce los datos de la dirección:");
        locality = MenuUtils.getString("Localidad [" + addr.getLocality() + "]: ", true);
        if (!locality.equals("")) {
            addr.setLocality(locality);
        }

        province = MenuUtils.getString("Provincia [" + addr.getProvince() + "]: ", true);
        if (!province.equals("")) {
            addr.setProvince(province);
        }

        zipCode = MenuUtils.getZipCode("Código Postal [" + addr.getZipCode() + "]: ", true);
        if (!zipCode.equals("")) {
            addr.setZipCode(zipCode);
        }

        address = MenuUtils.getString("Dirección [" + addr.getAddress() + "]: ", true);
        if (!address.equals("")) {
            addr.setAddress(address);
        }

        person.setFullAddress(addr);

        // Números de teléfono
        System.out.print("Quieres añadir nuevos números de teléfono? s/[n]: ");
        String opt = keyboard.nextLine();
        boolean addNewNumbers = false;
        if (opt.equalsIgnoreCase("s")) {
            addNewNumbers = true;
        }

        if (addNewNumbers) {
            LinkedHashSet<String> oldPhones = person.getPhoneNumber();

            System.out.println("Números de teléfono: ");
            System.out.println(oldPhones);
            System.out.println("Introduce los nuevos números: ");
            LinkedHashSet<String> newPhones = askPhoneNumber();

            if (oldPhones.addAll(newPhones)) {
                person.setPhoneNumber(oldPhones);
            } else {
                System.out.println("No se han podido actualizar los números");
            }
        }

        // Modificar
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
        id = MenuUtils.getInteger("ID del cliente que borrar: ", false);

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

    private void printObjects(Persistable p) {
        System.out.println("");
        HashMap<Integer, Object> hashMap = p.getMap();
        for (Object values : hashMap.values()) {
            System.out.println(values.toString() + "\n");
            // System.out.println(cFormatter.format(values.toString()) + "\n");

        }
    }
}
