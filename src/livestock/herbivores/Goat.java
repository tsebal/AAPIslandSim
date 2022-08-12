package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Goat extends Herbivore {
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private int foodSaturation;
    private boolean isMoved;

    public Goat(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("GoatWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("GoatAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("GoatFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("GoatBreedFactor"));
        this.foodSaturation = Integer.parseInt(appProp.getProperty("GoatFoodSaturation"));
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
        foodSaturation -= 2;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("goatPopulation") < newLocation.getMaxPopulation().get("maxGoatPopulation")) {
            location.animalLeave(this, "goatPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "goatPopulation");
        }
    }

    @Override
    public void breed() {
        int locationGoatPopulation = location.getPopulation().get("goatPopulation");
        if (locationGoatPopulation / BREED_FACTOR >= 2 &&
                locationGoatPopulation < location.getMaxPopulation().get("maxGoatPopulation")) {
            Goat newGoat = new Goat(location);
            newGoat.setIsMoved(true);
            location.animalArrive(newGoat, "goatPopulation");
        }
        foodSaturation -= 3;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "goatPopulation");
        }
    }
}
