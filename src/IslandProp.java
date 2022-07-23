import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * A class for working with AAPIsland application settings (saving and loading settings).
 */
public class IslandProp {
    public static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static String appConfigPath = rootPath + "app.properties";

    //checks app.properties file/creates if absent
    static {
        Path appPropFilePath = Paths.get(appConfigPath);
        if (Files.notExists(appPropFilePath)) {
            try {
                Files.createFile(appPropFilePath);
            } catch (IOException e) {
                System.out.println("Problems with creating settings file on filesSystem.");
                e.printStackTrace();
            }
        }
    }

    //loads and initializes app properties from app.properties file
    public static void load() {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            System.out.println("Problems with loading the settings file. Check the app.properties file" +
                    " in the root directory of the program.");
            e.printStackTrace();
        }
    }
}
