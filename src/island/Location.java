package island;

import livestock.Plant;
import livestock.herbivores.Deer;
import livestock.herbivores.Herbivore;
import livestock.predators.Predator;

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

    List<Herbivore> herbivores = new ArrayList<>();
    List<Predator> predators = new ArrayList<>();
    List<Plant> plants = new ArrayList<>();

    public Location(Properties appProp) {
        this.appProp = appProp;
        this.maxPlantPopulation = Integer.parseInt(appProp.getProperty("PlantPopulationMax"));
        this.maxDeerPopulation = Integer.parseInt(appProp.getProperty("DeerPopulationMax"));
        initialize();
    }

    //location livestock initialization
    public void initialize() {
        int random = new Random().nextInt(maxPlantPopulation);
        for (int i = 0; i < random; i++) {
            plants.add(new Plant());
        }
        random = new Random().nextInt(maxDeerPopulation);
        for (int i = 0; i < random; i++) {
            herbivores.add(new Deer());
        }
    }

    //events taking place in the location
    public void recalculate() {

        // В ЦИКЛЕ ПЕРЕБИРАЕМ ХИЩНИКОВ ИЗ ЛИСТА
        // И КАЖДОМУ ПО ОЧЕРЕДИ СУЕМ СПИСОК ТРАВОЯДНЫХ
        for (int i = 0; i < predators.size(); i++) {
            Predator predator = predators.get(i);
            predator.eat(herbivores);

            // РАЗМНОЖАЕМСЯ
            predator.breed();

            // ДВИГАЕМСЯ
            predator.moveDirection();
        }

        // ТО ЖЕ САМОЕ ДЕЛАЕМ ДЛЯ ТРАВОЯДНЫХ

        for (int i = 0; i < herbivores.size(); i++) {
            // ... //
        }

    }

    @Override
    public String toString() {
        return "Location{" +
                "herbivores=" + herbivores.size() +
                ", predators=" + predators.size() +
                ", plants=" + plants.size() +
                '}';
    }
}
