package livestock.herbivores;

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
    private float foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                System.out.println("Mouse eats Plant");
                plant.remove(0);
                foodSaturation += 0.01f;
            } else {
                System.out.println("Mouse is hungry, no more plants there!");
                foodSaturation -= 0.01f;
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
}
