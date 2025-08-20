import bagel.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Level3 extends Level {
    private int numLanes = 0;
    private final int CLEAR_SCORE = 400;
    private final ArrayList<Enemy> enemies;
    private int numEnemies = 0;
    private int currEnemy = 0;
    private final Guardian guardian;
    private final ArrayList<Arrow> arrows;
    private int numArrows = 0;
    private int currArrow = 0;
    private Clip track;

    /**
     * A constructor that reads the csv file of level2 and stores the lanes in the array.
     * In addition, stores the special lane, guardian, and enemies in the level
     */
    public Level3() {
        lanes = new Lane [2];
        enemies = new ArrayList<>();
        guardian = new Guardian();
        arrows = new ArrayList<>();

        try {
            File file = new File("res/track3.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            track = AudioSystem.getClip();
            track.open(audioStream);
            track.loop(Clip.LOOP_CONTINUOUSLY); // loop indefinitely
            track.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("res/level3.csv"))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    if(laneType.equals("Special")) {
                        int pos = Integer.parseInt(splitText[2]);
                        SpecialLane spl = new SpecialLane(pos);
                        addSpecialLane(spl);
                    } else {
                        int pos = Integer.parseInt(splitText[2]);
                        Lane lane = new Lane(laneType, pos);
                        addLane(lane);
                    }

                } else if (splitText[0].equals("Special") && spLane != null) {
                    if(splitText[1].equals("Bomb")) {
                        BombNote bombNote = new BombNote(Integer.parseInt(splitText[2]));
                        spLane.addBombNote(bombNote);
                    } else {
                        SpecialNote specialNote = new SpecialNote(splitText[1],
                                Integer.parseInt(splitText[2]));
                        spLane.addSpecialNote(specialNote);
                    }
                }else {
                    // reading notes for normal lane
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                Note note = new Note(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                            case "Bomb":
                                BombNote bombNote = new BombNote(Integer.parseInt(splitText[2]));
                                lane.addBombNote(bombNote);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Method that adds lane to the array of lane
     * @param l this is the lane that are added
     */
    public void addLane(Lane l) {
        lanes[numLanes++] = l;
    }

    /**
     * Method that adds special lane to the level
     * @param spl this is the special lane that are added
     */
    public void addSpecialLane(SpecialLane spl) {
        spLane = spl;
    }

    /**
     * Method that adds enemy to the list of enemy in the level
     * @param enemy this is the enemy that are added
     */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        numEnemies++;
    }

    /**
     * Method that gets the total number of enemies in the list
     * @return int this is the number of enemies in the list
     */
    public int getNumEnemies() {
        return numEnemies;
    }

    /**
     * Method that gets the current index of enemy that is not completed
     * @return int this is current index of enemy that still not completed
     */
    public int getCurrEnemy() {
        return currEnemy;
    }

    /**
     * Method that iterates the current index of enemy.
     */
    public void iterateCurrEnemy() {
        currEnemy++;
    }

    /**
     * Method that gets the enemy by the index from the list
     * @param index this is the enemy index in the list
     * @return Arrow this is the enemy that are returned
     */
    public Enemy getEnemy(int index) {
        return enemies.get(index);
    }

    /**
     * Method that gets the guardian in the level
     * @return Guardian this is the guardian that are returned
     */
    public Guardian getGuardian() {
        return guardian;
    }

    /**
     * Method that adds arrow to the list of arrow in the level
     * @param arrow this is the arrow that are added
     */
    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
        numArrows++;
    }

    /**
     * Method that gets the total number of arrows in the list
     * @return int this is the number of arrows in the list
     */
    public int getNumArrows() {
        return numArrows;
    }

    /**
     * Method that gets the current index of arrow that is not completed
     * @return int this is current index of arrow that still not completed
     */
    public int getCurrArrow() {
        return currArrow;
    }

    /**
     * Method that gets the arrow by the index from the list
     * @param index this is the arrow index in the list
     * @return Arrow this is the arrow that are returned
     */
    public Arrow getArrow(int index) {
        return arrows.get(index);
    }

    /**
     * Method that gets the total number of lanes in the array
     * @return int this is the number of lanes in the array
     */
    public int getNumLanes() {
        return numLanes;
    }

    /**
     * Method that gets the lane by the index from the array
     * @param laneIndex this is the lane index in the array
     * @return Lane this is the lane that are returned
     */
    public Lane getLane(int laneIndex) {
        return lanes[laneIndex];
    }

    /**
     * Method that gets the special lane from the level
     * @return SpecialLane this is the special lane that are returned
     */
    public SpecialLane getSpecialLane() {
        return spLane;
    }

    /**
     * Method that gets the clear score of the level
     */
    public int getClearScore(){
        return CLEAR_SCORE;
    }

    /**
     * Method that check if the level is finished or not
     * @return boolean this is the state of level2
     */
    public boolean isFinished() {
        if(!spLane.isFinished()) {
            return false;
        }
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }

}
