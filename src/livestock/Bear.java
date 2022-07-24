package livestock;

public class Bear extends Predator {
    public static final int WEIGHT = 500;
    public static final int MAX_AREA_POPULATION = 5;
    public static final int MAX_AREA_MOVE_SPEED = 2;
    public static final int MAX_FOOD_SATURATION = 80;
    private int foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();
}
