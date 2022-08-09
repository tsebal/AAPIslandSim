package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

//Mouse chance eats: Caterpillar 90%, Plant 100%
public class Mouse extends Herbivore {
    private final Properties appProp;
    private Location location;
    private static float WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static float MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Mouse(Location location, Properties appProp) {
        this.appProp = appProp;
        this.location = location;
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
            if (!plant.isEmpty()) {
                plant.remove(0);
                foodSaturation = MAX_FOOD_SATURATION;
            } else {
                foodSaturation -= 0.005f;
                isDied();
            }
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
        int locationFoxPopulation = location.getPopulation().get("mousePopulation");
        if (locationFoxPopulation / BREED_FACTOR >= 2 &&
                locationFoxPopulation < location.getMaxPopulation().get("maxMousePopulation")) {
            Mouse newMouse = new Mouse(location, appProp);
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
