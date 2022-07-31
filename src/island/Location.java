package island;

import livestock.Animal;
import livestock.Plant;
import livestock.herbivores.*;
import livestock.predators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * Location (one cell of the island) where the main action takes place.
 * Contains lists of herbivores, predators, plants and events.
 */
public class Location {
    Properties appProp;
    public Location[][] islandMap;
    private int[] locationCoordinates;
    private int maxPlantPopulation;
    private int maxDeerPopulation;
    private int maxMousePopulation;
    private int maxFoxPopulation;
    private int maxWolfPopulation;

    public int deerPopulation;
    public int mousePopulation;
    public int foxPopulation;
    public int wolfPopulation;

    public List<Herbivore> herbivores = new ArrayList<>();
    public List<Predator> predators = new ArrayList<>();
    public List<Plant> plants = new ArrayList<>();

    public Location(Properties appProp, Location[][] islandMap, int[] locationCoordinates) {
        this.appProp = appProp;
        this.islandMap = islandMap;
        this.locationCoordinates = locationCoordinates;
        this.maxPlantPopulation = Integer.parseInt(appProp.getProperty("PlantPopulationMax"));
        this.maxDeerPopulation = Integer.parseInt(appProp.getProperty("DeerPopulationMax"));
        this.maxMousePopulation = Integer.parseInt(appProp.getProperty("MousePopulationMax"));
        this.maxFoxPopulation = Integer.parseInt(appProp.getProperty("FoxPopulationMax"));
        this.maxWolfPopulation = Integer.parseInt(appProp.getProperty("WolfPopulationMax"));
        initialize();
    }

    public int[] getLocationCoordinates() {
        return locationCoordinates;
    }

    //location livestock initialization
    private void initialize() {
        int random = new Random().nextInt(maxPlantPopulation);
        for (int i = 0; i < random; i++) {
            plants.add(new Plant());
        }
        deerPopulation = initializeHerbivores(Deer.class, maxDeerPopulation);
        mousePopulation = initializeHerbivores(Mouse.class, maxMousePopulation);
        foxPopulation = initializePredators(Fox.class, maxFoxPopulation);
        wolfPopulation = initializePredators(Wolf.class, maxWolfPopulation);

    }

    private int initializeHerbivores(Class<?> herbivoreClass, int maxPopulation) {
        int population = new Random().nextInt(maxPopulation + 1);
        for (int i = 0; i < population; i++) {
            try {
                herbivores.add((Herbivore) herbivoreClass.getConstructor(Location.class).newInstance(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return population;
    }

    private int initializePredators(Class<?> predatorClass, int maxPopulation) {
        int population = new Random().nextInt(maxPopulation + 1);
        for (int i = 0; i < population; i++) {
            try {
                predators.add((Predator) predatorClass.getConstructor(Location.class).newInstance(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return population;
    }

    //events taking place in the location
    public void recalculate() {

        //predators eats herbivores
        for (int i = 0; i < predators.size(); i++) {
            Predator predator = predators.get(i);
            predator.eat(herbivores);

            // РАЗМНОЖАЕМСЯ
            //predator.breed();

            // ДВИГАЕМСЯ
            predator.move();
        }

        //herbivores eats plants, mouses, caterpillars
        for (int i = 0; i < herbivores.size(); i++) {
            Herbivore herbivore = herbivores.get(i);
            herbivore.eat(plants);
        }

    }

    public void animalLeave(Animal animal, String populationField) {
        if (animal instanceof Predator) {
            predators.remove(animal);
        } else if (animal instanceof Herbivore) {
            herbivores.remove(animal);
        }
        //changes population field in this location
        try {
            int locationAnimalPopulation = this.getClass().getDeclaredField(populationField).getInt(this);
            System.out.println("Было здесь лис = " + locationAnimalPopulation);
            this.getClass().getDeclaredField(populationField).setInt(this, locationAnimalPopulation - 1);
            System.out.println("Стало здесь лис = " + this.foxPopulation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void animalArrive(Animal animal, int[] newCoordinates, String populationField) {
        Location newLocation = islandMap[newCoordinates[0]][newCoordinates[1]];
        if (animal instanceof Predator) {
            newLocation.predators.add((Predator) animal);
        } else if (animal instanceof Herbivore) {
            newLocation.herbivores.add((Herbivore) animal);
        }
        //changes population field in new location
        try {
            int locationAnimalPopulation = newLocation.getClass().getDeclaredField(populationField).getInt(newLocation);
            System.out.println("Было там лис = " + locationAnimalPopulation);
            newLocation.getClass().getDeclaredField(populationField).setInt(newLocation, locationAnimalPopulation + 1);
            System.out.println("Стало там лис = " + newLocation.getClass().getDeclaredField(populationField).getInt(newLocation));
            animal.getClass().getDeclaredField("location").set(animal, newLocation);
            System.out.println("Локация лисы новая: " + animal.getClass().getDeclaredField("location").get(animal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Location{" +
                "herbivores=" + herbivores.size() +
                ": \uD83E\uDD8CDeers=" + deerPopulation + " | \uD83D\uDC01Mouses=" + mousePopulation +
                "\n\t\t\t  predators=" + predators.size() +
                ": \uD83E\uDD8AFoxes=" + foxPopulation + " | \uD83D\uDC3AWolves=" + wolfPopulation +
                "\n\t\t\t \uD83C\uDF3Fplants=" + plants.size() +
                "}\n";
    }
}
