package island;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Island class initializes the entire island with the given parameters.
 * Retrieves the Island dimensions into the constructor
 */
public class Island implements Runnable {
    private final Properties appProp;
    private final Location[][] islandMap;
    private final ExecutorService executor;
    private int daysCounter = 1;

    public Island(Properties appProp) {
        this.appProp = appProp;
        this.islandMap = new Location[Integer.parseInt(appProp.getProperty("IslandSizeX"))]
                [Integer.parseInt(appProp.getProperty("IslandSizeY"))];
        this.executor = Executors.newFixedThreadPool(Integer.parseInt(appProp.getProperty("NumOfIslandHandlers")));
    }

    public void initialize() {
        int[] locationCoordinates;
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                locationCoordinates = new int[]{i, j};
                islandMap[i][j] = new Location(appProp, islandMap, locationCoordinates);
            }
        }
        System.out.printf("Island dimensions: %dX%d \n", islandMap.length, islandMap[0].length);
    }

    @Override
    public void run() {
        daysCounter++;
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                executor.execute(islandMap[i][j]);
            }
        }
        print();
        if (isExtinct()) {
            System.out.println("Unfortunately, all the animals on the island are extinct. " +
                    "Life on the island lasted " + daysCounter + " days.");
            System.exit(0);
        }
    }

    public void print() {
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                System.out.print("[" + i + ":" + j + "]" + islandMap[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Checks to see if all the animals are extinct on the island.
     * @return true if all the animals are extinct
     */
    public boolean isExtinct() {
        int emptyLocationsCounter = 0;
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                if (islandMap[i][j].getPopulation()
                        .entrySet()
                        .stream()
                        .allMatch(entry -> entry.getValue() <= 0)) {
                    emptyLocationsCounter++;
                }
            }
        }
        return emptyLocationsCounter == (islandMap.length * islandMap[0].length);
    }
}
