import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
import java.lang.Math;

public class Arrow {
    private static final Image image = new Image("res/arrow.png");
    private static final int ARROW_HITBOX = 62;
    private final double angle;
    private Point currPosition;
    private Point targetPosition;
    private Enemy target;
    private Vector2 track;
    private int x;
    private int y;
    private int speed = 6;
    private boolean active = false;
    private boolean completed = false;

    /**
     * A constructor for Arrow which initialize the current position,
     * target, target position, track/course of the arrow, and angle
     * @param guardian this is guardian which the Arrow originates
     * @param enemy this is the target of this arrow
     * @param angle the angle of the rotation that the arrow has
     */
    public Arrow(Guardian guardian, Enemy enemy, double angle) {
        active = true;
        target = enemy;
        currPosition = new Point(guardian.getGuardianX(), guardian.getGuardianY());
        targetPosition = new Point(target.getEnemyX(), target.getEnemyY());
        this.angle = angle;
        track = new Vector2(targetPosition.x - currPosition.x, targetPosition.y - currPosition.y);
        track = track.normalised();
    }

    /**
     * Method that draws the active Arrow with its rotation
     */
    public void draw() {
        if (active) {
            DrawOptions drawOptions = new DrawOptions();
            drawOptions.setRotation(angle);
            image.draw(currPosition.x, currPosition.y, drawOptions);
        }
    }

    /**
     * Method that updates the current position with the new projection.
     * Allows the arrow to terminate the enemy in hitbox range
     */
    public void update() {
        if (active) {
            Vector2 currProjection = currPosition.asVector();
            currProjection = currProjection.add(track.mul(speed));
            currPosition = currProjection.asPoint();
            terminateEnemy();
        }

        if (!completed && currPosition.x <= Window.getWidth() && currPosition.y <= Window.getHeight()) {
            active = true;
        }
    }

    /**
     * Method that perform the termination of the enemy by calculating the distance
     */
    public void terminateEnemy() {
        if (currPosition.distanceTo(targetPosition) <= ARROW_HITBOX) {
            target.deactivate();
            deactivate();
        }
    }

    /**
     * Method that deactivate the Arrow
     */
    public void deactivate() {
        active = false;
        completed = true;
    }
}
