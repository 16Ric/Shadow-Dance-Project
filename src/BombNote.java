import bagel.*;
public class BombNote {
    private static final Image image = new Image("res/noteBomb.png");;
    private final int BOMB_SCORE = 0;
    private final int appearanceFrame;
    private int speed = 2;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;
    public static final String BOMB = "Lane Clear";

    /**
     * A constructor that sets the appearance frame for this bomb note.
     * @param appearanceFrame this is the frame in which the bomb will be drawn
     */
    public BombNote(int appearanceFrame) {
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * Method that check if the bomb note is active or not.
     * @return boolean this is the active state of the bomb
     */
    public boolean isActive() {return active;}

    /**
     * Method that check if the bomb note is completed or not.
     * @return boolean this is the completed state of the bomb
     */
    public boolean isCompleted() {return completed;}

    /**
     * Method that deactivates the bomb note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Method that makes the bomb note falls from the lane according to its appearance frame.
     * Allows to make the bomb note inactive if it is completed
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
     * Method to draw the bomb note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Method to check if the bomb is pressed in range of the Special Accuracy or not
     * @param input this is the input from the user
     * @param spAccuracy this will evaluate the bomb using the Special Accuracy
     * @param targetHeight this is the centre of the stationary point (the aim of the game)
     * @param relevantKey this is the relevant key that needs to be pressed
     * @return int this is a flag that signalised that the bomb is triggered
     */
    public int checkBomb(Input input, SpecialAccuracy spAccuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = spAccuracy.evaluateEffect(BOMB, y, targetHeight, input.wasPressed(relevantKey));

            if (score != SpecialAccuracy.NOT_SCORED) {
                deactivate();
                if (score != SpecialAccuracy.NOT_ACTIVATED) {
                    return Lane.CLEAR;
                }
            }
        }

        return 0;
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
