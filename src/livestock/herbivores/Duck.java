package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

// Duck chance eats: Caterpillar 90%
public class Duck extends Herbivore implements EatsHerbivores {
    private final Properties appProp;
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static float MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Duck(Location location, Properties appProp) {
        this.appProp = appProp;
        this.location = location;
        WEIGHT = Integer.parseInt(appProp.getProperty("DuckWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("DuckAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Float.parseFloat(appProp.getProperty("DuckFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("DuckBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("DuckFoodSaturation"));
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
        //System.out.println("Duck fake eats herbivore");
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
        int locationFoxPopulation = location.getPopulation().get("duckPopulation");
        if (locationFoxPopulation / BREED_FACTOR >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxDuckPopulation")) {
            Duck newDuck = new Duck(location, appProp);
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
