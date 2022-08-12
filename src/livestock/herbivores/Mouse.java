package livestock.herbivores;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends Herbivore implements EatsHerbivores {
    private Location location;
    private static float WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static float MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Mouse(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Float.parseFloat(appProp.getProperty("MouseWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("MouseAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Float.parseFloat(appProp.getProperty("MouseFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("MouseBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("MouseFoodSaturation"));
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
            int mouseChoosesFood = ThreadLocalRandom.current().nextInt(2);

            switch (mouseChoosesFood) {
                case 0 -> {
                    if (!plant.isEmpty()) {
                        plant.remove(0);
                        foodSaturation = MAX_FOOD_SATURATION;
                    }
                }
                case 1 -> eatHerbivore(location.getHerbivores());
            }
        } else {
            foodSaturation -= 0.005f;
            isDied();
        }
    }

    @Override
    public void eatHerbivore(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Caterpillar &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "caterpillarPopulation");
                    foodSaturation += herbivore.getWeight();
                    return;
                }
            }
            foodSaturation -= 0.005f;
            isDied();
        }
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 0.005f;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("mousePopulation") < newLocation.getMaxPopulation().get("maxMousePopulation")) {
            location.animalLeave(this, "mousePopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "mousePopulation");
        }
    }

    @Override
    public void breed() {
        int locationMousePopulation = location.getPopulation().get("mousePopulation");
        if (locationMousePopulation / BREED_FACTOR >= 2 &&
                locationMousePopulation < location.getMaxPopulation().get("maxMousePopulation")) {
            Mouse newMouse = new Mouse(location);
            newMouse.setIsMoved(true);
            location.animalArrive(newMouse, "mousePopulation");
        }
        foodSaturation -= 0.005f;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "mousePopulation");
        }
    }
}
