import bagel.*;
import java.util.Random;
public class Enemy {
    private static final Image image = new Image("res/enemy.png");
    private static final int X_UPPER_BOUNDARY = 900;
    private static final int Y_UPPER_BOUNDARY = 500;
    private static final int XY_LOWER_BOUNDARY = 100;
    public static final int ENEMY_HITBOX = 104;
    private final int speed = 1;
    private int x;
    private int y;
    private Random rand = new Random();
    private String dir;
    private boolean active = false;
    private boolean completed = false;

    /**
     * A constructor for Enemy that randomize 3 things.
     * If it is going to go left or right
     * The x that it will be drawn to (100 - 900)
     * The y that it will be drawn to (100 - 500)
     */
    public Enemy() {
        int leftOrRight = rand.nextInt(2);
        if (leftOrRight == 0) {
            dir = "Left";
        } else {
            dir = "Right";
        }
        x = XY_LOWER_BOUNDARY +
                rand.nextInt(X_UPPER_BOUNDARY + 1 - XY_LOWER_BOUNDARY);
        y = XY_LOWER_BOUNDARY +
                rand.nextInt(Y_UPPER_BOUNDARY + 1 - XY_LOWER_BOUNDARY);
    }

    /**
     * Method that check if the enemy is active or not
     * @return boolean this is the active state of enemy
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method that check if the enemy is completed or not
     * @return boolean this is the completed state of enemy
     */
    public boolean isCompleted() {return completed;}

    /**
     * Method that deactivates the enemy
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Method that updates the x of the enemy according to its direction.
     * Reverse the direction if it reached the X axis boundary (100 and 900).
     * Allows to make the enemy inactive if it is completed.
     */
    public void update() {
        if (active) {
            switch (dir) {
                case "Left":
                    if (x - speed < XY_LOWER_BOUNDARY) {
                        goingRight();
                        dir = "Right";
                    } else {
                        goingLeft();
                    }
                    break;
                case "Right":
                    if (x + speed > X_UPPER_BOUNDARY) {
                        goingLeft();
                        dir = "Left";
                    } else {
                        goingRight();
                    }
                    break;
            }
        }
        if (!completed) {
            active = true;
        }
    }

    /**
     * Method that draws the enemy
     */
    public void draw() {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Method that makes the enemy go left
     */
    public void goingLeft() {
        x -= speed;
    }

    /**
     * Method that makes the enemy go right
     */
    public void goingRight() {
        x += speed;
    }

    /**
     * Method that gets the enemy x
     * @return int this is the enemy's x
     */
    public int getEnemyX() {
        return x;
    }

    /**
     * Method that gets the enemy y
     * @return int this is the enemy's y
     */
    public int getEnemyY() {
        return y;
    }
}
