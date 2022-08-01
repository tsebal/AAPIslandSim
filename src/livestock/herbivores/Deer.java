package livestock.herbivores;

import island.Location;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;

public class Deer extends Herbivore {
    public static final int WEIGHT = 300;
    public static final int MAX_AREA_MOVE_SPEED = 4;
    public static final int MAX_FOOD_SATURATION = 50;
    private int foodSaturation = 25;
    Location location;
    public boolean isAlreadyTurned;

    public Deer(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            if (!plant.isEmpty()) {
                System.out.println("Deer eats Plant");
                plant.remove(0);
                foodSaturation += 1;
            } else {
                System.out.println("Deer is hungry, no more plants there!");
                foodSaturation -= 1;
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
            System.out.println("The deer died hungry.");
            location.herbivores.remove(this);
            location.deerPopulation -= 1;
        }
    }
}
