package island;

import livestock.Animal;
import livestock.Plant;
import livestock.herbivores.*;
import livestock.predators.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private final Map<String, Integer> population = new HashMap<>();

    private final CopyOnWriteArrayList<Herbivore> herbivores = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Predator> predators = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Plant> plants = new CopyOnWriteArrayList<>();

    public Location(Properties appProp, Location[][] islandMap, int[] locationCoordinates) {
        this.appProp = appProp;
        this.islandMap = islandMap;
        this.locationCoordinates = locationCoordinates;
        this.maxPopulation.put("maxPlantPopulation", Integer.parseInt(appProp.getProperty("PlantPopulationMax")));
        this.maxPopulation.put("maxBoaPopulation", Integer.parseInt(appProp.getProperty("BoaPopulationMax")));
        this.maxPopulation.put("maxBoarPopulation", Integer.parseInt(appProp.getProperty("BoarPopulationMax")));
        this.maxPopulation.put("maxBuffaloPopulation", Integer.parseInt(appProp.getProperty("BuffaloPopulationMax")));
        this.maxPopulation.put("maxCaterpillarPopulation", Integer.parseInt(appProp.getProperty("CaterpillarPopulationMax")));
        this.maxPopulation.put("maxDeerPopulation", Integer.parseInt(appProp.getProperty("DeerPopulationMax")));
        this.maxPopulation.put("maxDuckPopulation", Integer.parseInt(appProp.getProperty("DuckPopulationMax")));
        this.maxPopulation.put("maxFoxPopulation", Integer.parseInt(appProp.getProperty("FoxPopulationMax")));
        this.maxPopulation.put("maxGoatPopulation", Integer.parseInt(appProp.getProperty("GoatPopulationMax")));
        this.maxPopulation.put("maxHorsePopulation", Integer.parseInt(appProp.getProperty("HorsePopulationMax")));
        this.maxPopulation.put("maxMousePopulation", Integer.parseInt(appProp.getProperty("MousePopulationMax")));
        this.maxPopulation.put("maxRabbitPopulation", Integer.parseInt(appProp.getProperty("RabbitPopulationMax")));
        this.maxPopulation.put("maxSheepPopulation", Integer.parseInt(appProp.getProperty("SheepPopulationMax")));
        this.maxPopulation.put("maxWolfPopulation", Integer.parseInt(appProp.getProperty("WolfPopulationMax")));
        initialize();
    }

    public Properties getAppProp() {
        return appProp;
    }

    public Location[][] getIslandMap() {
        return islandMap;
    }

    public int[] getLocationCoordinates() {
        return locationCoordinates;
    }

    public Map<String, Integer> getMaxPopulation() {
        return maxPopulation;
    }

    public Map<String, Integer> getPopulation() {
        return population;
    }

    public CopyOnWriteArrayList<Herbivore> getHerbivores() {
        return herbivores;
    }

    public CopyOnWriteArrayList<Predator> getPredators() {
        return predators;
    }

    public CopyOnWriteArrayList<Plant> getPlants() {
        return plants;
    }

    private synchronized void changePopulation(String populationType, int quantity) {
        population.computeIfPresent(populationType, (key, value) -> (value + quantity));
    }

    //location livestock initialization
    private void initialize() {
        growPlants();
        population.put("boaPopulation",
                initializePredators(Boa.class, maxPopulation.get("maxBoaPopulation")));
        population.put("boarPopulation",
                initializeHerbivores(Boar.class, maxPopulation.get("maxBoarPopulation")));
        population.put("buffaloPopulation",
                initializeHerbivores(Buffalo.class, maxPopulation.get("maxBuffaloPopulation")));
        population.put("caterpillarPopulation",
                initializeHerbivores(Caterpillar.class, maxPopulation.get("maxCaterpillarPopulation")));
        population.put("deerPopulation",
                initializeHerbivores(Deer.class, maxPopulation.get("maxDeerPopulation")));
        population.put("duckPopulation",
                initializeHerbivores(Duck.class, maxPopulation.get("maxDuckPopulation")));
        population.put("foxPopulation",
                initializePredators(Fox.class, maxPopulation.get("maxFoxPopulation")));
        population.put("goatPopulation",
                initializeHerbivores(Goat.class, maxPopulation.get("maxGoatPopulation")));
        population.put("horsePopulation",
                initializeHerbivores(Horse.class, maxPopulation.get("maxHorsePopulation")));
        population.put("mousePopulation",
                initializeHerbivores(Mouse.class, maxPopulation.get("maxMousePopulation")));
        population.put("rabbitPopulation",
                initializeHerbivores(Rabbit.class, maxPopulation.get("maxRabbitPopulation")));
        population.put("sheepPopulation",
                initializeHerbivores(Sheep.class, maxPopulation.get("maxSheepPopulation")));
        population.put("wolfPopulation",
                initializePredators(Wolf.class, maxPopulation.get("maxWolfPopulation")));
    }

    private int initializeHerbivores(Class<?> herbivoreClass, int maxPopulation) {
        int population = ThreadLocalRandom.current().nextInt(maxPopulation + 1);
        for (int i = 0; i < population; i++) {
            try {
                herbivores.add((Herbivore) herbivoreClass
                        .getConstructor(Location.class)
                        .newInstance(this));
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
                        .getConstructor(Location.class)
                        .newInstance(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return population;
    }

    @Override
    public void run() {
        ThreadLocalRandom animalCase = ThreadLocalRandom.current();
        //predators randomly eats/moves/breeds
        for (int i = 0; i < predators.size(); i++) {
            Predator predator = predators.get(i);

            if (!predator.isMoved()) {
                switch (animalCase.nextInt(3)) {
                    case 0 -> predator.breed();
                    case 1 -> predator.eat(herbivores);
                    case 2 -> predator.move();
                }
            } else {
                predator.setIsMoved(false);
            }
        }

        //herbivores randomly eats/moves/breeds
        for (int i = 0; i < herbivores.size(); i++) {
            Herbivore herbivore = herbivores.get(i);

            if (!herbivore.isMoved()) {
                switch (animalCase.nextInt(3)) {
                    case 0 -> herbivore.breed();
                    case 1 -> herbivore.eat(plants);
                    case 2 -> herbivore.move();
                }
            } else {
                herbivore.setIsMoved(false);
            }
        }
        growPlants();
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

    private void growPlants() {
        int random = ThreadLocalRandom.current().nextInt(maxPopulation.get("maxPlantPopulation") + 1);
        plants.clear();
        for (int i = 0; i < random; i++) {
            plants.add(new Plant());
        }
    }

    @Override
    public String toString() {
        return " location: " +
                "herbivores=" + herbivores.size() +
                ": \uD83D\uDC17Boars=" + population.get("boarPopulation") +
                " | \uD83D\uDC03Buffalo=" + population.get("buffaloPopulation") +
                " | \uD83D\uDC1BCaterplr=" + population.get("caterpillarPopulation") +
                " | \uD83E\uDD8CDeers=" + population.get("deerPopulation") +
                " | \uD83E\uDD86Ducks=" + population.get("duckPopulation") +
                " | \uD83D\uDC10Goats=" + population.get("goatPopulation") +
                " | \uD83D\uDC0EHorses=" + population.get("horsePopulation") +
                " | \uD83D\uDC01Mouses=" + population.get("mousePopulation") +
                " | \uD83D\uDC07Rabbits=" + population.get("rabbitPopulation") +
                " | \uD83D\uDC11Sheep=" + population.get("sheepPopulation") +
                "\n\t\t\t    predators=" + predators.size() +
                ":  \uD83D\uDC0DBoas=" + population.get("boaPopulation") +
                " | \uD83E\uDD8AFoxes=" + population.get("foxPopulation") +
                " | \uD83D\uDC3AWolves=" + population.get("wolfPopulation") +
                "\n\t\t\t   \uD83C\uDF3Fplants=" + plants.size() +
                "}\n";
    }
}
