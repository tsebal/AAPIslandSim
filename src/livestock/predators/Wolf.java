package livestock.predators;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.herbivores.Deer;
import livestock.herbivores.Herbivore;
import livestock.herbivores.Mouse;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Wolf chance eats: Horse 10%, Deer 15%, Rabbit 60%, Mouse 80%, Goat 60%, Sheep 70%, Boar 15%, Buffalo 10%, Duck 40%
 */
public class Wolf extends Predator {
    private final Properties appProp;
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static int MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    public boolean isMoved;

    public Wolf(Location location, Properties appProp) {
        this.appProp = appProp;
        this.location = location;
        WEIGHT = Integer.parseInt(appProp.getProperty("WolfWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("WolfAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Integer.parseInt(appProp.getProperty("WolfFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("WolfBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("WolfFoodSaturation"));
    }

    @Override
    public void eat(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Deer &&
                        EatingChance.isEated(this, herbivore)) {
                    System.out.println("Wolf eats Deer");
                    location.animalLeave(herbivore, "deerPopulation");
                    foodSaturation = MAX_FOOD_SATURATION;
                    return;
                } else if (herbivore instanceof Mouse &&
                        EatingChance.isEated(this, herbivore)) {
                    System.out.println("Wolf eats Mouse");
                    location.animalLeave(herbivore, "mousePopulation");
                    foodSaturation += 0.05f;
                    return;
                }
            }
            System.out.println("Wolf is hungry, no more herbivores there!");
            foodSaturation -= 1;
            isDied();
        }
    }

    @Override
    public void move() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        for (int i = 0; i < moveSpeed; i++) {
            System.out.println("Wolf moves " + (i + 1) + " times");
            moveDirection();
        }
        isMoved = true;
        foodSaturation -= 1;
        isDied();
    }

    @Override
    public void moveDirection() {
        Location newLocation = MoveDirection.getNewLocation(location);

        if (newLocation != location &&
                newLocation.getPopulation().get("wolfPopulation") < newLocation.getMaxPopulation().get("maxWolfPopulation")) {
            location.animalLeave(this, "wolfPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "wolfPopulation");
        }
    }

    @Override
    public void breed() {
        int locationWolfPopulation = location.getPopulation().get("wolfPopulation");
        if (locationWolfPopulation / BREED_FACTOR >= 2 &&
                locationWolfPopulation < location.getMaxPopulation().get("maxWolfPopulation")) {
            Wolf newWolf = new Wolf(location, appProp);
            newWolf.isMoved = true;
            location.animalArrive(newWolf, "wolfPopulation");
            System.out.println("A new wolf was born.");
        } else {
            System.out.println("The wolf could not breed.");
        }
        foodSaturation -= 1;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            System.out.println("The wolf died hungry.");
            location.animalLeave(this, "wolfPopulation");
        }
    }
}
