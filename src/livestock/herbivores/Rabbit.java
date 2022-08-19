package livestock.herbivores;

import island.Island;
import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Rabbit extends Herbivore {
    private static final int WEIGHT;
    private static final int MAX_AREA_MOVE_SPEED;
    private static final float MAX_FOOD_SATURATION;
    private static final int BREED_FACTOR;

    private Location location;
    private float foodSaturation;
    private boolean isMoved;

    static {
        Properties appProp = Island.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("RabbitWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("RabbitAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Float.parseFloat(appProp.getProperty("RabbitFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("RabbitBreedFactor"));
    }

    public Rabbit(Location location) {
        this.location = location;
        this.foodSaturation = Float.parseFloat(Island.getAppProp().getProperty("RabbitFoodSaturation"));
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
                foodSaturation = MAX_FOOD_SATURATION;
            } else {
                foodSaturation -= 0.225;
                isDied();
            }
        }
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 0.225;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("rabbitPopulation") < newLocation.getMaxPopulation().get("maxRabbitPopulation")) {
            location.animalLeave(this, "rabbitPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "rabbitPopulation");
        }
    }

    @Override
    public void breed() {
        int locationRabbitPopulation = location.getPopulation().get("rabbitPopulation");
        if (locationRabbitPopulation / BREED_FACTOR >= 2 &&
                locationRabbitPopulation < location.getMaxPopulation().get("maxRabbitPopulation")) {
            Rabbit newRabbit = new Rabbit(location);
            newRabbit.setIsMoved(true);
            location.animalArrive(newRabbit, "rabbitPopulation");
        }
        foodSaturation -= 0.23;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "rabbitPopulation");
        }
    }
}
