import bagel.*;

/**
 * Class for normal notes
 */
public class Note {
    private final Image image;
    private final int appearanceFrame;
    private int speed = 2;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;

    /**
     * A constructor for Note that sets the image according to dir.
     * In addition, sets the appearance frame of the note
     * @param dir this is the direction of the note
     * @param appearanceFrame this is the frame when the note will appear
     */
    public Note(String dir, int appearanceFrame) {
        image = new Image("res/note" + dir + ".png");
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * Method that check if the hold note is active or not
     * @return boolean this is the active state of the note
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method that check if the hold note is completed or not
     * @return boolean this is the completed state of the note
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Method that deactivates the note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Method that gets the note's y coordinate
     * @return int this is the y coordinate
     */
    public int getNoteY() {
        return y;
    }


    /**
     * Method that makes the note falls from the lane according to its appearance frame.
     * Allows to make the note inactive if it is completed
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
     * Method that draws the note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Method to evaluate the score of note using Normal Accuracy.
     * Allows the nAccuracy to draw the message according to the accuracy.
     * @param input this is the input from the user
     * @param nAccuracy this will evaluate the note using the Normal Accuracy
     * @param targetHeight this is the centre of the stationary point (the aim of the game)
     * @param relevantKey this is the relevant key that needs to be pressed
     * @return int this is the score which will be added to the game score
     */
    public int checkScore(Input input, NormalAccuracy nAccuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = nAccuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey));

            if (score != NormalAccuracy.NOT_SCORED) {
                deactivate();
                return score;
            }

        }

        return 0;
    }

    /**
     * Method that makes the note falls faster
     */
    public void speedUp() {
        speed++;
    }

    /**
     * Method that makes the note falls slower
     */
    public void slowDown() {
        speed--;
    }
}
