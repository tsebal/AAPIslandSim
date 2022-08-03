package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Mouse chance eats: Caterpillar 90%, Plant 100%
 */
public class Mouse extends Herbivore {
    public static final float WEIGHT = 0.05f;
    public static final int MAX_AREA_MOVE_SPEED = 1;
    public static final float MAX_FOOD_SATURATION = 0.01f;
    private Location location;
    private float foodSaturation = 0.005f;

    public Mouse(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                System.out.println("Mouse eats Plant");
                plant.remove(0);
                foodSaturation = MAX_FOOD_SATURATION;
            } else {
                System.out.println("Mouse is hungry, no more plants there!");
                foodSaturation -= 0.005f;
                isDied();
            }
        }
    }

    @Override
    public void move() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        for (int i = 0; i < moveSpeed; i++) {
            System.out.println("Mouse moves " + (i + 1) + " times");
            moveDirection();
        }
        foodSaturation -= 0.01f;
        isDied();
    }

    @Override
    public void moveDirection() {
        Location newLocation = MoveDirection.getNewLocation(location);

        if (newLocation != location &&
                newLocation.getPopulation().get("mousePopulation") < newLocation.getMaxPopulation().get("maxMousePopulation")) {
            location.animalLeave(this, "mousePopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "mousePopulation");
        }
    }

    @Override
    public void breed() {
        int locationFoxPopulation = location.getPopulation().get("mousePopulation");
        if (locationFoxPopulation / 100 >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxMousePopulation")) {
            location.animalArrive(new Mouse(location), "mousePopulation");
            System.out.println("A new mouse was born.");
        } else {
            System.out.println("The mouse could not breed.");
        }
        foodSaturation -= 0.005f;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The mouse died hungry.");
            location.animalLeave(this, "mousePopulation");
        }
    }
}
