
/**
 * Write a description of class Star here.
 * class for the item star
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
public class Star extends Item implements Square
{

    public Star(int x, int y, int width, int height) {
        super(ItemType.SPECIAL, x, y, width, height);
    }

    /**
     * @return the size of the star
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

}