
/**
 * Item.java
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * Assignment #:
 *
 * Brief Program Description:abstract class for item
 *
 *
 */
import javax.imageio.*;
import java.awt.image.*;
public abstract class Item
{
    BufferedImage image;

    private ItemType type;
    private int x, y, width, height;

    private boolean pickedUp, visible;

    private int playerID;

    public Item(ItemType type, int x, int y, int width, int height) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.pickedUp = false;

        playerID = -1;
        visible = true;
    }

    /**
     * @return the x coordinate of the item
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate of the item
     */
    public int getY() {
        return y;
    }

    /**
     * @return the width of the item
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the item
     */
    public int getHeight() {
        return width;
    }

    /**
     * sets the x coordinate of the item
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * sets the y coordinate of the item
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * sets the width of the item
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * sets the height of the item
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return true if colliding
     */
    public boolean intersectsWith(Player player) {
        if (Math.abs(player.getX() - this.x) < this.width && Math.abs(player.getY() - this.y) < this.height) {
            return true;
        }
        return false;
    }

    /**
     * @return true if the player with the parameter ID picked up the item
     */
    public void pickUp(int ID) {
        this.pickedUp = true;
        setPlayerID(ID);
    }

    /**
     * put the item back to the original spot
     */
    public void putDown() {
        this.pickedUp = false;
        setPlayerID(-1);
    }

    /**
     * @return return boolean whether the item is picked up or not
     */
    public boolean isPickedUp() {
        return pickedUp;
    }

    /**
     * sets the ID of the player who picked up the item
     */
    public void setPlayerID(int ID) {
        playerID = ID;
    }

    /**
     * @return return the ID of the player who picked up the item
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * @return the image of the item
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * sets the image of the item
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * decides whether the item is visible or not
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return boolean wheter the item is visible or not
     */
    public boolean isVisible() {
        return visible;
    }

}

/**
 * enum for item type to make it easier
 */
enum ItemType {
    FLAG,
    SPECIAL,
    PLAYER
}
