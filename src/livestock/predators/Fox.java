package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fox chance eats: Rabbit 70%, Mouse 90%, Duck 60%, Caterpillar 40%
 */
public class Fox extends Predator {
    public static final int WEIGHT = 8;
    public static final int MAX_AREA_MOVE_SPEED = 2;
    public static final int MAX_FOOD_SATURATION = 2;
    private Location location;
    private float foodSaturation = 1f;

    public Fox(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Mouse &&
                        EatingChance.isEated(this, herbivore)) {
                    System.out.println("Fox eats Mouse");
                    location.animalLeave(herbivore, "mousePopulation");
                    foodSaturation += 0.05f;
                    return;
                }
            }
            System.out.println("Fox is hungry, no more herbivores there!");
            foodSaturation -= 0.05f;
            isDied();
        }
    }

    @Override
    public void move() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        for (int i = 0; i < moveSpeed; i++) {
            System.out.println("Fox moves " + (i + 1) + " times");
            moveDirection();
        }
        foodSaturation -= 0.05f;
        isDied();
    }

    @Override
    public void moveDirection() {
        Location newLocation = MoveDirection.getNewLocation(location);

        if (newLocation != location &&
                newLocation.getPopulation().get("foxPopulation") < newLocation.getMaxPopulation().get("maxFoxPopulation")) {
                location.animalLeave(this, "foxPopulation");
                this.location = newLocation;
                newLocation.animalArrive(this, "foxPopulation");
        }
    }

    @Override
    public void breed() {
        int locationFoxPopulation = location.getPopulation().get("foxPopulation");
        if (locationFoxPopulation / 10 >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxFoxPopulation")) {
            location.animalArrive(new Fox(location), "foxPopulation");
            System.out.println("A new fox was born.");
        } else {
            System.out.println("The fox could not breed.");
        }
        foodSaturation -= 0.05f;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The fox died hungry.");
            location.animalLeave(this, "foxPopulation");
        }
    }
}
