package livestock;

import island.Location;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Enum of animal movement direction on island.
 */
public enum MoveDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    private static final List<MoveDirection> DIRECTION =
            Collections.unmodifiableList(Arrays.asList(values()));

    /**
     * Randomly picks direction for movement from 4-way.
     * @return a new Location for movement.
     */
    public static Location getNewLocation(Location location, int moveSpeed) {
        MoveDirection moveDirection = DIRECTION.get(ThreadLocalRandom.current().nextInt(4));

        Location tempLocation = location;
        Location newLocation = location;
        for (int i = 0; i < moveSpeed; i++) {
            System.out.println("Animal moves " + (i + 1) + " times");

            int[] oldCoordinates = tempLocation.getLocationCoordinates();
            if (moveDirection == MoveDirection.UP) {
                if (oldCoordinates[1] > 0) {
                    newLocation = location.getIslandMap()[oldCoordinates[0]][oldCoordinates[1] - 1];
                }
            } else if (moveDirection == MoveDirection.RIGHT) {
                if (oldCoordinates[0] < location.getIslandMap().length - 1) {
                    newLocation = location.getIslandMap()[oldCoordinates[0] + 1][oldCoordinates[1]];
                }
            } else if (moveDirection == MoveDirection.DOWN) {
                if (oldCoordinates[1] < location.getIslandMap()[0].length - 1) {
                    newLocation = location.getIslandMap()[oldCoordinates[0]][oldCoordinates[1] + 1];
                }
            } else if (moveDirection == MoveDirection.LEFT) {
                if (oldCoordinates[0] > 0) {
                    newLocation = location.getIslandMap()[oldCoordinates[0] - 1][oldCoordinates[1]];
                }
            }
            tempLocation = newLocation;
        }

        return newLocation;
    }
}
