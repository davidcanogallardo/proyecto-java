package project.Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import project.Classes.Client;
import project.Classes.Pack;
import project.Classes.Person;
import project.Classes.Product;
import project.Classes.Supplier;
import project.DAOs.ClientDAO;
import project.DAOs.PresenceRegisterDAO;
import project.DAOs.ProductsDAO;
import project.DAOs.SupplierDAO;
import project.Models.Persistable;

public class MenuUtils {
    private static Scanner keyboard = new Scanner(System.in);


    // Logs
    // private static Logger logger = Logger.getLogger(ViewController.class.getName());

    public static boolean fileIsValid(String filePath) {
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

    public static void deleteLine(int linesToDelete) {
        System.out.print(String.format("\033[%dA", linesToDelete)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    public static String getPhoneNumber(String question) {
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

    public static Integer getInteger(String question, boolean returnNull) {
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

    public static double getDouble(String question, boolean returnNull) {
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
                // logger.warning("Input error");
                // System.out.println("Precio incorrecto");
                invalidDouble = true;
            }
        } while (invalidDouble);

        return value;
    }

    public static String getString(String question, boolean returnEmpty) {
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

    public static boolean isNumber(String num) {
        String numberRegex = "\\d{1,10}";
        return num.matches(numberRegex);
    }

    public static Integer getDiscount(String question, boolean returnEmpty) {
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

    public static int getFreeId(Persistable dao, String question) {
        Object obj;
        int id;
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

    public static int getExistingId(Persistable dao, String question) {
        Object obj;
        int id;
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

    public static String getValidDni(String question, boolean returnEmpty) {
        String dni;
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

    public static boolean isDniLetterValid(String letter, int num) {
        String[] letters = {
                "t", "r", "w", "a", "g", "m", "y", "f", "p", "d", "x", "b",
                "n", "j", "z", "s", "q", "v", "h", "l", "c", "k", "e"
        };
        return letter.equalsIgnoreCase(letters[num % 23]);
    }

    public static String getZipCode(String question, boolean returnEmpty) {
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

    public static LocalDate getDate(String question, boolean returnEmpty) {
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
