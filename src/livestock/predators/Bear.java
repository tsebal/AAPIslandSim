package livestock.predators;

import island.Island;
import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.*;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Predator implements EatsPredators {
    private static final int WEIGHT;
    private static final int MAX_AREA_MOVE_SPEED;
    private static final int MAX_FOOD_SATURATION;
    private static final int BREED_FACTOR;

    private Location location;
    private float foodSaturation;
    private boolean isMoved;

    static {
        Properties appProp = Island.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("BearWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("BearAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("BearFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("BearBreedFactor"));
    }

    public Bear(Location location) {
        this.location = location;
        this.foodSaturation = Float.parseFloat(Island.getAppProp().getProperty("BearFoodSaturation"));
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
            int bearChoosesFood = ThreadLocalRandom.current().nextInt(2);

            switch (bearChoosesFood) {
                case 0 -> {
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
                            recalculateSaturation(herbivore.getWeight());
                            return;
                        }  else if (herbivore instanceof Goat &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "goatPopulation");
                            recalculateSaturation(herbivore.getWeight());
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
                            recalculateSaturation(herbivore.getWeight());
                            return;
                        } else if (herbivore instanceof Sheep &&
                                EatingChance.isEated(this, herbivore)) {
                            location.animalLeave(herbivore, "sheepPopulation");
                            recalculateSaturation(herbivore.getWeight());
                            return;
                        }
                    }
                }
                case 1 -> eatPredator(location.getPredators());
            }
        } else {
            foodSaturation -= 5;
            isDied();
        }
    }

    @Override
    public void eatPredator(List<Predator> predators) {
        for (Predator predator : predators) {
            if (predator instanceof Boa &&
                    EatingChance.isEated(this, predator)) {
                location.animalLeave(predator, "boaPopulation");
                recalculateSaturation(predator.getWeight());
                return;
            }
        }
        foodSaturation -= 5;
        isDied();
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
                newLocation.getPopulation().get("bearPopulation") < newLocation.getMaxPopulation().get("maxBearPopulation")) {
            location.animalLeave(this, "bearPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "bearPopulation");
        }
    }

    @Override
    public void breed() {
        int locationBearPopulation = location.getPopulation().get("bearPopulation");
        if (locationBearPopulation / BREED_FACTOR >= 2 &&
                locationBearPopulation < location.getMaxPopulation().get("maxBearPopulation")) {
            Bear newBear = new Bear(location);
            newBear.setIsMoved(true);
            location.animalArrive(newBear, "bearPopulation");
        }
        foodSaturation -= 10;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "bearPopulation");
        }
    }

    private void recalculateSaturation(float victimWeight) {
        if ((foodSaturation += victimWeight) > MAX_FOOD_SATURATION) {
            foodSaturation = MAX_FOOD_SATURATION;
        } else {
            foodSaturation += victimWeight;
        }
    }
}
