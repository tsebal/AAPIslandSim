package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;

/**
 * Fox chance eats: Rabbit 70%, Mouse 90%, Duck 60%, Caterpillar 40%
 */
public class Fox extends Predator {
    public static final int WEIGHT = 8;
    public static final int MAX_AREA_MOVE_SPEED = 2;
    public static final int MAX_FOOD_SATURATION = 2;
    private float foodSaturation = 1f; //MAX_FOOD_SATURATION;
    private MoveDirection moveDirection = MoveDirection.randomDirection();
    Location location;

    public Fox(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Herbivore> herbivores) {
        boolean isEated;

        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Mouse &&
                        EatingChance.isEated(this, herbivore)) {
                    System.out.println("Fox eats Mouse");
                    herbivores.remove(herbivore);
                    location.mousePopulation -= 1;
                    foodSaturation += 0.05f;
                    return;
                }
            }
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void moveDirection() {

    }

    @Override
    public void breed() {

    }
}
