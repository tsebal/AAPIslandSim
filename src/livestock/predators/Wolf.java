package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Deer;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;

/**
 * Wolf chance eats: Horse 10%, Deer 15%, Rabbit 60%, Mouse 80%, Goat 60%, Sheep 70%, Boar 15%, Buffalo 10%, Duck 40%
 */
public class Wolf extends Predator {
    public static final int WEIGHT = 50;
    public static final int MAX_AREA_MOVE_SPEED = 3;
    public static final int MAX_FOOD_SATURATION = 8;
    private int foodSaturation = 5;
    public MoveDirection moveDirection = MoveDirection.randomDirection();
    Location location;

    public Wolf(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Deer &&
                        EatingChance.isEated(this, herbivore)) {
                    System.out.println("Wolf eats Deer");
                    herbivores.remove(herbivore);
                    location.deerPopulation -= 1;
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                }
            }
            System.out.println("Wolf is hungry, no more herbivores there!");
            foodSaturation -= 1;
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
