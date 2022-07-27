import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * A class for working with Island Model application settings (first init, saving and loading settings).
 */
public class IslandProp {
    public static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static String appConfigPath = rootPath.subSequence(3, rootPath.length()) + "app.properties";

    //checks app.properties file/creates if absent
    public static void init() {
        List<String> configLines;
        if (Files.notExists(Paths.get(appConfigPath))) {
            try {
                configLines = Arrays.asList("AppName = Island Model",
                        "Version = 0.1b",
                        "Author = Tsebal",
                        "IslandSizeX = 1",
                        "IslandSizeY = 1",
                        "PlantPopulationMax = 200",
                        "DeerPopulationMax = 20");
                Files.write(Paths.get(appConfigPath), configLines, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Problems with creating settings file on filesSystem.");
                e.printStackTrace();
            }
        }
    }

    //loads and initializes app properties from app.properties file
    public static Properties load() {
        Properties appProps = new Properties();
        try (FileInputStream fis = new FileInputStream(appConfigPath)) {
            appProps.load(fis);
        } catch (IOException e) {
            System.out.println("Problems with loading settings file. Check the app.properties file" +
                    " in the root directory of the program.");
            e.printStackTrace();
        }
        return appProps;
    }
}
