package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.*;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Wolf extends Predator {
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Wolf(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("WolfWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("WolfAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("WolfFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("WolfBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("WolfFoodSaturation"));
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
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Boar &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "boarPopulation");
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                } else if (herbivore instanceof Buffalo &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "buffaloPopulation");
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                } else if (herbivore instanceof Deer &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "deerPopulation");
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                } else if (herbivore instanceof Duck &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "duckPopulation");
                    foodSaturation += herbivore.getWeight();
                    return;
                }  else if (herbivore instanceof Goat &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "goatPopulation");
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                } else if (herbivore instanceof Horse &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "horsePopulation");
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
                    foodSaturation += herbivore.getWeight();
                    return;
                } else if (herbivore instanceof Sheep &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "sheepPopulation");
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                }
            }
            foodSaturation -= 2;
            isDied();
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
                newLocation.getPopulation().get("wolfPopulation") < newLocation.getMaxPopulation().get("maxWolfPopulation")) {
            location.animalLeave(this, "wolfPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "wolfPopulation");
        }
    }

    @Override
    public void breed() {
        int locationWolfPopulation = location.getPopulation().get("wolfPopulation");
        if (locationWolfPopulation / BREED_FACTOR >= 2 &&
                locationWolfPopulation < location.getMaxPopulation().get("maxWolfPopulation")) {
            Wolf newWolf = new Wolf(location);
            newWolf.setIsMoved(true);
            location.animalArrive(newWolf, "wolfPopulation");
        }
        foodSaturation -= 2;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "wolfPopulation");
        }
    }
}
