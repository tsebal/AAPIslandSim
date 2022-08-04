package livestock;

import livestock.herbivores.Deer;
import livestock.herbivores.Mouse;
import livestock.predators.Fox;
import livestock.predators.Wolf;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Determines the chance of an animal eating food that suits it.
 * Returns the result as boolean variable, eaten or not.
 */
public class EatingChance {

    public static boolean isEated(Animal eater, Animal victim) {
        boolean result = false;
        if (eater instanceof Fox && victim instanceof Mouse) {
            if (getRandomChance() <= 90) {
                result = true;
            }
        }
        if (eater instanceof Wolf) {
            if (victim instanceof Deer && getRandomChance() <= 15) {
                result = true;
            }
            if (victim instanceof Mouse && getRandomChance() <= 80) {
                result = true;
            }
        }
        return result;
    }

    private static int getRandomChance() {
        return ThreadLocalRandom.current().nextInt(100 + 1);
    }
}
