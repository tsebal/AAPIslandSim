package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;
import java.util.Random;

/**
 * Fox chance eats: Rabbit 70%, Mouse 90%, Duck 60%, Caterpillar 40%
 */
public class Fox extends Predator {
    public static final int WEIGHT = 8;
    public static final int MAX_AREA_MOVE_SPEED = 2;
    public static final int MAX_FOOD_SATURATION = 2;
    private Location location;
    private float foodSaturation = 1f; //MAX_FOOD_SATURATION;
    private MoveDirection moveDirection;

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
        int moveSpeed = new Random().nextInt(MAX_AREA_MOVE_SPEED + 1);
        for (int i = 0; i < moveSpeed; i++) {
            System.out.println("Лиса делает " + (i + 1) + " ход");
            moveDirection();
        }

    }

    @Override
    public void moveDirection() {
        moveDirection = MoveDirection.randomDirection();
        int[] thisCoordinates = location.getLocationCoordinates();
        int[] newCoordinates = location.getLocationCoordinates();
        System.out.println(thisCoordinates[0] + "|" + thisCoordinates[1] + " коорд лисы старые");
        if (moveDirection == MoveDirection.UP) {
            if (thisCoordinates[1] > 0) {
                location.animalLeave(this, "foxPopulation");
                newCoordinates[1] = newCoordinates[1] - 1;
                location.animalArrive(this, newCoordinates, "foxPopulation");
            }
        } else if (moveDirection == MoveDirection.RIGHT) {
            if (thisCoordinates[0] < location.islandMap.length - 1) {
                location.animalLeave(this, "foxPopulation");
                newCoordinates[0] = newCoordinates[0] + 1;
                location.animalArrive(this, newCoordinates, "foxPopulation");
            }
        } else if (moveDirection == MoveDirection.DOWN) {
            if (thisCoordinates[1] < location.islandMap[0].length - 1) {
                location.animalLeave(this, "foxPopulation");
                newCoordinates[1] = newCoordinates[1] + 1;
                location.animalArrive(this, newCoordinates, "foxPopulation");
            }
        } else if (moveDirection == MoveDirection.LEFT) {
            if (thisCoordinates[0] > 0) {
                location.animalLeave(this, "foxPopulation");
                newCoordinates[0] = newCoordinates[0] - 1;
                location.animalArrive(this, newCoordinates, "foxPopulation");
            }
        }
        System.out.println(newCoordinates[0] + "|" + newCoordinates[1] + " коорд лисы новые");
    }

    @Override
    public void breed() {

    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The fox died hungry.");
            location.animalLeave(this, "foxPopulation");
        }
    }
}
