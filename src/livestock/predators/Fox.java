package livestock.predators;

import island.Island;
import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;

/**
 * Fox chance eats: Rabbit 70%, Mouse 90%, Duck 60%, Caterpillar 40%
 */
public class Fox extends Predator {
    public static final int WEIGHT = 8;
    public static final int MAX_AREA_MOVE_SPEED = 2;
    public static final int MAX_FOOD_SATURATION = 2;
    private float foodSaturation = 1f; //MAX_FOOD_SATURATION;
    private MoveDirection moveDirection;
    Location location;

    public Fox(Location location) {
        this.location = location;
    }

    @Override
    public void eat(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Mouse &&
                        EatingChance.isEated(this, herbivore)) {
                    System.out.println("Fox eats Mouse");
                    herbivores.remove(herbivore);
                    location.mousePopulation -= 1;
                    foodSaturation += 0.05f;
                    return;
                }
            }
            System.out.println("Fox is hungry, no more herbivores there!");
            foodSaturation -= 0.05f;
            isDied();
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void moveDirection() {
        moveDirection = MoveDirection.randomDirection();
        int[] thisCoordinates = location.locationCoordinates;
        if (moveDirection == MoveDirection.UP) {
            if (thisCoordinates[1] > 0) {
                location.islandMap[thisCoordinates[0]][thisCoordinates[1] - 1].predators.add(this);
                location.islandMap[thisCoordinates[0]][thisCoordinates[1]].predators.remove(this);
            }
        }
        if (moveDirection == MoveDirection.RIGHT) {
            if (thisCoordinates[0] < location.islandMap.length) {
                location.islandMap[thisCoordinates[0] + 1][thisCoordinates[1]].predators.add(this);
                location.islandMap[thisCoordinates[0]][thisCoordinates[1]].predators.remove(this);
            }
        }
        if (moveDirection == MoveDirection.DOWN) {
            if (thisCoordinates[1] < location.islandMap[0].length) {
                location.islandMap[thisCoordinates[0]][thisCoordinates[1] + 1].predators.add(this);
                location.islandMap[thisCoordinates[0]][thisCoordinates[1]].predators.remove(this);
            }
        }
        if (moveDirection == MoveDirection.LEFT) {
            if (thisCoordinates[0] > 0) {
                location.islandMap[thisCoordinates[0] - 1][thisCoordinates[1]].predators.add(this);
                location.islandMap[thisCoordinates[0]][thisCoordinates[1]].predators.remove(this);
            }
        }
    }

    @Override
    public void breed() {

    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The fox died hungry.");
            location.predators.remove(this);
            location.foxPopulation -= 1;
        }
    }
}
