package livestock.predators;

import island.Island;
import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.*;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Boa extends Predator implements EatsPredators {
    private static final int WEIGHT;
    private static final int MAX_AREA_MOVE_SPEED;
    private static final int MAX_FOOD_SATURATION;
    private static final int BREED_FACTOR;

    private Location location;
    private float foodSaturation;
    private boolean isMoved;

    static {
        Properties appProp = Island.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("BoaWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("BoaAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("BoaFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("BoaBreedFactor"));
    }

    public Boa(Location location) {
        this.location = location;
        this.foodSaturation = Float.parseFloat(Island.getAppProp().getProperty("BoaFoodSaturation"));
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
            int boaChoosesFood = ThreadLocalRandom.current().nextInt(2);

            switch (boaChoosesFood) {
                case 0 -> {
                    for (Herbivore herbivore : herbivores) {
                        if (herbivore instanceof Duck &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "duckPopulation");
                            foodSaturation += herbivore.getWeight();
                            return;
                        } else if (herbivore instanceof Mouse &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "mousePopulation");
                            foodSaturation += herbivore.getWeight();
                            return;
                        } else if (herbivore instanceof Rabbit &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "rabbitPopulation");
                            if ((foodSaturation += herbivore.getWeight()) > MAX_FOOD_SATURATION) {
                                foodSaturation = MAX_FOOD_SATURATION;
                            } else {
                                foodSaturation += herbivore.getWeight();
                            }
                            return;
                        }
                    }
                }
                case 1 -> eatPredator(location.getPredators());
            }
        } else {
            foodSaturation -= 0.75f;
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
        foodSaturation -= 0.75f;
        isDied();
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 0.75f;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("boaPopulation") < newLocation.getMaxPopulation().get("maxBoaPopulation")) {
            location.animalLeave(this, "boaPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "boaPopulation");
        }
    }

    @Override
    public void breed() {
        int locationBoaPopulation = location.getPopulation().get("boaPopulation");
        if (locationBoaPopulation / BREED_FACTOR >= 2 &&
                locationBoaPopulation < location.getMaxPopulation().get("maxBoaPopulation")) {
            Boa newBoa = new Boa(location);
            newBoa.setIsMoved(true);
            location.animalArrive(newBoa, "boaPopulation");
        }
        foodSaturation -= 1;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "boaPopulation");
        }
    }
}
