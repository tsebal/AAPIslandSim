package livestock.predators;

import livestock.herbivores.Herbivore;

import java.util.List;

public class Boa extends Predator {
    public static final int WEIGHT = 15;
    public static final int MAX_AREA_POPULATION = 30;
    public static final int MAX_AREA_MOVE_SPEED = 1;
    public static final int MAX_FOOD_SATURATION = 3;
    private int foodSaturation = 0;

    @Override
    public boolean isMoved() {
        return false;
    }

    @Override
    public void setIsMoved(boolean isMoved) {

    }

    @Override
    public float getWeight() {
        return 0;
    }

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

    @Override
    public void isDied() {

    }
}
