package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Deer extends Herbivore {
    public static final int WEIGHT = 300;
    public static final int MAX_AREA_MOVE_SPEED = 4;
    public static final int MAX_FOOD_SATURATION = 50;
    private Location location;
    private int foodSaturation = 25;

    public Deer(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                System.out.println("Deer eats Plant");
                plant.remove(0);
                foodSaturation += 1;
            } else {
                System.out.println("Deer is hungry, no more plants there!");
                foodSaturation -= 1;
                isDied();
            }
        }
    }

    @Override
    public void move() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        for (int i = 0; i < moveSpeed; i++) {
            System.out.println("Deer moves " + (i + 1) + " times");
            moveDirection();
        }
        foodSaturation -= 1;
        isDied();
    }

    @Override
    public void moveDirection() {
        Location newLocation = MoveDirection.getNewLocation(location);

        if (newLocation != location &&
                newLocation.getPopulation().get("deerPopulation") < newLocation.getMaxPopulation().get("maxDeerPopulation")) {
            location.animalLeave(this, "deerPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "deerPopulation");
        }
    }

    @Override
    public void breed() {
        int locationFoxPopulation = location.getPopulation().get("deerPopulation");
        if (locationFoxPopulation / 6 >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxDeerPopulation")) {
            location.animalArrive(new Deer(location), "deerPopulation");
            System.out.println("A new deer was born.");
        } else {
            System.out.println("The deer could not breed.");
        }
        foodSaturation -= 1;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The deer died hungry.");
            location.animalLeave(this, "deerPopulation");
        }
    }
}
