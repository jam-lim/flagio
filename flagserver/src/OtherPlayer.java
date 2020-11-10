
/**
 * OtherPlayer.java
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * Assignment #:
 *
 * Brief Program Description:
 *
 *
 */
import java.awt.*;
public class OtherPlayer
{

    private String name;

    private int x, y, size, color, originalColor, ID;

    public boolean tagged, hasShield, hasStar;

    /**
     * @param Name of the player, x coordinate, y coordinate, the size of the player, the color of the player, the original color team color that doesnt change, ID to differentiate between two players
     */
    public OtherPlayer(String name, int x, int y, int size, int color, int originalColor, int ID) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.originalColor = originalColor;
        this.ID = ID;

        tagged = false;
        hasShield = false;
        hasStar = false;
    }

    /**
     * @return name of player
     */
    public String getName() {
        return name;
    }
    /**
     * @return x coordiante of player
     */
    public int getX() {
        return x;
    }
    /**
     * @return y coordiante of player
     */
    public int getY() {
        return y;
    }
    /**
     * @return size of player (w or height because it's a square)
     */
    public int getSize() {
        return size;
    }
    /**
     * @return integer identification of player
     */
    public int getID() {
        return ID;
    }

    /**
     * using ints for the colors because it gets sent through the server, 1 == blue, 2 == red, 3 == special
     * @return current color of player
     */
    public int getColor() {
        return color;
    }
    /**
     * Either blue or red
     * @return original, team color of player
     */
    public int getOriginalColor() {
        return originalColor;
    }
    /**
     * @param sets the x coordiante of player
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * @param sets the y coordiante of player
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * @param sets the integer identification of player
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    /**
     * @param sets the size of the player
     */
    public void setSize(int size) {
        this.size = size;
    }
    /**
     * @param sets the current color of player
     */
    public void setColor(int color) {
        this.color = color;
    }
    /**
     * @param sets if the player is tagged or not
     */
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }
    /**
     * @param sets if the player has a shield or not
     */
    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }
    /**
     * @param sets if the player has a star or not
     */
    public void setHasStar(boolean hasStar) {
        this.hasStar = hasStar;
    }
    /**
     * @return if the player has a shield or not
     */
    public boolean hasShield() {
        return hasShield;
    }
    /**
     * @return if the player has a star or not
     */
    public boolean hasStar() {
        return hasStar;
    }
    /**
     * @return if the player is tagged or not
     */
    public boolean isTagged() {
        return tagged;
    }

    /**
     * 1== tagged, -1 == not tagged, this method is here because it made it easier to send this data to all the clients
     * @return if the player is tagged or not in integer form
     */
    public int isTaggedInt() {
        return tagged ? 1 : -1;
    }
}
