package livestock.herbivores;

import island.Location;
import livestock.EatingChance;
import livestock.MoveDirection;
import livestock.Plant;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Boar extends Herbivore implements EatsHerbivores {
    private Location location;
    private static int WEIGHT;
    private static int MAX_AREA_MOVE_SPEED;
    private static float MAX_FOOD_SATURATION;
    private static int BREED_FACTOR;
    private float foodSaturation;
    private boolean isMoved;

    public Boar(Location location) {
        this.location = location;
        Properties appProp = location.getAppProp();
        WEIGHT = Integer.parseInt(appProp.getProperty("BoarWeight"));
        MAX_AREA_MOVE_SPEED = Integer.parseInt(appProp.getProperty("BoarAreaMoveSpeed"));
        MAX_FOOD_SATURATION = Float.parseFloat(appProp.getProperty("BoarFoodSaturationMax"));
        BREED_FACTOR = Integer.parseInt(appProp.getProperty("BoarBreedFactor"));
        this.foodSaturation = Float.parseFloat(appProp.getProperty("BoarFoodSaturation"));
        this.isMoved = false;
    }

    @Override
    public boolean isMoved() {
        return isMoved;
    }

    @Override
    public void setIsMoved(boolean isMoved) {
        this.isMoved = isMoved;
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

    @Override
    public void eat(List<Plant> plant) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            int boarChoosesFood = ThreadLocalRandom.current().nextInt(2);

            switch (boarChoosesFood) {
                case 0 -> {
                    if (!plant.isEmpty()) {
                        plant.remove(0);
                        foodSaturation += 1;
                    }
                }
                case 1 -> eatHerbivore(location.getHerbivores());
            }
        } else {
            foodSaturation -= 1;
            isDied();
        }
    }

    @Override
    public void eatHerbivore(List<Herbivore> herbivores) {
        if (foodSaturation < MAX_FOOD_SATURATION) {
            for (Herbivore herbivore : herbivores) {
                if (herbivore instanceof Caterpillar &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "caterpillarPopulation");
                    foodSaturation += herbivore.getWeight();
                    return;
                } else if (herbivore instanceof Mouse &&
                        EatingChance.isEated(this, herbivore)) {
                    location.animalLeave(herbivore, "mousePopulation");
                    foodSaturation += herbivore.getWeight();
                    return;
                }
            }
            foodSaturation -= 1;
            isDied();
        }
    }

    @Override
    public void move() {
        moveDirection();
        setIsMoved(true);
        foodSaturation -= 2;
        isDied();
    }

    @Override
    public void moveDirection() {
        int moveSpeed = ThreadLocalRandom.current().nextInt(MAX_AREA_MOVE_SPEED + 1);
        Location newLocation = MoveDirection.getNewLocation(location, moveSpeed);

        if (newLocation != location &&
                newLocation.getPopulation().get("boarPopulation") < newLocation.getMaxPopulation().get("maxBoarPopulation")) {
            location.animalLeave(this, "boarPopulation");
            this.location = newLocation;
            newLocation.animalArrive(this, "boarPopulation");
        }
    }

    @Override
    public void breed() {
        int locationBoarPopulation = location.getPopulation().get("boarPopulation");
        if (locationBoarPopulation / BREED_FACTOR >= 2 &&
                locationBoarPopulation < location.getMaxPopulation().get("maxBoarPopulation")) {
            Boar newBoar = new Boar(location);
            newBoar.setIsMoved(true);
            location.animalArrive(newBoar, "boarPopulation");
        }
        foodSaturation -= 4;
        isDied();
    }

    @Override
    public void isDied() {
        if (foodSaturation < 0) {
            location.animalLeave(this, "boarPopulation");
        }
    }
}
