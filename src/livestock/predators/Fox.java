package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Caterpillar;
import livestock.herbivores.Duck;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

//Fox chance eats: Rabbit 70%, Mouse 90%, Duck 60%, Caterpillar 40%
public class Fox extends Predator {
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Fox(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("FoxWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("FoxAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("FoxFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("FoxBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("FoxFoodSaturation"));
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
                if (herbivore instanceof Mouse &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "mousePopulation");
                    foodSaturation += herbivore.getWeight();
                    return;
                } else if (herbivore instanceof Caterpillar &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "caterpillarPopulation");
                    foodSaturation += herbivore.getWeight();
                    return;
                }
            }
            foodSaturation -= 0.5f;
            isDied();
        }
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
                newLocation.getPopulation().get("foxPopulation") < newLocation.getMaxPopulation().get("maxFoxPopulation")) {
                location.animalLeave(this, "foxPopulation");
                this.location = newLocation;
                newLocation.animalArrive(this, "foxPopulation");
        }
    }

    @Override
    public void breed() {
        int locationFoxPopulation = location.getPopulation().get("foxPopulation");
        if (locationFoxPopulation / BREED_FACTOR >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxFoxPopulation")) {
            Fox newFox = new Fox(location);
            newFox.setIsMoved(true);
            location.animalArrive(newFox, "foxPopulation");
        }
        foodSaturation -= 0.5f;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "foxPopulation");
        }
    }
}
