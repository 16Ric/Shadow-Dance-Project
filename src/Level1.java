import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

public class Level1 extends Level {
    private int numLanes = 0;
    private final int CLEAR_SCORE = 150;

    /**
     * A constructor that reads the csv file of level1 and stores the lanes in the array
     */
    public Level1() {
        CSV_FILE = "res/level1.csv";
        lanes = new Lane[4];
        try (BufferedReader br = new BufferedReader(new FileReader("res/level1.csv"))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    addLane(lane);

                } else {
                    // reading notes
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
     * Method that gets the clear score of the level
     */
    public int getClearScore(){
        return CLEAR_SCORE;
    }

    /**
     * Method that check if the level is finished or not
     * @return boolean this is the state of level1
     */
    public boolean isFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }

}
