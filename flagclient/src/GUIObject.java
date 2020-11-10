
/**
 * Write a description of class GUIObject here.
 * abstract class for the object in game
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
import java.awt.Color;
import java.util.ArrayList;
public abstract class GUIObject
{

    private int x, y, width, height;
    private ArrayList<Color> colors = new ArrayList<Color>();

    private boolean enabled;
    private int alpha;

    public GUIObject(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colors.add(color);

        enabled = true;
        alpha = 125;
    }

    /**
     * @param Color object to be added to the array list of colors for this item
     */
    public void addColor(Color color) {
        this.colors.add(color);
    }

    /**
     * @return color specified from params, returns black if outofbounds
     */
    public Color getColor(int color) {
        if(color < colors.size() && color > -1)
            return colors.get(color);
        else
            return Color.BLACK;
    }

    /**
     * sets the x coordinate of the object
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * sets the y coordinate of the object
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * sets the width of the object
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * sets the height of the object
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the x coordinate of the object
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate of the object
     */
    public int getY() {
        return y;
    }

    /**
     * @return the width of the object
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the object
     */
    public int getHeight() {
        return height;
    }

    /**
     * sets the opacity of the object
     */
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    /**
     * @return the alpha value of the object
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * sets the boolean that decides whether the object is enable or not
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return boolean whether the object is enable or not
     */
    public boolean isEnabled() {
        return enabled;
    }

}
