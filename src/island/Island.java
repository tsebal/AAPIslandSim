package island;

import java.util.Arrays;
import java.util.Properties;
import java.util.function.BooleanSupplier;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

/**
 * Island class initializes the entire island with the given parameters.
 * Retrieves the Island dimensions into the constructor
 */
public class Island {
    Properties appProp;
    private Location[][] islandMap;

    public Island(Properties appProp) {
        this.appProp = appProp;
        this.islandMap = new Location[Integer.parseInt(appProp.getProperty("IslandSizeX"))]
                [Integer.parseInt(appProp.getProperty("IslandSizeY"))];
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

    public void refresh() {
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                islandMap[i][j].recalculate();
            }
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

    public boolean isExtinct() {
        int emptyLocationsCounter = 0;
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                if (islandMap[i][j].predators.isEmpty() && islandMap[i][j].herbivores.isEmpty()) {
                    emptyLocationsCounter++;
                }
            }
        }
        return emptyLocationsCounter == (islandMap.length + islandMap[0].length - 1);
    }
}
