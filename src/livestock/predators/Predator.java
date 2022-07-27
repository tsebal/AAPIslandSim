package livestock.predators;

import livestock.Animal;
import livestock.herbivores.Herbivore;

import java.util.List;

/**
 * The Predator class is the common parent for all predators on the island.
 */
public abstract class Predator extends Animal {

    public abstract void eat(List<Herbivore> herbivores);
}
