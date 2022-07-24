package livestock;

public class Boa extends Predator {
    public static final int WEIGHT = 15;
    public static final int MAX_AREA_POPULATION = 30;
    public static final int MAX_AREA_MOVE_SPEED = 1;
    public static final int MAX_FOOD_SATURATION = 3;
    private int foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();
}
