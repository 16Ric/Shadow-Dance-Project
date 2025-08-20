import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

public class Level2 extends Level {
    private int numLanes = 0;
    private final int CLEAR_SCORE = 400;

    /**
     * A constructor that reads the csv file of level2 and stores the lanes in the array.
     * In addition, stores the special lane in the level
     */
    public Level2() {
        lanes = new Lane [3];
        try (BufferedReader br = new BufferedReader(new FileReader("res/level2.csv"))) {
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
