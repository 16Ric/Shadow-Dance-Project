import bagel.*;

public class Accuracy {
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;

    /**
     * Method that sets the accuracy message and initialize the frame count again.
     * @param accuracy this is the message that will be drawn
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * Method that reset the accuracy.
     */
    public void resetMessage() {
        currAccuracy = null;
    }

    /**
     * Method that draws the message for given period of frames
     */
    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
        }
    }
}
