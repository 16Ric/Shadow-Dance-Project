import bagel.*;

/**
 * Class for hold notes
 */
public class HoldNote {

    private static final int HEIGHT_OFFSET = 82;
    private final Image image;
    private final int appearanceFrame;
    private int speed = 2;
    private int y = 24;
    private boolean active = false;
    private boolean holdStarted = false;
    private boolean completed = false;

    /**
     * A constructor for HoldNote that sets the image according to dir.
     * In addition, sets the appearance frame of the hold note
     * @param dir this is the direction of the hold note
     * @param appearanceFrame this is the frame when the hold note will appear
     */
    public HoldNote(String dir, int appearanceFrame) {
        image = new Image("res/holdNote" + dir + ".png");
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * Method that check if the hold note is active or not
     * @return boolean this is the active state of the hold note
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method that check if the hold note is completed or not
     * @return boolean this is the completed state of the hold note
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Method that deactivates the hold note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Method that sets the holdStarted to true
     */
    public void startHold() {
        holdStarted = true;
    }

    /**
     * Method that makes the hold note falls from the lane according to its appearance frame.
     * Allows to make the hold note inactive if it is completed
     */
    public void update() {
        if (active) {
            y += speed;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * Method that draws the hold note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Method to evaluate the score of hold note using Normal Accuracy.
     * Allows the nAccuracy to draw the message according to the accuracy.
     * @param input this is the input from the user
     * @param nAccuracy this will evaluate the hold note using the Normal Accuracy
     * @param targetHeight this is the centre of the stationary point (the aim of the game)
     * @param relevantKey this is the relevant key that needs to be pressed
     * @return int this is the score which will be added to the game score
     */
    public int checkScore(Input input, NormalAccuracy nAccuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = nAccuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));

            if (score == NormalAccuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != NormalAccuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = nAccuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != NormalAccuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                nAccuracy.setAccuracy(NormalAccuracy.MISS);
                return NormalAccuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return y + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return y - HEIGHT_OFFSET;
    }

    /**
     * Method that makes the bomb note falls from the lane faster
     */
    public void speedUp() {
        speed++;
    }

    /**
     * Method that makes the bomb note falls from the lane slower
     */
    public void slowDown() {
        speed--;
    }
}
