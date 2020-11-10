
/**
 * Player.java
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * Assignment #:
 *
 * Brief Program Description:class for player
 *
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Player
{
    private String name;
    private int x, y, size, color;
    private double velocity, nonstopVelocity; //nonstopVelocity ignores when you are tagged and keeps track of your moving velocity

    private double upDown, rightLeft= 0; //when it can update movement
    private double aud, arl, stopUd,stopRl=0; //acceleration
    private double intensity=0;

    private int ID, score;

    private GameColor originalColor;

    private boolean tagged;
    private boolean canTag;

    private java.util.Timer tagTimer;
    private int timeLeft, currentTimeLeft;

    private boolean hasShield, hasStar;

    PrintWriter out;

    /**
     * @param name of player, size of player(square so widht or height), color of player, x coordinate of player, y coordinate, velocity of player, id of player
     */
    public Player(String name, int size, int color, int x, int y, double velocity, int ID) {
        this.name = name;
        this.size = size;
        this.color = color;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.ID = ID;

        nonstopVelocity = velocity;
        tagged = false;
        score = 0;
        tagTimer = new java.util.Timer();
        timeLeft = 30;
        currentTimeLeft = timeLeft;
        canTag = true;
        hasShield = false;
        hasStar = false;
    }

    /**
     * @param takes in a printwriter so that this class can send data to the server easily
     */
    public void setOut(PrintWriter out) {
        this.out = out;
    }
    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    }
    /**
     * @return the size of the player
     */
    public int getSize() {
        return size;
    }
    /**
     * @return the color of the player
     */
    public GameColor getColor() {
        return color == 1 ? GameColor.BLUE : color == 2 ? GameColor.RED : GameColor.SPECIAL;
    }/**
 * @return the x coordinate of the player
 */

public int getX() {
    return x;
}
    /**
     * @return the y coordinate of the player
     */
    public int getY() {
        return y;
    }
    /**
     * @return the velocity of the player
     */
    public double getVelocity() {
        return velocity;
    }
    /**
     * @return the id of the player
     */
    public int getID() {
        return ID;
    }
    /**
     * @return the personal score of the player
     */
    public int getScore() {
        return score;
    }
    /**
     * @return if the player can tag or not
     */
    public boolean getCanTag(){
        return canTag;
    }

    /**
     * @param boolean deciding whether this player can tag or not
     */
    public void setCanTag(boolean canTag){
        this.canTag = canTag;
    }
    /**
     * @param is the personal score of the player
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * @param takes in a double for velocity to see how fast the player will move
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
        this.nonstopVelocity = velocity != 0 ? velocity : nonstopVelocity;
    }
    /**
     * @param sets the x coordinates of the player
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * @param sets the y coordinates of the player
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * @param sets the size of the player (width or height)
     */
    public void setSize(int size) {
        this.size = size;
    }
    /**
     * @param sets the name of the player
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param sets the color of the player
     */
    public void setColor(GameColor color) {
        this.color = color.getValue();
    }
    /**
     * @param sets the id of the player
     */
    public void setID(int ID) {
        this.ID = ID;
    }
    /**
     * @param sets the original team color of the player
     */
    public void setOriginalColor(GameColor originalColor) {
        this.originalColor = originalColor;
    }
    /**
     * @param sets the time left the player has of being tagged
     */
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
    /**
     * @param boolean representing whether or not the player has a shield item
     */
    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }
    /**
     * @param boolean representing whether or not the player has a star item
     */
    public void setHasStar(boolean hasStar) {
        this.hasStar = hasStar;
    }
    /**
     * @return if this player has a shield or not
     */
    public boolean hasShield() {
        return hasShield;
    }
    /**
     * @return if this player has a star or not
     */
    public boolean hasStar() {
        return hasStar;
    }
    /**
     * @return max countdown set before the player is tagged
     */
    public int getTimeLeft() {
        return timeLeft;
    }
    /**
     * @return current time of the timer
     */
    public int getCurrentTimeLeft() {
        return currentTimeLeft;
    }
    /**
     * @param boolean deciding whether the player is tagged or not;
     */
    public void setTagged(boolean tagged) {
        boolean temp = this.tagged;
        this.tagged = tagged;
        if(tagged)setVelocity(0);
        else setVelocity(nonstopVelocity);
        if(tagged && !temp) {
            currentTimeLeft = timeLeft;
            tagTimer = new java.util.Timer();
            tagTimer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if(currentTimeLeft <= 0) {
                        currentTimeLeft = timeLeft;
                        setTagged(false);
                        if(out != null)
                            out.println("TAG UNTAGGED by:0|to:"+getID()+"|");
                        return;
                    }
                    currentTimeLeft -= 1;
                }
            }, 0, 1000);
        } else if(!tagged && temp) {
            currentTimeLeft = timeLeft;
            tagTimer.cancel();
            tagTimer.purge();

            this.setHasShield(true);
            out.println("ITEM START SHIELD ID:"+this.getID()+"|");
            java.util.Timer t = new java.util.Timer();
            t.scheduleAtFixedRate(new TimerTask() {

                public void run() {

                    out.println("ITEM END SHIELD ID:"+getID()+"|");
                    setHasShield(false);
                    t.cancel();
                    t.purge();

                }
            }, 2500, 2500);

        }
    }
    /**
     * @return the original team color of this player
     */
    public GameColor getOriginalColor() {
        return originalColor;
    }
    /**
     * @return if this player is tagged or not
     */
    public boolean isTagged() {
        return tagged;
    }
    /**
     * @param angle at which the player will move, intesity at which it moves at and a boolean saying whether to stop or not
     * moves the player based on the angle
     * speed is affected by the distance between the cursor and the center of the player
     */
    public void move(double angle, double intensity, boolean stop)
    {
        if(!stop)
        {
            aud=Math.sin(angle);
            arl=Math.cos(angle);
            stopUd=aud;
            stopRl=arl;

        }
        this.intensity=intensity;
        upDown+=aud;
        if(upDown<-1.5/intensity)
        {
            this.y += velocity;
            upDown=0;
        }
        if(upDown>1.5/intensity)
        {
            this.y -= velocity;
            upDown=0;
        }
        rightLeft+=arl;
        if(rightLeft<-1.5/intensity)
        {
            this.x -= velocity;
            rightLeft=0;
        }
        if(rightLeft>1.5/intensity)
        {
            this.x += velocity;
            rightLeft=0;
        }
        if(stop)
        {
            arl+=arl<0 ?0.001 :-0.001;
            aud+=aud<0 ?0.001 :-0.001;
            if(Math.abs(stopUd-aud)>Math.abs(stopUd))
                aud=0;
            if(Math.abs(stopRl-arl)>Math.abs(stopRl))
                arl=0;
        }

    }

}
