package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;

/**
 * Mouse chance eats: Caterpillar 90%, Plant 100%
 */
public class Mouse extends Herbivore {
    public static final float WEIGHT = 0.05f;
    public static final int MAX_AREA_MOVE_SPEED = 1;
    public static final float MAX_FOOD_SATURATION = 0.01f;
    private float foodSaturation = 0.005f;
    private MoveDirection moveDirection = MoveDirection.randomDirection();
    Location location;

    public Mouse(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                System.out.println("Mouse eats Plant");
                plant.remove(0);
                foodSaturation += 0.01f;
            } else {
                System.out.println("Mouse is hungry, no more plants there!");
                foodSaturation -= 0.005f;
                isDied();
            }
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void moveDirection() {

    }

    @Override
    public void breed() {

    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The mouse died hungry.");
            location.herbivores.remove(this);
            location.mousePopulation -= 1;
        }
    }
}
