package wojia.link.ddns.util;

import org.springframework.boot.logging.java.SimpleFormatter;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/6/18 <br/>
 */
public class Log {

    private static final Logger logger = Logger.getLogger("ddns");

    static {
        logger.setUseParentHandlers(false);

        Formatter formatter = new SimpleFormatter();

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        String userHome = System.getProperty("user.home");
        String directory = userHome + "/ddns";

        mkdir(directory);

        try {
            FileHandler fileHandler = new FileHandler(directory + "/ddns.log", true);
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException ignored) {
        }
    }

    private static void mkdir(String dir) {
        File file = new File(dir);

        int count = 0;
        while (count < 5 && !file.mkdir()) {
            count++;
        }
    }

    public static Logger getLogger() {
        return logger;
    }

}