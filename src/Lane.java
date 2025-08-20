import bagel.*;
import java.util.ArrayList;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    public static final int CLEAR = 1;
    private final String type;
    private final Image image;
    private final ArrayList<Note> notes;
    private int numNotes = 0;
    private final ArrayList<HoldNote> holdNotes;
    private int numHoldNotes = 0;
    private final ArrayList<BombNote> bombNotes;
    private int numBombNotes = 0;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;
    private int currHoldNote = 0;
    private int currBombNote = 0;

    /**
     * A constructor that sets the location, direction, relevant keys, and image of the lane.
     * Allows to initialize the list of Note, HoldNote, and BombNote
     * @param dir this is the direction of the lane
     * @param location this is the x location which the lane will be drawn
     */
    public Lane(String dir, int location) {
        notes = new ArrayList<>();
        holdNotes = new ArrayList<>();
        bombNotes = new ArrayList<>();
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
        }
    }

    /**
     * Method that gets the direction of the lane.
     * @return String this is the type or direction of the lane
     */
    public String getType() {
        return type;
    }

    /**
     * Method to keep updating the stored notes (all types except special).
     * Allows the lane to be cleared of any active notes or hold notes.
     * @param input this is the input from the user
     * @param nAccuracy this will evaluate the note and hold note using the Normal Accuracy
     * @param spAccuracy this will evaluate the bomb notes using the Special Accuracy
     * @return int this is the score which will be added to the game score
     */
    public int update(Input input, NormalAccuracy nAccuracy, SpecialAccuracy spAccuracy) {
        draw();

        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).update();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).update();
        }

        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).update();
        }

        if (currBombNote < numBombNotes) {
            int score = bombNotes.get(currBombNote).checkBomb(input, spAccuracy, TARGET_HEIGHT, relevantKey);
            if (bombNotes.get(currBombNote).isCompleted()) {
                currBombNote++;
            }
            clearLane(score == CLEAR);
        }

        if (currNote < numNotes) {
            int score = notes.get(currNote).checkScore(input, nAccuracy, TARGET_HEIGHT, relevantKey);
            if (notes.get(currNote).isCompleted()) {
                currNote++;
                return score;
            }
        }

        if (currHoldNote < numHoldNotes) {
            int score = holdNotes.get(currHoldNote).checkScore(input, nAccuracy, TARGET_HEIGHT, relevantKey);
            if (holdNotes.get(currHoldNote).isCompleted()) {
                currHoldNote++;
            }
            return score;
        }

        return NormalAccuracy.NOT_SCORED;
    }

    /**
     * Method that adds a note to the list of notes stored in the lane
     */
    public void addNote(Note n) {
        notes.add(n);
        numNotes++;
    }

    /**
     * Method that adds a hold note to the list of hold notes stored in the lane
     */
    public void addHoldNote(HoldNote hn) {
        holdNotes.add(hn);
        numHoldNotes++;
    }

    /**
     * Method that adds a bomb note to the list of bomb notes stored in the lane
     */
    public void addBombNote(BombNote bn) {
        bombNotes.add(bn);
        numBombNotes++;
    }

    /**
     * Method that gets a note from the list of notes by index
     * @param index this is the note index
     * @return Note this is the note of that index
     */
    public Note getNote(int index) {
        return notes.get(index);
    }

    /**
     * Method that gets the total number of notes in the list
     * @return int this is the total notes in the list
     */
    public int getNumNotes() {
        return numNotes;
    }

    /**
     * Method that gets the current index of note that is not completed
     * @return int this is current index of note that still not completed
     */
    public int getCurrNote() {
        return currNote;
    }

    /**
     * Method that gets the x location of the lane
     * @return int this is the x location of the lane
     */
    public int getLocation() {
        return location;
    }

    /**
     * Method that check if the lane is finished by iterating through the lists
     * @return boolean this is the state of the lane
     */
    public boolean isFinished() {
        for (int i = 0; i < numNotes; i++) {
            if (!notes.get(i).isCompleted()) {
                return false;
            }
        }

        for (int j = 0; j < numHoldNotes; j++) {
            if (!holdNotes.get(j).isCompleted()) {
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

        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).draw(location);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).draw(location);
        }

        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).draw(location);
        }
    }

    /**
     * Method that clears the lane if bomb is triggered.
     * This only clears the active notes and hold notes.
     */
    private void clearLane(boolean clear) {
        if (clear) {
            for (int i = currNote; i < numNotes && notes.get(i).isActive(); i++) {
                notes.get(i).deactivate();
            }

            for (int j = currHoldNote; j < numHoldNotes && holdNotes.get(j).isActive(); j++) {
                holdNotes.get(j).deactivate();
            }
        }
    }

    /**
     * Method that speeds up the whole lane
     */
    public void speedUpLane() {
        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).speedUp();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).speedUp();
        }

        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).speedUp();
        }
    }

    /**
     * Method that slows down the whole lane
     */
    public void slowDownLane() {
        for (int i = currNote; i < numNotes; i++) {
            notes.get(i).slowDown();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes.get(j).slowDown();
        }

        for (int k = currBombNote; k < numBombNotes; k++) {
            bombNotes.get(k).slowDown();
        }
    }
}
