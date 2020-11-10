
/**
 * Flag.java
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * Assignment #:
 *
 * Brief Program Description: class for the flag
 *
 *
 */
public class Flag extends Item implements Square {

    private int color;

    public Flag(int x, int y, int width, int height, int color) {
        super(ItemType.FLAG, x, y, width, height);

        this.color = color;
    }

    /**
     * @return the color of the flag
     */
    public int getColor() {
        return color;
    }

    /**
     * @return the size of the flag;
     */
    public int getSize() {
        return super.getWidth();
    }

    /**
     * takes the input size and sets the width and height to the input size
     */
    public void setSize(int size) {
        super.setWidth(size);
        super.setHeight(size);
    }

}
