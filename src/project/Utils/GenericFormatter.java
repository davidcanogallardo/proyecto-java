package project.Utils;

import java.io.Console;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

public class GenericFormatter {
    // Internacionalizacion
    static Locale lDefault = Locale.getDefault(Category.DISPLAY);
    static Locale lFormat = Locale.getDefault(Category.FORMAT);
    static DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    static ResourceBundle text;
    static NumberFormat  nFormatter;
    static NumberFormat cFormatter;
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // static String owo = "not owo";
    // public static String getOwo() {
    //     return owo;
    // }
    // public static void owo() {
    //     owo="owo";
    // }

    public static void setLocale() {
        if (!lDefault.equals(new Locale("es", "ES")) && !lDefault.equals(new Locale("ca", "ES"))) {
            System.out.println("1");
            lDefault = new Locale("es", "ES");
            System.out.println(lDefault);
            df = DateFormat.getDateInstance(DateFormat.SHORT, lDefault);
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


    public static ResourceBundle getText() {
        return text;
    }

    public static String formatNumber(Integer num) {
        if (num != null) {
            return nFormatter.format(num);
        }
        return " ";
    }

    public static String formatPrice(Double num) {
        if (num != null) {
            return cFormatter.format(num);
        }
        return " ";
    }
    public static String formatTime(LocalTime time) {
        if (time != null) {
            return timeFormatter.format(time);
        }
        return " ";
    }

    public static String formatDate(LocalDate date) {
        if (date != null) {
            return date.format(dateFormatter);
        } 
        return null;
    }


}
