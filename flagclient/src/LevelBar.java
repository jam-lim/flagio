
/**
 * Write a description of class LevelBar here.
 * class for the lever bar
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
import java.awt.Color;
public class LevelBar extends GUIObject
{

    private int score, maxScore, level;

    public LevelBar(int x, int y, int width, int height, Color color, int score) {
        super(x, y, width, height, color);
        this.score = score;

        level = 1;
        maxScore = (2 * level);
    }

    /**
     * sets the score of the player
     */
    public void setScore(int score) {
        this.score = score - ((level - 1) * level);
    }

    /**
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * @return boolean whether the player is being leveled up or not
     */
    public boolean canLevelUp() {
        return this.score >= maxScore;
    }

    /**
     * levels up the player
     * increases the score required for leveling up
     */
    public void levelUp() {
        if(this.score >= maxScore) {
            this.score = this.score - maxScore;
            level ++;
            maxScore = (2 * level);
        }
    }

    /**
     * sets the max score before leveling up
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * @return the max score before leveling up
     */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * @return the level of the player
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return how much the level bar is filled up according to the score of the player
     */
    public int getProgressBarWidth() {
        double percent = (double)score / (double)maxScore;
        double width = (double)this.getWidth() * percent;

        return (int)width > this.getWidth() ? this.getWidth() : (int)width;
    }

}
