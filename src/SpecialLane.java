import bagel.*;
import java.util.ArrayList;

public class SpecialLane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    public static final int CLEAR = 1;
    private static final int SPEED_UP_SCORE = 15;
    private static final int SLOW_DOWN_SCORE = 15;
    private final Image image;
    private final ArrayList<SpecialNote> specialNotes;
    private int numSpecialNotes = 0;
    private final ArrayList<BombNote> bombNotes;
    private int numBombNotes = 0;
    private final Keys relevantKey = Keys.SPACE;
    private final int location;
    private int currSpecialNote = 0;
    private int currBombNote = 0;
    private String currSkill = "None";

    /**
     * A constructor that sets the location and image of the lane.
     * Allows to initialize the list of Special Note and BombNote
     * @param location this is the x location which the lane will be drawn
     */
    public SpecialLane(int location) {
        specialNotes = new ArrayList<>();
        bombNotes = new ArrayList<>();
        this.location = location;
        image = new Image("res/laneSpecial.png");
    }

    /**
     * Method that adds a special note to the list of special notes stored in the special lane
     */
    public void addSpecialNote(SpecialNote spn) {
        specialNotes.add(spn);
        numSpecialNotes++;
    }

    /**
     * Method that adds a bomb note to the list of bomb notes stored in the special lane
     */
    public void addBombNote(BombNote bn) {
        bombNotes.add(bn);
        numBombNotes++;
    }

    /**
     * Method to keep updating the stored notes (special and bomb).
     * Allows the special lane to be cleared of any active special notes.
     * @param input this is the input from the user
     * @param spAccuracy this will evaluate the special notes and bomb notes using the Special Accuracy
     * @return int this is the score which will be added to the game score
     */
    public int update(Input input, SpecialAccuracy spAccuracy) {
        draw();

        for (int i = currSpecialNote; i < numSpecialNotes; i++) {
            specialNotes.get(i).update();
        }

        for (int j = currBombNote; j < numBombNotes; j++) {
            bombNotes.get(j).update();
        }

        if (currBombNote < numBombNotes) {
            int score = bombNotes.get(currBombNote).checkBomb(input, spAccuracy, TARGET_HEIGHT, relevantKey);
            if (bombNotes.get(currBombNote).isCompleted()) {
                currBombNote++;
            }
            clearSpecialLane(score == CLEAR);
        }

        if (currSpecialNote < numSpecialNotes) {
            int score = specialNotes.get(currSpecialNote).checkSkill
                    (input, spAccuracy, TARGET_HEIGHT, relevantKey);
            score = activateSkill(score == SpecialAccuracy.ACTIVATED,
                    specialNotes.get(currSpecialNote).getSkill());

            if (specialNotes.get(currSpecialNote).isCompleted()) {
                currSpecialNote++;
            }
            return score;
        }

        return SpecialAccuracy.NOT_SCORED;
    }

    /**
     * Method that check if the lane is finished by iterating through the lists
     * @return boolean this is the state of the lane
     */
    public boolean isFinished() {
        for (int i = 0; i < numSpecialNotes; i++) {
            if (!specialNotes.get(i).isCompleted()) {
                return false;
            }
        }

        for (int k = 0; k < numBombNotes; k++) {
            if (!bombNotes.get(k).isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Method that draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currSpecialNote; i < numSpecialNotes; i++) {
            specialNotes.get(i).draw(location);
        }

        for (int j = currBombNote; j < numBombNotes; j++) {
            bombNotes.get(j).draw(location);
        }
    }

    /**
     * Method that clears the special lane if bomb is triggered.
     * This only clears the active special notes.
     */
    private void clearSpecialLane(boolean clear) {
        if (clear) {
            for (int i = currSpecialNote; i < numSpecialNotes &&
                    specialNotes.get(i).isActive(); i++) {
                specialNotes.get(i).deactivate();
            }
        }
    }

    /**
     * Method that sets the current skill to skill if activated
     */
    private int activateSkill(boolean activated, String skill) {
        if (activated) {
            switch (skill) {
                case "DoubleScore":
                    currSkill = skill;
                    break;
                case "SpeedUp":
                    currSkill = skill;
                    return SPEED_UP_SCORE;
                case "SlowDown":
                    currSkill = skill;
                    return SLOW_DOWN_SCORE;
            }
        }
        return 0;
    }

    /**
     * Method that gets the current skill.
     * @return String this is the current skill
     */
    public String getCurrSkill() {
        return currSkill;
    }

    /**
     * Method that clears the current skill.
     */
    public void resetCurrSkill() {
        currSkill = "None";
    }

    /**
     * Method that speeds up the whole lane
     */
    public void speedUpSpecialLane() {
        for (int i = currSpecialNote; i < numSpecialNotes; i++) {
            specialNotes.get(i).speedUp();
        }

        for (int j = currBombNote; j < numBombNotes; j++) {
            bombNotes.get(j).speedUp();
        }
    }

    /**
     * Method that slows down the whole lane
     */
    public void slowDownSpecialLane() {
        for (int i = currSpecialNote; i < numSpecialNotes; i++) {
            specialNotes.get(i).slowDown();
        }

        for (int j = currBombNote; j < numBombNotes; j++) {
            bombNotes.get(j).slowDown();
        }
    }
}
