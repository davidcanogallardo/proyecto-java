package project;

import java.util.Scanner;
import project.Models.Client;
import project.Models.DAO;
import project.Models.PresenceRegisterDAO;

import java.util.logging.Logger;

import project.Exceptions.StockInsuficientException;
import project.Models.Address;
import project.Models.Client;
import project.Models.DAO;
import project.Models.Pack;
import project.Models.Persistable;
import project.Models.Person;
import project.Models.Presence;
import project.Models.PresenceRegisterDAO;
import project.Models.Product;
import project.Models.ProductsDAO;
import project.Models.Supplier;

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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Locale.Category;

public class ViewController {
    private Scanner keyboard = new Scanner(System.in);

    private DAO<Client> daoClient = new DAO<>();
    private DAO<Supplier> daoSupplier = new DAO<>();
    private ProductsDAO daoProduct = new ProductsDAO<>();
    private PresenceRegisterDAO prd = new PresenceRegisterDAO();

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
    private LocalDate startCatalog;
    private LocalDate endCatalog;
    private LocalDate date;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String PRODUCT_PATH = "products.dat";
    private static final String SUPPLIER_PATH = "suppliers.dat";
    private static final String CLIENT_PATH = "clients.dat";

    // Internacionalizacion
    private Locale lDefault = Locale.getDefault(Category.DISPLAY);
    private Locale lFormat = Locale.getDefault(Category.FORMAT);
    private ResourceBundle text;
    NumberFormat nFormatter;
    NumberFormat cFormatter;

    private static Logger logger = Logger.getLogger(ViewController.class.getName());

    public void run() throws IOException {

        // try (DataInputStream dis = new DataInputStream(
        // new BufferedInputStream(new FileInputStream("a.txt")))) {
        // while (dis.available() > 0) {
        // System.out.println(dis.readInt());
        // System.out.println(dis.readInt());
        // }
        // }

        // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // LocalDate owo = LocalDate.parse("22/05/2000", dtf);
        // System.out.println((owo.format(dtf)));

        // DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        // LocalTime now = LocalTime.now();
        // System.out.println(dtf2.format(now));

        // LocalDate today = LocalDate.now();
        // System.out.println(today);

        // LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        // LocalDateTime ldt2 = LocalDateTime.of(LocalDate.now(),
        // LocalTime.now().plusHours(1));
        // System.out.println(ldt.compareTo(ldt2));

        // LocalDate date = LocalDate.of(2022,02,18);
        // System.out.println(dtf.parse("22/05/2000"));
        // System.out.println(dtf.parse("dsfdsfvsd"));
        // getDate(false);

        System.out.println(lDefault.toLanguageTag());
        // ProductsDAO dd = new ProductsDAO<>();
        // File productFile = new File(PRODUCT_PATH);

        // dd.load(PRODUCT_PATH);

        // System.out.println(dd.getMap().toString());

        loadDAO();
        // System.out.println(lFormat.equals(new Locale("en", "US")));
        setLocale();
        System.out.println(cFormatter.format(43));

        try {
            mainMenu();
        } catch (DateTimeParseException e) {
            System.out.println(e);
            // Caza cualquier excepción y guarda todos los DAOs
            // TODO no guarda todos los daos
            saveDAO();
        }
    }

