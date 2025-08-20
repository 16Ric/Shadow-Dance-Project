import bagel.Font;
import bagel.Window;

public class SpecialAccuracy extends Accuracy {
    public static final int NOT_SCORED = 0;
    public static final int NOT_ACTIVATED = -1;
    public static final int ACTIVATED = 1;
    private static final int SPECIAL_RADIUS = 50;

    /**
     * Method that evaluate the distance between height and target height
     * @param height this is the height of the special note or bomb note
     * @param targetHeight this is the target that is supposed to be hit
     * @param triggered this is the flag if the relevant key is pressed or not
     * @return int this is the flag if it is activated, not activated, or is not triggered
     */
    public int evaluateEffect(String message, int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= SPECIAL_RADIUS) {
                setAccuracy(message);
                return ACTIVATED;
            }

        } else if (height >= (Window.getHeight())) {
            return NOT_ACTIVATED;
        }

        return NOT_SCORED;

    }
}
