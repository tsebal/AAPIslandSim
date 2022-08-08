import island.Island;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main executable. Initializes and loads application settings, creates an Island.
 */
public class IslandModel {
    public static void main(String[] args) {
        System.out.println("Welcome to Animal And Plant Island Simulator!");
        Properties appProp = IslandProp.load();
        Island island = new Island(appProp);
        island.initialize();
        island.print();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleAtFixedRate(island, 0, 100, TimeUnit.MILLISECONDS);
    }
}
