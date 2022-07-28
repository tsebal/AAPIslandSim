package livestock.herbivores;

import livestock.Animal;
import livestock.Plant;

import java.util.List;

/**
 * The Herbivore class is the common parent for all herbivores animals on the island.
 */
public abstract class Herbivore extends Animal {

    public abstract void eat(List<Plant> plant);
}
