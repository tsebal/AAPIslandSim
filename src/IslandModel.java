import island.Island;

import java.util.Properties;

/**
 * The main executable. Initializes and loads application settings, creates an Island.
 */
public class IslandModel {
    public static void main(String[] args) {
        int daysCounter = 1;
        System.out.println("Welcome to Animal And Plant Island Simulator!");
        Properties appProp = IslandProp.load();
        Island island = new Island(appProp);
        island.initialize();
        island.print();

        while (true) {
            daysCounter++;
            island.refresh();
            island.print();
            if (island.isExtinct()) {
                System.out.println("Unfortunately, all the animals on the island are extinct. " +
                        "Life on the island lasted " + daysCounter + " days.");
                return;
            }
        }
    }
}
