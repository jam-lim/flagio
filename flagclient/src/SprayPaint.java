
/**
 * Write a description of class SprayPaint here.
 * class for the item spraypaint
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
public class SprayPaint extends Item implements Square
{

    private boolean running;
    private GameColor teamColor;

    public SprayPaint(int x, int y, int width, int height) {
        super(ItemType.SPECIAL, x, y, width, height);

        running = false;
    }

    /**
     * @return the size of the spraypaint
     */
    public int getSize() {
        return super.getWidth();
    }

    /**
     * sets the width and height to the input size
     */
    public void setSize(int size) {
        super.setWidth(size);
        super.setHeight(size);
    }

    /**
     * sets the variable accordingly if the player gets the spraypaint
     */
    public void setRunning(boolean running, GameColor teamColor) {
        this.running = running;
        this.teamColor = teamColor;
    }

    /**
     * @return boolean whether the spary paint is being used or not
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * return the team color of the player who got the item
     */
    public GameColor getTeamColor() {
        return teamColor;
    }

}
