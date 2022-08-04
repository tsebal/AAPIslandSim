import island.Island;

import java.util.Properties;

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
        island.refresh();
        island.print();
    }
}
