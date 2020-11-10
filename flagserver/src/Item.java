
/**
 * Write a description of class Item here.
 * class for the item
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
public class Item
{

    private boolean isVisible, taken;

    public Item() {
        isVisible = false;
        taken = false;
    }

    /**
     * @return if item is visible or not
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @param sets the item to be visible or not to be visible
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * @param change whether the item was taken by a player
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    /**
     * @return check if the item has been taken
     */
    public boolean isTaken() {
        return taken;
    }
}
