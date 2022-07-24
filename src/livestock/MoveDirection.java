package livestock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private static final Random RANDOM = new Random();

    /**
     * Randomly picks direction for movement from 4-way.
     * @return a random direction of movement
     */
    public static MoveDirection randomDirection() {
        return DIRECTION.get(RANDOM.nextInt(4));
    }
}
