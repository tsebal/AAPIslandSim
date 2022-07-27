package island;

import java.util.Properties;

/**
 * Island class initializes the entire island with the given parameters.
 * Retrieves the Island dimensions into the constructor
 */
public class Island {
    Properties appProp;
    Location[][] islandMap;

    public Island(Properties appProp) {
        this.appProp = appProp;
        this.islandMap = new Location[Integer.parseInt(appProp.getProperty("IslandSizeX"))]
                                    [Integer.parseInt(appProp.getProperty("IslandSizeY"))];
    }

    public void initialize(){
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                islandMap[i][j] = new Location(appProp);
            }
        }
    }

    public void print(){
        System.out.printf("Island dimensions: %dX%d \n", islandMap.length, islandMap[0].length);
        for (int i = 0; i < islandMap.length; i++) {
            for (int j = 0; j < islandMap[i].length; j++) {
                System.out.print(islandMap[i][j]);
            }
            System.out.println();
        }
    }
}
