package pr5;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws SecurityException, IOException, ClassNotFoundException {

        String LogPath = "log.txt";
        try {
            Logger logger = Logger.getLogger(View.class.getName());
            FileHandler fh = new FileHandler(LogPath, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            View v = new View();
            v.run();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            System.out.println("Could not find file " + LogPath);
        }
    }
    // TODO controlar bucles infinitos
}
