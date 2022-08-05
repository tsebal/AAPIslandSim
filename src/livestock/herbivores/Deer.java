package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;
import livestock.predators.Wolf;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Deer extends Herbivore {
    private final Properties appProp;
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private int foodSaturation;
    public boolean isMoved;

    public Deer(Location location, Properties appProp) {
        this.appProp = appProp;
        this.location = location;
        WEIGHT = Integer.parseInt(appProp.getProperty("DeerWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("DeerAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("DeerFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("DeerBreedFactor"));
        this.foodSaturation = Integer.parseInt(appProp.getProperty("DeerFoodSaturation"));
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
        isMoved = true;
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
        if (locationFoxPopulation / BREED_FACTOR >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxDeerPopulation")) {
            Deer newDeer = new Deer(location, appProp);
            newDeer.isMoved = true;
            location.animalArrive(newDeer, "deerPopulation");
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
