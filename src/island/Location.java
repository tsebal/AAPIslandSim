package island;

import livestock.Animal;
import livestock.Plant;
import livestock.herbivores.*;
import livestock.predators.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Location (one cell of the island) where the main action takes place.
 * Contains lists of herbivores, predators, plants and events.
 */
public class Location implements Runnable {
    private final Properties appProp;
    private final Location[][] islandMap;
    private final int[] locationCoordinates;
    private final Map<String, Integer> maxPopulation = new HashMap<>();
    private volatile Map<String, Integer> population = new HashMap<>();

    public volatile List<Herbivore> herbivores = new ArrayList<>();
    public volatile List<Predator> predators = new ArrayList<>();
    public volatile List<Plant> plants = new ArrayList<>();

    public Location(Properties appProp, Location[][] islandMap, int[] locationCoordinates) {
        this.appProp = appProp;
        this.islandMap = islandMap;
        this.locationCoordinates = locationCoordinates;
        this.maxPopulation.put("maxPlantPopulation", Integer.parseInt(appProp.getProperty("PlantPopulationMax")));
        this.maxPopulation.put("maxDeerPopulation", Integer.parseInt(appProp.getProperty("DeerPopulationMax")));
        this.maxPopulation.put("maxDuckPopulation", Integer.parseInt(appProp.getProperty("DuckPopulationMax")));
        this.maxPopulation.put("maxFoxPopulation", Integer.parseInt(appProp.getProperty("FoxPopulationMax")));
        this.maxPopulation.put("maxMousePopulation", Integer.parseInt(appProp.getProperty("MousePopulationMax")));
        this.maxPopulation.put("maxWolfPopulation", Integer.parseInt(appProp.getProperty("WolfPopulationMax")));
        initialize();
    }

    public int[] getLocationCoordinates() {
        return locationCoordinates;
    }

    public Location[][] getIslandMap() {
        return islandMap;
    }

    public Map<String, Integer> getMaxPopulation() {
        return maxPopulation;
    }

    public Map<String, Integer> getPopulation() {
        return population;
    }

    public void changePopulation(String populationType, int quantity) {
        population.computeIfPresent(populationType, (key, value) -> (value + quantity));
    }

    //location livestock initialization
    private void initialize() {
        int random = ThreadLocalRandom.current().nextInt(maxPopulation.get("maxPlantPopulation") + 1);
        for (int i = 0; i < random; i++) {
            plants.add(new Plant());
        }
        population.put("deerPopulation",
                initializeHerbivores(Deer.class, maxPopulation.get("maxDeerPopulation")));
        population.put("duckPopulation",
                initializeHerbivores(Duck.class, maxPopulation.get("maxDuckPopulation")));
        population.put("foxPopulation",
                initializePredators(Fox.class, maxPopulation.get("maxFoxPopulation")));
        population.put("mousePopulation",
                initializeHerbivores(Mouse.class, maxPopulation.get("maxMousePopulation")));
        population.put("wolfPopulation",
                initializePredators(Wolf.class, maxPopulation.get("maxWolfPopulation")));
    }

    private int initializeHerbivores(Class<?> herbivoreClass, int maxPopulation) {
        int population = ThreadLocalRandom.current().nextInt(maxPopulation + 1);
        for (int i = 0; i < population; i++) {
            try {
                herbivores.add((Herbivore) herbivoreClass
                        .getConstructor(Location.class, Properties.class)
                        .newInstance(this, this.appProp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return population;
    }

    private int initializePredators(Class<?> predatorClass, int maxPopulation) {
        int population = ThreadLocalRandom.current().nextInt(maxPopulation + 1);
        for (int i = 0; i < population; i++) {
            try {
                predators.add((Predator) predatorClass
                        .getConstructor(Location.class, Properties.class)
                        .newInstance(this, this.appProp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return population;
    }

    @Override
    public void run() {
        ThreadLocalRandom animalCase = ThreadLocalRandom.current();
        Field isMoved;

        //predators randomly eats/moves/breeds
        for (int i = 0; i < predators.size(); i++) {
            Predator predator = predators.get(i);

            try {
                isMoved = predator.getClass().getDeclaredField("isMoved");
                if (!isMoved.getBoolean(predator)) {
                    switch (animalCase.nextInt(3)) {
                        case 0 -> predator.breed();
                        case 1 -> predator.eat(herbivores);
                        case 2 -> predator.move();
                    }
                } else {
                    isMoved.setBoolean(predator, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //herbivores eats plants, mouses, caterpillars
        for (int i = 0; i < herbivores.size(); i++) {
            Herbivore herbivore = herbivores.get(i);

            try {
                isMoved = herbivore.getClass().getDeclaredField("isMoved");
                if (!isMoved.getBoolean(herbivore)) {
                    switch (animalCase.nextInt(3)) {
                        case 0 -> herbivore.breed();
                        case 1 -> herbivore.eat(plants);
                        case 2 -> herbivore.move();
                    }
                } else {
                    isMoved.setBoolean(herbivore, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void animalLeave(Animal animal, String populationType) {
        if (animal instanceof Predator) {
            predators.remove(animal);
        } else if (animal instanceof Herbivore) {
            herbivores.remove(animal);
        }
        changePopulation(populationType, -1);
    }

    public synchronized void animalArrive(Animal animal, String populationType) {
        if (animal instanceof Predator) {
            predators.add((Predator) animal);
        } else if (animal instanceof Herbivore) {
            herbivores.add((Herbivore) animal);
        }
        changePopulation(populationType, +1);
    }

    @Override
    public String toString() {
        return "Location{" +
                "herbivores=" + herbivores.size() +
                ": \uD83E\uDD8CDeers=" + population.get("deerPopulation") +
                " | \uD83D\uDC01Mouses=" + population.get("mousePopulation") +
                " | \uD83E\uDD86Ducks=" + population.get("duckPopulation") +
                "\n\t\t\t  predators=" + predators.size() +
                ": \uD83E\uDD8AFoxes=" + population.get("foxPopulation") +
                " | \uD83D\uDC3AWolves=" + population.get("wolfPopulation") +
                "\n\t\t\t \uD83C\uDF3Fplants=" + plants.size() +
                "}\n";
    }
}
