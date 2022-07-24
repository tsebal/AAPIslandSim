package livestock;

public class Eagle extends Predator {
    public static final int WEIGHT = 6;
    public static final int MAX_AREA_POPULATION = 20;
    public static final int MAX_AREA_MOVE_SPEED = 3;
    public static final int MAX_FOOD_SATURATION = 1;
    private int foodSaturation = 0;
    private MoveDirection moveDirection = MoveDirection.randomDirection();
}
