import bagel.*;
import java.lang.Math;

public class Guardian {
    private static final Image image = new Image("res/guardian.png");
    private static final int GUARDIAN_X = 800;
    private static final int GUARDIAN_Y = 600;
    private static final double RIGHT_ANGLE_IN_RADIAN = Math.toRadians(90);
    public final Keys relevantKey = Keys.LEFT_SHIFT;

    /**
     * Method that draws the Guardian
     */
    public void draw() {
        image.draw(GUARDIAN_X, GUARDIAN_Y);
    }

    /**
     * Method that calculates the angle of the arrow.
     * @param distance this is the distance from guardian to nearest enemy
     * @param yDistance this is the y distance from guardian to nearest enemy
     * @return double this is the angle of the arrow
     */
    public double shootArrow(double distance, double yDistance) {
        double sinB = Math.sin(RIGHT_ANGLE_IN_RADIAN);
        double sinA = yDistance * sinB / distance;
        return Math.asin(sinA) - Math.PI;
    }

    /**
     * Method that gets the x of the guardian
     * @return int this is the x of the guardian
     */
    public int getGuardianX() {
        return GUARDIAN_X;
    }

    /**
     * Method that gets the y of the guardian
     * @return int this is the y of the guardian
     */
    public int getGuardianY() {
        return GUARDIAN_Y;
    }
}
