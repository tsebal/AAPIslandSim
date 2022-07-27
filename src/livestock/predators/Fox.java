package livestock.predators;

import livestock.MoveDirection;
import livestock.herbivores.Herbivore;

import java.util.List;

public class Fox extends Predator {
    public static final int WEIGHT = 8;
    public static final int MAX_AREA_POPULATION = 30;
    public static final int MAX_AREA_MOVE_SPEED = 2;
    public static final int MAX_FOOD_SATURATION = 2;
    private int foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();

    @Override
    public void eat(List<Herbivore> herbivores) {

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
