package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Duck;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;
import livestock.herbivores.Rabbit;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Eagle extends Predator implements EatsPredators {
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Eagle(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("EagleWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("EagleAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("EagleFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("EagleBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("EagleFoodSaturation"));
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
    public void eat(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            int eagleChoosesFood = ThreadLocalRandom.current().nextInt(2);

            switch (eagleChoosesFood) {
                case 0 -> {
                    for (Herbivore herbivore : herbivores) {
                        if (herbivore instanceof Duck &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "duckPopulation");
                            foodSaturation = MAX_FOOD_SATURATION;
                            return;
                        } else if (herbivore instanceof Mouse &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "mousePopulation");
                            foodSaturation += herbivore.getWeight();
                            return;
                        } else if (herbivore instanceof Rabbit &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "rabbitPopulation");
                            foodSaturation = MAX_FOOD_SATURATION;
                            return;
                        }
                    }
                }
                case 1 -> eatPredator(location.getPredators());
            }
        } else {
            foodSaturation -= 0.25f;
            isDied();
        }
    }

    @Override
    public void eatPredator(List<Predator> predators) {
        for (Predator predator : predators) {
            if (predator instanceof Fox &&
                    EatingChance.isEated(this, predator)) {
                location.animalLeave(predator, "foxPopulation");
                foodSaturation = MAX_FOOD_SATURATION;
                return;
            }
        }
        foodSaturation -= 0.25f;
        isDied();
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 0.5f;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("eaglePopulation") < newLocation.getMaxPopulation().get("maxEaglePopulation")) {
            location.animalLeave(this, "eaglePopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "eaglePopulation");
        }
    }

    @Override
    public void breed() {
        int locationEaglePopulation = location.getPopulation().get("eaglePopulation");
        if (locationEaglePopulation / BREED_FACTOR >= 2 &&
                locationEaglePopulation < location.getMaxPopulation().get("maxEaglePopulation")) {
            Eagle newEagle = new Eagle(location);
            newEagle.setIsMoved(true);
            location.animalArrive(newEagle, "eaglePopulation");
        }
        foodSaturation -= 0.5f;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "eaglePopulation");
        }
    }
}
