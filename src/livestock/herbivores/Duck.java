package livestock.herbivores;

import island.Island;
import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Duck extends Herbivore implements EatsHerbivores {
    private static final int WEIGHT;
    private static final int MAX_AREA_MOVE_SPEED;
    private static final float MAX_FOOD_SATURATION;
    private static final int BREED_FACTOR;

    private Location location;
    private float foodSaturation;
    private boolean isMoved;

    static {
        Properties appProp = Island.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("DuckWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("DuckAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Float.parseFloat(appProp.getProperty("DuckFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("DuckBreedFactor"));
    }

    public Duck(Location location) {
        this.location = location;
        this.foodSaturation = Float.parseFloat(Island.getAppProp().getProperty("DuckFoodSaturation"));
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
            int duckChoosesFood = ThreadLocalRandom.current().nextInt(2);

            switch (duckChoosesFood) {
                case 0 -> {
                    if (!plant.isEmpty()) {
                        plant.remove(0);
                        foodSaturation = MAX_FOOD_SATURATION;
                    }
                }
                case 1 -> eatHerbivore(location.getHerbivores());
            }
        } else {
            foodSaturation -= 0.05f;
            isDied();
        }
    }

    @Override
    public void eatHerbivore(List<Herbivore> herbivores) {
        for (Herbivore herbivore : herbivores) {
            if (herbivore instanceof Caterpillar &&
                    EatingChance.isEated(this, herbivore)) {
                location.animalLeave(herbivore, "caterpillarPopulation");
                foodSaturation += herbivore.getWeight();
                return;
            }
        }
        foodSaturation -= 0.05f;
        isDied();
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 0.05f;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("duckPopulation") < newLocation.getMaxPopulation().get("maxDuckPopulation")) {
            location.animalLeave(this, "duckPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "duckPopulation");
        }
    }

    @Override
    public void breed() {
        int locationDuckPopulation = location.getPopulation().get("duckPopulation");
        if (locationDuckPopulation / BREED_FACTOR >= 2 &&
                locationDuckPopulation < location.getMaxPopulation().get("maxDuckPopulation")) {
            Duck newDuck = new Duck(location);
            newDuck.setIsMoved(true);
            location.animalArrive(newDuck, "duckPopulation");
        }
        foodSaturation -= 0.05f;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "duckPopulation");
        }
    }
}
