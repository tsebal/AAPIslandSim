package livestock.herbivores;

import livestock.MoveDirection;
import livestock.Plant;

public class Deer extends Herbivore {
    public static final int WEIGHT = 300;
    public static final int MAX_AREA_POPULATION = 20;
    public static final int MAX_AREA_MOVE_SPEED = 4;
    public static final int MAX_FOOD_SATURATION = 50;
    private int foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();

    @Override
    public void eat(Plant plant) {

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
