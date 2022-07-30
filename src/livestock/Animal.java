package livestock;

/**
 * The Animal class is the common parent for all animals on the island.
 */
public abstract class Animal {

    public abstract void move();

    public abstract void moveDirection();

    public abstract void breed();

    public abstract void isDied();
}
