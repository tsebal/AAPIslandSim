package livestock.herbivores;

import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;

public class Deer extends Herbivore {
    public static final int WEIGHT = 300;
    public static final int MAX_AREA_MOVE_SPEED = 4;
    public static final int MAX_FOOD_SATURATION = 50;
    private int foodSaturation = 40;
    private MoveDirection moveDirection = MoveDirection.randomDirection();

    @Override
    public void eat(List<Plant> plant) {
        if (this.foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                System.out.println("Deer eats Plant");
                plant.remove(0);
                this.foodSaturation += 1;    //неправильно считает насыщение, доработать
            } else {
                System.out.println("Deer is hungry, no more plants there!");
                this.foodSaturation -= 1;
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
