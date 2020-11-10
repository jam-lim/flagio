
/**
 * OtherPlayer.java
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * Assignment #:
 *
 * Brief Program Description:class for other player in the server
 *
 *
 */
import java.awt.*;
public class OtherPlayer extends Item implements Square
{

    private String name;

    private GameColor color, originalColor;
    private int ID;

    private boolean tagged;

    private boolean hasShield, hasStar, canTag;

    /**
     * @param name of player, x coord of player, y coord of player, size of player, color of player, original team color of player, whether player is tagged or not, whether he/she has a shield, whether or not they have a star, and an ID of the player
     */
    public OtherPlayer(String name, int x, int y, int size, GameColor color, GameColor orignalColor, int tagged, int shield, int star, int ID) {
        super(ItemType.PLAYER, x, y, size, size);
        this.name = name;
        this.color = color;
        this.originalColor = orignalColor;
        this.ID = ID;
        this.tagged = tagged == 1 ? true : false;
        this.hasShield = shield == 1 ? true : false;
        this.hasStar = star == 1 ? true : false;

        canTag = true;
    }
    /**
     * @returns the name of the player
     */
    public String getName() {
        return name;
    }
    @Override
    /**
     * @param size as an integer thats sets the size of the player to a square
     */
    public void setSize(int size) {
        super.setWidth(size);
        super.setHeight(size);
    }
    /**
     * @return sqaure, all sides are equal so it returns any side length as the size
     */
    public int getSize() {
        return super.getWidth();
    }
    /**
     * @return id of the player
     */
    public int getID() {
        return ID;
    }
    /**
     * @return coriginal team color of the player
     */
    public GameColor getOriginalColor() {
        return originalColor;
    }
    /**
     * @return color of the player
     */
    public GameColor getColor() {
        return color;
    }
    /**
     * @param boolean if the player has a shield item
     */
    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }
    /**
     * @param boolean if the player has a star item
     */
    public void setHasStar(boolean hasStar) {
        this.hasStar = hasStar;
    }
    /**
     * @return boolean if the player has a shield item
     */
    public boolean hasShield() {
        return hasShield;
    }
    /**
     * @return boolean if the player has a star item
     */
    public boolean hasStar() {
        return hasStar;
    }
    /**
     * @param sets the id of the player
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    /**
     * @param sets the color of the player
     */
    public void setColor(GameColor color) {
        this.color = color;
    }
    /**
     * @param sets if the player is tagged or not
     */
    public void setTagged(boolean tagged) {
        boolean temp = this.tagged;
        this.tagged = tagged;
    }
    /**
     * @return whether or not this player is tagged
     */
    public boolean isTagged() {
        return tagged;
    }

    /**
     * sets the boolean canTag which decides whether the player can tag someone else or not
     */
    public void setCanTag(boolean canTag) {
        this.canTag = canTag;
    }

    /**
     * @return boolean deciding whether the player can tag someone else or not
     */
    public boolean canTag() {
        return canTag;
    }
}
