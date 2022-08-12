package livestock;

import livestock.herbivores.*;
import livestock.predators.Boa;
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
        if (eater instanceof Boa) {
            if (victim instanceof Caterpillar && getRandomChance() <= 90) {
                result = true;
            }
            if (victim instanceof Mouse && getRandomChance() <= 50) {
                result = true;
            }
        }
        if (eater instanceof Boar) {
            if (victim instanceof Duck && getRandomChance() <= 10) {
                result = true;
            }
            if (victim instanceof Fox && getRandomChance() <= 15) {
                result = true;
            }
            if (victim instanceof Rabbit && getRandomChance() <= 20) {
                result = true;
            }
            if (victim instanceof Mouse && getRandomChance() <= 40) {
                result = true;
            }
        }
        if ((eater instanceof Duck || eater instanceof Mouse)
                && victim instanceof Caterpillar) {
            if (getRandomChance() <= 90) {
                result = true;
            }
        }
        if (eater instanceof Fox) {
            if (victim instanceof Caterpillar && getRandomChance() <= 40) {
                result = true;
            }
            if (victim instanceof Duck && getRandomChance() <= 60) {
                result = true;
            }
            if (victim instanceof Rabbit && getRandomChance() <= 70) {
                result = true;
            }
            if (victim instanceof Mouse && getRandomChance() <= 90) {
                result = true;
            }
        }
        if (eater instanceof Wolf) {
            if ((victim instanceof Buffalo || victim instanceof Horse)
                    && getRandomChance() <= 10) {
                result = true;
            }
            if ((victim instanceof Boar || victim instanceof Deer)
                    && getRandomChance() <= 15) {
                result = true;
            }
            if (victim instanceof Duck && getRandomChance() <= 40) {
                result = true;
            }
            if ((victim instanceof Goat || victim instanceof Rabbit)
                    && getRandomChance() <= 60) {
                result = true;
            }
            if (victim instanceof Sheep && getRandomChance() <= 70) {
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
