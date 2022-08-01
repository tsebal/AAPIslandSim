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
    public Location location;
    private float foodSaturation = 1f; //MAX_FOOD_SATURATION;
    public boolean isAlreadyTurned;

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
        foodSaturation -= 0.05f;
        isAlreadyTurned = true;
        isDied();
    }

    @Override
    public void moveDirection() {
        int[] thisCoordinates = location.getLocationCoordinates();
        Location newLocation = MoveDirection.getNewLocation(location);

        System.out.println(thisCoordinates[0] + "|" + thisCoordinates[1] + " коорд лисы старые");

        if (newLocation != location &&
                newLocation.foxPopulation < newLocation.getMaxPopulation().get("maxFoxPopulation")) {
                location.animalLeave(this, "foxPopulation");
                this.location = newLocation;
                newLocation.animalArrive(this, "foxPopulation");

        }

        System.out.println(this.location.getLocationCoordinates()[0] + "|" + this.location.getLocationCoordinates()[1] + " коорд лисы новые");
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
