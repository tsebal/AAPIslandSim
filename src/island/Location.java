package island;

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

    public Location(Properties appProp) {
        this.appProp = appProp;
        this.maxPlantPopulation = Integer.parseInt(appProp.getProperty("PlantPopulationMax"));
        this.maxDeerPopulation = Integer.parseInt(appProp.getProperty("DeerPopulationMax"));
        this.maxMousePopulation = Integer.parseInt(appProp.getProperty("MousePopulationMax"));
        this.maxFoxPopulation = Integer.parseInt(appProp.getProperty("FoxPopulationMax"));
        this.maxWolfPopulation = Integer.parseInt(appProp.getProperty("WolfPopulationMax"));
        initialize();
    }

    //location livestock initialization
    private void initialize() {
        int random = new Random().nextInt(maxPlantPopulation);
        for (int i = 0; i < random; i++) {
            plants.add(new Plant());
        }
        deerPopulation = initializeHerbivores(new Deer(), maxDeerPopulation);
        mousePopulation = initializeHerbivores(new Mouse(), maxMousePopulation);
        foxPopulation = initializePredators(new Fox(this), maxFoxPopulation);
        wolfPopulation = initializePredators(new Wolf(), maxWolfPopulation);

    }

    private int initializeHerbivores(Herbivore herbivore, int maxPopulation) {
        int population = new Random().nextInt(maxPopulation);
        for (int i = 0; i < population; i++) {
            herbivores.add(herbivore);
        }
        return population;
    }

    private int initializePredators(Predator predator, int maxPopulation) {
        int population = new Random().nextInt(maxPopulation);
        for (int i = 0; i < population; i++) {
            predators.add(predator);
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
            //predator.moveDirection();
        }

        //herbivores eats plants, mouses, caterpillars
        for (int i = 0; i < herbivores.size(); i++) {
            Herbivore herbivore = herbivores.get(i);
            herbivore.eat(plants);
        }

    }

    @Override
    public String toString() {
        return "Location{" +
                "herbivores=" + herbivores.size() +
                ": Deers=" + deerPopulation + " | Mouses=" + mousePopulation +
                "\n\t\t predators=" + predators.size() +
                ": Foxes=" + foxPopulation + " | Wolves=" + wolfPopulation +
                "\n\t\t plants=" + plants.size() +
                '}';
    }
}
