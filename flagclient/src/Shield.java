
/**
 * Write a description of class Shield here.
 * class for the item shield
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
public class Shield extends Item implements Square
{

    public Shield(int x, int y, int width, int height) {
        super(ItemType.SPECIAL, x, y, width, height);
    }

    /**
     * @return the size of the shield
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