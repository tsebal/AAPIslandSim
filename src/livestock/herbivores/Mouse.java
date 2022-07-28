package livestock.herbivores;

import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;

public class Mouse extends Herbivore {
    public static final float WEIGHT = 0.05f;
    public static final int MAX_AREA_POPULATION = 500;
    public static final int MAX_AREA_MOVE_SPEED = 1;
    public static final float MAX_FOOD_SATURATION = 0.01f;
    private int foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();

    @Override
    public void eat(List<Plant> plant) {

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
