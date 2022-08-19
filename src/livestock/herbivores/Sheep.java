package livestock.herbivores;

import island.Island;
import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Sheep extends Herbivore {
    private static final int WEIGHT;
    private static final int MAX_AREA_MOVE_SPEED;
    private static final int MAX_FOOD_SATURATION;
    private static final int BREED_FACTOR;

    private Location location;
    private int foodSaturation;
    private boolean isMoved;

    static {
        Properties appProp = Island.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("SheepWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("SheepAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("SheepFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("SheepBreedFactor"));
    }

    public Sheep(Location location) {
        this.location = location;
        this.foodSaturation = Integer.parseInt(Island.getAppProp().getProperty("SheepFoodSaturation"));
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
        foodSaturation -= 3;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("sheepPopulation") < newLocation.getMaxPopulation().get("maxSheepPopulation")) {
            location.animalLeave(this, "sheepPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "sheepPopulation");
        }
    }

    @Override
    public void breed() {
        int locationSheepPopulation = location.getPopulation().get("sheepPopulation");
        if (locationSheepPopulation / BREED_FACTOR >= 2 &&
                locationSheepPopulation < location.getMaxPopulation().get("maxSheepPopulation")) {
            Sheep newSheep = new Sheep(location);
            newSheep.setIsMoved(true);
            location.animalArrive(newSheep, "sheepPopulation");
        }
        foodSaturation -= 4;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "sheepPopulation");
        }
    }
}