    private void setLocale() {
        if (!lDefault.equals(new Locale("es", "ES")) && !lDefault.equals(new Locale("ca", "ES"))) {
            System.out.println("1");
            lDefault = new Locale("es", "ES");
            System.out.println(lDefault);
        }
        if (!lFormat.equals(new Locale("es", "ES")) && !lFormat.equals(new Locale("ca", "ES"))) {
            System.out.println("2");
            lFormat = new Locale("es", "ES");
            System.out.println(lFormat);
        }
		nFormatter = NumberFormat.getNumberInstance(lFormat);
		cFormatter = NumberFormat.getCurrencyInstance(lFormat);
		
        text = ResourceBundle.getBundle("Texts", lDefault);
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
            System.out.println(text.getString("000")+":");
            System.out.println("[0] " + text.getString("001"));
            System.out.println("[1] " + text.getString("002"));
            System.out.println("[2] " + text.getString("003"));
            System.out.println("[3] " + text.getString("004"));
            System.out.println("[4] " + text.getString("005"));
            System.out.print(text.getString("006")+": ");
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
                    deleteLine(7);
                    System.out.println("Introduce una opción correcta!");
                    break;
            }
        } while (!"0".equals(option));
    }

    private void clockInOutMenu() {
        int id = 1;
        String option;
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");

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
                    id = getInteger("Id con el que quieres fichar: ", false);
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
                    id = getInteger("Id con el que quieres fichar: ", false);
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
                        option = getString("Qué quieres añadir? (producto/pack) [producto] ", true);
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
                    date = getDate("Introduce una fecha [" + today + "]: ", true);
                    if (date != null) {
                        listDiscontinuedProducts(date);
                    } else {
                        listDiscontinuedProducts(LocalDate.parse(today, dtf));
                    }

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
                // TODO bucle para añadir multiples productos
                boolean isPackRepeated = false;
                boolean keepAddingProds = true;
                Pack packCopy;
                System.out.println("");
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

                packCopy = new Pack((TreeSet<Product>) pack.getProdList().clone(), pack.getDiscount(), pack.getId(),
                        pack.getName(), pack.getPrice(), pack.getStartCatalog(), pack.getEndCatalog());

                System.out.println("");

                do {
                    // Obtener el producto que añadir
                    // TODO añadir aviso prod repetido
                    do {
                        id = getExistingId(daoProduct, "ID del producto que añadir al pack: ");
                        if (daoProduct.get(id) instanceof Pack) {
                            deleteLine(2);
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
                deleteLine(6);
                System.out.println("Introduce una opción correcta!");
            }
        } while (!"0".equals(option));
    }

    private void listDiscontinuedProducts(LocalDate date) {
        System.out.println("\nProductos descatalogados a partir de: " + date.toString() + "\n");
        HashMap<Integer, Product> hm = daoProduct.getMap();
        for (Product product : hm.values()) {
            if (product.getEndCatalog().isBefore(date)) {
                System.out.print("Días de diferencia: ");
                System.out.println(ChronoUnit.DAYS.between(product.getEndCatalog(), date));
                System.out.println(product.toString() + "\n");
            }
        }
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
        id = getFreeId(daoProduct, "ID del producto: ");

        name = getString("Nombre: ", false);
        price = getDouble("Precio: ", false);
        startCatalog = getDate("Introduce la fecha de inicio del catálogo (dd/MM/yyyy): ", false);
        endCatalog = getDate("Introduce la fecha de fin del catálogo (dd/MM/yyyy): ", false);

        // Según si quiere añadir un pack o producto pide diferentes propiedades
        if (!isPack) {
            stock = getInteger("Stock: ", false);
            prod = new Product(id, name, price, stock, startCatalog, endCatalog);
        } else {
            discount = getDiscount("Descuento (0-100): ", false);
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
        id = getInteger("ID del producto: ", false);
        prod = daoProduct.get(id);
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
        id = getExistingId(daoProduct, "ID del producto que quieres modificar: ");

        prod = daoProduct.get(id);

        // Reemplaza las propiedades del producto
        // si el usuario introduce algo
        name = getString("Nombre [" + prod.getName() + "]: ", true);
        if (!name.equals("")) {
            prod.setName(name);
        }

        price = getDouble("Precio [" + prod.getPrice() + "]: ", true);
        if (price != 0) {
            prod.setPrice(price);
        }

        startCatalog = getDate("Inicio catálogo [" + prod.getStartCatalog().format(dtf) + "]: ", true);
        if (startCatalog != null) {
            prod.setStartCatalog(startCatalog);
        }

        endCatalog = getDate("Fin catálogo [" + prod.getEndCatalog().format(dtf) + "]: ", true);
        if (endCatalog != null) {
            prod.setEndCatalog(endCatalog);
        }

        if (prod instanceof Pack pack) {
            discount = getDiscount("Descuento (0-100) [" + pack.getDiscount() + "]: ", true);
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
                            id = getExistingId(daoProduct, "ID de un producto: ");
                            stock = getInteger("Stock que añadir: ", false);
                            prod = daoProduct.get(id);
                            prod.putStock(stock);
                            break;
                        case "2":
                            String filePath = getString("Nombre del archivo donde leer la comanda: ", false);
                            System.out.println(filePath);
                            if (fileIsValid(filePath)) {
                                putStockFromFile(filePath);
                                System.out.println("Stock añadido");
                            }
                            break;
                    }
                    System.out.println("");
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

    private void putStockFromFile(String filePathString) throws FileNotFoundException, IOException {
        int id;
        int quantity;
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(filePathString)))) {
            while (dis.available() > 0) {
                id = dis.readInt();
                quantity = dis.readInt();
                if (id != 0) {
                    // System.out.println(id + " " + quantity);
                    prod = daoProduct.get(id);
                    if (prod != null) {
                        prod.putStock(quantity);
                    } else {
                        // System.out.println("producto id: "+ id + "no existe");
                    }
                }
            }
        }
        System.out.println("");
    }

    private void saveOrderFile() throws IOException {
        String id = "";
        String stock = "";
        String filePath = getString("Nombre del archivo donde guardar la comanda: ", false);
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
                        deleteLine(1);
                    }
                } while (id.equals(""));

                if (!id.equalsIgnoreCase("q")) {
                    // STOCK
                    do {
                        System.out.print("Stock: ");
                        stock = keyboard.nextLine();
                        if (stock.equals("")) {
                            deleteLine(1);
                        }
                    } while (stock.equals(""));

                    if (!stock.equalsIgnoreCase("q")) {
                        if (isNumber(stock) && isNumber(id) && !stock.equals("0")) {
                            products.add(Integer.parseInt(id));
                            products.add(Integer.parseInt(stock));
                            System.out.println("");
                        } else {
                            deleteLine(3);
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

        // Pedir datos del cliente/proveedor
        dni = getValidDni("DNI: ", false);
        name = getString("Nombre: ", false);
        surname = getString("Apellido: ", false);
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
        locality = getString("Localidad: ", false);

        province = getString("Provincia: ", false);

        zipCode = getZipCode("Código Postal (número de 5 cifras): ", false);

        address = getString("Dirección: ", false);

        return new Address(locality, province, zipCode, address);
    }

    private LinkedHashSet<String> askPhoneNumber() {
        LinkedHashSet<String> phoneNumber = new LinkedHashSet();
        boolean keepAddPhones = false;
        String opt;
        String phone;

        do {
            phone = getPhoneNumber("Número de teléfono: ");
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

        // dni, nombre, apellidos
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

        // Dirección
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
            // System.out.println(cFormatter.format(values.toString()) + "\n");

        }
    }

    /******************************** UTILS **************************************/

    private boolean fileIsValid(String filePath) {
        File file;
        Path p = Paths.get(filePath);

        file = new File(filePath);
        if (Files.notExists(p)) {
            System.out.println("\nEl archivo no existe!");
            return false;
        }
        if (!file.isFile()) {
            System.out.println("\nNo es un archivo");
            return false;
        }
        if (!Files.isReadable(p)) {
            System.out.println("\nEl archivo no tiene permisos de lectura");
            return false;
        }
        return true;
    }

    private void deleteLine(int linesToDelete) {
        System.out.print(String.format("\033[%dA", linesToDelete)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    private String getPhoneNumber(String question) {
        String phone;
        boolean isPhoneNumber = true;
        String phoneRegex = "\\d{9}";
        do {
            System.out.println("");
            System.out.print(question);
            phone = keyboard.nextLine();
            // TODO trim
            // https://www.geeksforgeeks.org/java-string-trim-method-example/
            if (phone.equals("")) {
                isPhoneNumber = false;
            } else if (phone.matches(phoneRegex)) {
                isPhoneNumber = true;
            } else {
                isPhoneNumber = false;
            }
        } while (!isPhoneNumber);

        return phone;
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
            if (!returnEmpty) {
                if (num > 100) {
                    deleteLine(1);
                }
            } else {
                num = 0;
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

    private int getExistingId(Persistable dao, String question) {
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

    private LocalDate getDate(String question, boolean returnEmpty) {
        boolean dateValid = true;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inputDate = null;

        do {
            System.out.print(question);
            String date = keyboard.nextLine();
            try {
                inputDate = LocalDate.parse(date, dtf);
                dateValid = true;
            } catch (Exception e) {
                if (!returnEmpty) {
                    System.out.println("Introduce una fecha correcta: ");
                }
                dateValid = false;
                // TODO: handle exception
            }
        } while (!dateValid && !returnEmpty);

        return inputDate;
    }
}
