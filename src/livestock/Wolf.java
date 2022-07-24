package livestock;

public class Wolf extends Predator {
    public static final int WEIGHT = 50;
    public static final int MAX_AREA_POPULATION = 30;
    public static final int MAX_AREA_MOVE_SPEED = 3;
    public static final int MAX_FOOD_SATURATION = 8;
    private int foodSaturation = 0;
    public MoveDirection moveDirection = MoveDirection.randomDirection();

}
