import island.Island;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An island model simulator.
 * The main executable. Initializes and loads application settings, creates an Island.
 * @author Tsebal
 * @version 0.1b
 */
public class IslandModel {
    public static void main(String[] args) throws IOException {
        Properties appProp = IslandProp.load();
        int islandRefreshDelayMs = Integer.parseInt(appProp.getProperty("IslandRefreshDelayMs"));

        System.out.println(Island.ANSI_GREEN + "Welcome to Animal And Plant Island Simulator!" + Island.ANSI_RESET);
        System.out.println("Island model dimensions: " +
                appProp.getProperty("IslandSizeX") + " X " +
                appProp.getProperty("IslandSizeY"));
        System.out.println("Island model refresh delay, ms: " + islandRefreshDelayMs);
        System.out.println("Number of Island handlers: " + appProp.getProperty("NumOfIslandHandlers"));
        System.out.println("You can change the parameters of the island model in the app.properties settings file.");
        System.out.println("Press Enter to continue...");
        System.in.read();

        Island island = new Island(appProp);
        island.initialize();
        island.print();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(island, 0, islandRefreshDelayMs, TimeUnit.MILLISECONDS);
    }
}
