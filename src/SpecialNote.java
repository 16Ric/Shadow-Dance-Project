import bagel.*;
public class SpecialNote {
    private final String skill;
    private final int appearanceFrame;
    private int speed = 2;
    private Image image;
    public String message;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;

    /**
     * A constructor for Note that sets the image according to skill.
     * In addition, sets the appearance frame of the special note
     * @param skill this is the skill of the special note
     * @param appearanceFrame this is the frame when the special note will appear
     */
    public SpecialNote (String skill, int appearanceFrame) {
        switch (skill) {
            case "DoubleScore":
                image = new Image("res/note2x.png");
                message = "Double Score";
                break;
            case "SpeedUp":
                image = new Image("res/noteSpeedUp.png");
                message = "Speed Up";
                break;
            case "SlowDown":
                image = new Image("res/noteSlowDown.png");
                message = "SlowDown";
                break;
        }
        this.skill = skill;
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
     * Method that check the skill of the special note.
     * @return String this is the skill of the special note
     */
    public String getSkill() {return skill;}

    /**
     * Method that deactivates the note
     */
    public void deactivate() {
        active = false;
        completed = true;
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
     * Method to evaluate the activation of special note using Special Accuracy.
     * Allows the spAccuracy to draw the message according to the message/skill.
     * @param input this is the input from the user
     * @param targetHeight this is the centre of the stationary point (the aim of the game)
     * @param relevantKey this is the relevant key that needs to be pressed
     * @return int this is the flag of activation
     */
    public int checkSkill(Input input, SpecialAccuracy spAccuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = spAccuracy.evaluateEffect(message, y, targetHeight, input.wasPressed(relevantKey));

            if (score != SpecialAccuracy.NOT_SCORED) {
                deactivate();
                if (score != SpecialAccuracy.NOT_ACTIVATED) {
                    return score;
                }
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


