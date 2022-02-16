package project;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws SecurityException, IOException, ClassNotFoundException {
        // String logPath = "log.txt";
        // try {
        //     Logger logger = Logger.getLogger(ViewController.class.getName());
        //     FileHandler fh = new FileHandler(logPath, true);
        //     fh.setFormatter(new SimpleFormatter());
        //     logger.addHandler(fh);
        //     logger.setLevel(Level.ALL);
            
            ViewController v = new ViewController();
            v.run();
        // } catch (IOException ex) {
        //     System.out.println(ex.toString());
        //     System.out.println("Could not find file " + logPath);
        // }
    }
    // TODO controlar bucles infinitos\\ cuando no hay productos etc
}
