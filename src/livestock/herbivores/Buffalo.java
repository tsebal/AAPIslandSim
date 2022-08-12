package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Buffalo extends Herbivore {
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private int foodSaturation;
    private boolean isMoved;

    public Buffalo(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("BuffaloWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("BuffaloAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("BuffaloFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("BuffaloBreedFactor"));
        this.foodSaturation = Integer.parseInt(appProp.getProperty("BuffaloFoodSaturation"));
        this.isMoved = false;
    }

    @Override
    public boolean isMoved() {
        return isMoved;
    }

    @Override
    public void setIsMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                plant.remove(0);
                foodSaturation += 1;
            } else {
                foodSaturation -= 1;
                isDied();
            }
        }
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 5;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("buffaloPopulation") < newLocation.getMaxPopulation().get("maxBuffaloPopulation")) {
            location.animalLeave(this, "buffaloPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "buffaloPopulation");
        }
    }

    @Override
    public void breed() {
        int locationBuffaloPopulation = location.getPopulation().get("buffaloPopulation");
        if (locationBuffaloPopulation / BREED_FACTOR >= 2 &&
                locationBuffaloPopulation < location.getMaxPopulation().get("maxBuffaloPopulation")) {
            Buffalo newBuffalo = new Buffalo(location);
            newBuffalo.setIsMoved(true);
            location.animalArrive(newBuffalo, "buffaloPopulation");
        }
        foodSaturation -= 10;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "buffaloPopulation");
        }
    }
}
