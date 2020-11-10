
/**
 * Write a description of class UpgradeWindow here.
 * class for the upgrade box
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
import java.awt.Color;
public class UpgradeBox extends GUIObject
{

    private UpgradeType type;
    private int level, maxLevel;

    public UpgradeBox(int x, int y, int width, int height, Color color, UpgradeType type) {
        super(x, y, width, height, color);
        this.type = type;

        level = 0;
        maxLevel = 5;
        super.setEnabled(false);
        super.setAlpha(0);
    }

    @Override
    /**
     * sets whether the upgrade is available or not and determines the visiblity accoring to the availability
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(level == maxLevel ? false : enabled);
        if(enabled) {
            if(maxLevel != level) {
                super.setAlpha(125);
            } else {
                super.setAlpha(50);
            }
        } else
            super.setAlpha(0);
    }

    /**
     * @return the type of the upgrade box
     */
    public UpgradeType getType() {
        return type;
    }

    /**
     * @return the max level of the upgrade
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * sets the level of the upgrade box
     */
    public void setLevel(int level) {
        this.level = level >= maxLevel ? maxLevel : level;
    }

    /**
     * @return the level of the upgrade box
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return the boolean wheter the point (x,y) intersects with the upgrade box or not
     */
    public boolean intersectsWith(int x, int y) {
        if(x >= this.getX() && x <= this.getX() + this.getWidth() && y >= this.getY() && y <= this.getY() + this.getHeight())
            return true;
        return false;
    }

    /**
     * @return the string that writes the type of the upgrade box
     */
    public String toString() {
        return type == UpgradeType.SPEED ? "1:SPEED" : type == UpgradeType.SMALL ? "2:SMALL" : type == UpgradeType.BIG ? "3:BIG" : type == UpgradeType.TAGTIMER ? "4:TIME" : "";
    }

}

/**
 * enum of the upgrade type
 */
enum UpgradeType {
    SPEED,
    SMALL,
    BIG,
    TAGTIMER
}
