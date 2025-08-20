import bagel.*;
import java.lang.Math;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2023
 * Please enter your name below
 * @author Erich Wiguna
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private static final int RETRY_HEIGHT = 500;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int LEVELS_X_OFFSET = 160;
    private final static int INS_Y_OFFSET = 190;
    private final static int LEVELS_Y_OFFSET = 260;
    private final static int SCORE_LOCATION = 35;
    private static final int DOUBLE_SCORE_DURATION = 480;
    private static final int DOUBLE = 2;
    private static final int SPAWN_ENEMY = 600;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS = "Select Levels With\nNumber Keys";
    private static final String LEVELS = "1      2      3";
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String RETRY_MESSAGE = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private final NormalAccuracy nAccuracy = new NormalAccuracy();
    private final SpecialAccuracy spAccuracy = new SpecialAccuracy();
    private Level1 level1;
    private Level2 level2;
    private Level3 level3;
    private int currLevel;
    private int score = 0;
    private int clearScore = 0;
    private boolean started = false;
    private boolean finished = false;
    private static int currFrame = 0;
    private static int doubleScoreFrame = 0;
    private boolean paused = false;

    /**
     * A constructor of the ShadowDance
     */
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The main method which initialize the ShadowGame object (game).
     * Run the game
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Method that performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * Allows user to choose Level and run the corresponding Level
     * Allows user to retry the game
     * @param input This is the input from the user
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
            INSTRUCTION_FONT.drawString(LEVELS,
                    TITLE_X + LEVELS_X_OFFSET, TITLE_Y + LEVELS_Y_OFFSET);

            if (input.wasPressed(Keys.NUM_1)) {
                level1 = new Level1();
                started = true;
                currLevel = 1;
            } else if (input.wasPressed(Keys.NUM_2)) {
                level2 = new Level2();
                started = true;
                currLevel = 2;
            } else if (input.wasPressed(Keys.NUM_3)) {
                level3 = new Level3();
                started = true;
                currLevel = 3;
            }

        } else if (finished) {
            // end screen
            switch (currLevel) {
                case 1:
                    clearScore = level1.getClearScore();
                    break;
                case 2:
                    clearScore = level2.getClearScore();
                    break;
                case 3:
                    clearScore = level3.getClearScore();
                    break;
            }
            if (score >= clearScore) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        WINDOW_HEIGHT/2);
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        WINDOW_HEIGHT/2);
            }
            INSTRUCTION_FONT.drawString(RETRY_MESSAGE,
                    (Window.getWidth() - INSTRUCTION_FONT.getWidth(RETRY_MESSAGE))/2.0, RETRY_HEIGHT);
            if (input.wasPressed(Keys.SPACE)) {
                retryLevel();
            }

        } else {
            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);
            // gameplay
            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                }
                switch (currLevel) {
                    case 1:
                        for (int i = 0; i < level1.getNumLanes(); i++) {
                            level1.getLane(i).draw();
                        }
                        break;
                    case 2:
                        for (int i = 0; i < level2.getNumLanes(); i++) {
                            level2.getLane(i).draw();
                        }
                        level2.getSpecialLane().draw();
                        break;
                    case 3:
                        level3.getGuardian().draw();
                        for (int i = level3.getCurrEnemy(); i < level3.getNumEnemies(); i++) {
                            level3.getEnemy(i).draw();
                        }
                        for (int i = 0; i < level3.getNumLanes(); i++) {
                            level3.getLane(i).draw();
                        }
                        level3.getSpecialLane().draw();
                        break;
                }

            } else {
                currFrame++;
                switch (currLevel) {
                    case 1:
                        for (int i = 0; i < level1.getNumLanes(); i++) {
                            score += level1.getLane(i).update(input, nAccuracy, spAccuracy);
                        }
                        finished = level1.isFinished();
                        break;
                    case 2:
                        drawLevel2(input, level2.getSpecialLane().getCurrSkill());
                        finished = level2.isFinished();
                        break;
                    case 3:
                        Guardian guardian = level3.getGuardian();
                        guardian.draw();

                        if (currFrame % SPAWN_ENEMY == 0) {
                            Enemy enemy = new Enemy();
                            level3.addEnemy(enemy);
                        }
                        if (input.wasPressed(guardian.relevantKey)) {
                            nearestEnemy(guardian);
                        }

                        for (int i = level3.getCurrEnemy(); i < level3.getNumEnemies(); i++) {
                            level3.getEnemy(i).draw();
                            level3.getEnemy(i).update();
                            if (level3.getEnemy(i).isCompleted()) {
                                level3.iterateCurrEnemy();
                            }
                        }
                        for (int i = level3.getCurrArrow(); i < level3.getNumArrows(); i++) {
                            level3.getArrow(i).draw();
                            level3.getArrow(i).update();
                        }

                        drawLevel3(input, level3.getSpecialLane().getCurrSkill());
                        finished = level3.isFinished();
                        break;
                }

                nAccuracy.update();
                spAccuracy.update();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                }
            }
        }
    }

    /**
     * Method to gain the current frame of the game at the moment
     * @return currFrame this is the current frame of the game
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    /**
     * Method that draw the level2.
     * Apply the ongoing skill for every lane
     * @param input this is the input from the user
     * @param ongoingSkill this is the ongoing skill that are being applied
     */
    public void drawLevel2(Input input, String ongoingSkill) {

        switch (ongoingSkill) {

            case "None":
                for (int i = 0; i < level2.getNumLanes(); i++) {
                    score += level2.getLane(i).update(input, nAccuracy, spAccuracy);
                }
                score += level2.getSpecialLane().update(input, spAccuracy);
                break;

            case "DoubleScore":
                if (doubleScoreFrame < DOUBLE_SCORE_DURATION) {
                    doubleScoreFrame++;
                } else {
                    level2.getSpecialLane().resetCurrSkill();
                }

                for (int i = 0; i < level2.getNumLanes(); i++) {
                    score += DOUBLE * (level2.getLane(i).update(input, nAccuracy, spAccuracy));
                }
                score += DOUBLE * (level2.getSpecialLane().update(input, spAccuracy));
                break;

            case "SpeedUp":
                for (int i = 0; i < level2.getNumLanes(); i++) {
                    level2.getLane(i).speedUpLane();
                    score += level2.getLane(i).update(input, nAccuracy, spAccuracy);
                }
                level2.getSpecialLane().speedUpSpecialLane();
                score += level2.getSpecialLane().update(input, spAccuracy);
                level2.getSpecialLane().resetCurrSkill();
                break;

            case "SlowDown":
                for (int i = 0; i < level2.getNumLanes(); i++) {
                    level2.getLane(i).slowDownLane();
                    score += level2.getLane(i).update(input, nAccuracy, spAccuracy);
                }
                level2.getSpecialLane().slowDownSpecialLane();
                score += level2.getSpecialLane().update(input, spAccuracy);
                level2.getSpecialLane().resetCurrSkill();
                break;
        }
    }

    /**
     * Method that draw the level3.
     * Apply the ongoing skill for every lane.
     * Perform the note stealing by the enemy.
     * @param input this is the input from the user
     * @param ongoingSkill this is the ongoing skill that are being applied
     */
    public void drawLevel3(Input input, String ongoingSkill) {

        switch (ongoingSkill) {

            case "None":
                for (int i = 0; i < level3.getNumLanes(); i++) {
                    Lane lane = level3.getLane(i);
                    score += lane.update(input, nAccuracy, spAccuracy);
                    stealNote(lane);
                }
                score += level3.getSpecialLane().update(input, spAccuracy);
                break;

            case "DoubleScore":
                if (doubleScoreFrame < DOUBLE_SCORE_DURATION) {
                    doubleScoreFrame++;
                } else {
                    level3.getSpecialLane().resetCurrSkill();
                }

                for (int i = 0; i < level3.getNumLanes(); i++) {
                    Lane lane = level3.getLane(i);
                    score += DOUBLE * (lane.update(input, nAccuracy, spAccuracy));
                    stealNote(lane);
                }
                score += DOUBLE * (level3.getSpecialLane().update(input, spAccuracy));
                break;

            case "SpeedUp":
                for (int i = 0; i < level3.getNumLanes(); i++) {
                    Lane lane = level3.getLane(i);
                    lane.speedUpLane();
                    score += lane.update(input, nAccuracy, spAccuracy);
                    stealNote(lane);
                }
                level3.getSpecialLane().speedUpSpecialLane();
                score += level3.getSpecialLane().update(input, spAccuracy);
                level3.getSpecialLane().resetCurrSkill();
                break;

            case "SlowDown":
                for (int i = 0; i < level3.getNumLanes(); i++) {
                    Lane lane = level3.getLane(i);
                    level3.getLane(i).slowDownLane();
                    score += lane.update(input, nAccuracy, spAccuracy);
                    stealNote(lane);
                }
                level3.getSpecialLane().slowDownSpecialLane();
                score += level3.getSpecialLane().update(input, spAccuracy);
                level3.getSpecialLane().resetCurrSkill();
                break;
        }
    }

    /**
     * Method that perform the note stealing by the enemy.
     * @param lane this is the lane from which the enemy would steal the note
     */
    public void stealNote(Lane lane) {
        for (int j = level3.getCurrEnemy(); j < level3.getNumEnemies(); j++) {
            for (int k = lane.getCurrNote();
                 k < lane.getNumNotes() && lane.getNote(k).isActive(); k++) {
                int xDistance = level3.getEnemy(j).getEnemyX() - lane.getLocation();
                int yDistance = level3.getEnemy(j).getEnemyY() - lane.getNote(k).getNoteY();
                double distance = Math.sqrt(xDistance*xDistance + yDistance*yDistance);
                if (distance <= Enemy.ENEMY_HITBOX) {
                    lane.getNote(k).deactivate();
                }
            }
        }
    }

    /**
     * Method to produce arrow which target the nearest enemy from the guardian.
     * @param guardian this is being compared to every active enemy
     *                 in order to get the nearest enemy
     */
    public void nearestEnemy(Guardian guardian) {
        double minDistance = -1;
        double minYDistance = -1;
        Enemy enemy = null;
        for (int i = level3.getCurrEnemy(); i < level3.getNumEnemies()
                && level3.getEnemy(i).isActive(); i++) {
            int xDistance = guardian.getGuardianX() - level3.getEnemy(i).getEnemyX();
            int yDistance = guardian.getGuardianY() - level3.getEnemy(i).getEnemyY();
            double currDistance = Math.sqrt(xDistance*xDistance + yDistance*yDistance);
            if (currDistance < minDistance || minDistance < 0){
                minDistance = currDistance;
                minYDistance = yDistance;
                enemy = level3.getEnemy(i);
            }
        }

        if (minDistance > 0 && enemy != null) {
            Arrow arrow = new Arrow(guardian, enemy, guardian.shootArrow(minDistance, minYDistance));
            level3.addArrow(arrow);
        }
    }

    /**
     * Method that reset the level so that we can retry the level
     */
    public void retryLevel() {
        score = 0;
        started = false;
        finished = false;
        currLevel = 0;
        currFrame = 0;
        nAccuracy.resetMessage();
        spAccuracy.resetMessage();
    }

}
