
/**
 * GamePanel.java
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * Assignment #:
 *
 * Brief Program Description: Class for drawing, updating and performing actions for the game.
 *
 *
 */

import javax.swing.JPanel;
import javax.imageio.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
    private PrintWriter out;
    private Player player;
    private BufferedImage background;
    private Flag redFlag, blueFlag;
    private SprayPaint sprayPaint;
    private Shield shield;
    private Star star;
    private ArrayList<OtherPlayer> players = new ArrayList<OtherPlayer>();
    private int scoreRed=0;
    private int scoreBlue=0;
    private boolean start = true, canSendCoords = true;
    private int offsetX, offsetY, mouseX, mouseY;
    private LevelBar levelBar;
    private UpgradeWindow upgradeWindow;
    private int coordTimer;
    private boolean spawn;
    private SineAnimation alphaAnimation, alphaAnimation1;
    private boolean debug=false;
    /**
     * @param The starting dimensions of the jpanel, the player object of the person who is playing
     */
    public GamePanel(Dimension d, Player player) {
        super();

        this.player = player;

        this.setOpaque(true);
        this.setPreferredSize(d);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);

        redFlag = new Flag(775, 1975, 50, 50, 2);
        blueFlag = new Flag(7175, 1975, 50, 50, 1);
        sprayPaint = new SprayPaint(3975, 100, 50, 50);
        shield = new Shield(offsetX + (this.getWidth() /2) - player.getSize() / 2, offsetY + (this.getHeight() / 2) - player.getSize() / 2, 50, 50);
        star = new Star(offsetX + (this.getWidth() /2) - player.getSize() / 2, offsetY + 3900 + (this.getHeight() / 2) - player.getSize() / 2, 50, 50);

        try {
            background = ImageIO.read(getClass().getResource("background.jpg"));
            redFlag.setImage(ImageIO.read(getClass().getResource("flag.jpg")));
            blueFlag.setImage(ImageIO.read(getClass().getResource("blue.jpg")));
            sprayPaint.setImage(ImageIO.read(getClass().getResource("sprayPaint.png")));
            shield.setImage(ImageIO.read(getClass().getResource("shield.png")));
            star.setImage(ImageIO.read(getClass().getResource("star.png")));
        } catch(IOException e) {}

        levelBar = new LevelBar((this.getWidth() / 2) - (this.getWidth() / 2), 50, (this.getWidth() / 2), 20, Color.GRAY, 0);
        levelBar.addColor(Color.GREEN);

        upgradeWindow = new UpgradeWindow(10, 10, 105, 105, Color.GRAY);
        upgradeWindow.addBox(new UpgradeBox(20, 20, 40,  40, Color.BLACK, UpgradeType.SPEED));
        upgradeWindow.addBox(new UpgradeBox(65, 20, 40, 40, Color.BLACK, UpgradeType.SMALL));
        upgradeWindow.addBox(new UpgradeBox(20, 65, 40, 40, Color.BLACK, UpgradeType.BIG));
        upgradeWindow.addBox(new UpgradeBox(65, 65, 40, 40, Color.BLACK, UpgradeType.TAGTIMER));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(coordTimer>-1)
                    updateGame();
            }
        }, 0, 1);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                if(out != null && player != null){
                    out.println("EXITED ID:" + player.getID());

                }
                if(redFlag.getPlayerID() == player.getID()) {
                    redFlag.putDown();
                    out.println("FLAG red:-1|blue:"+blueFlag.getPlayerID());
                }

                if(blueFlag.getPlayerID() == player.getID()) {
                    blueFlag.putDown();
                    out.println("FLAG red:"+redFlag.getPlayerID()+"|blue:-1");
                }
                freeMemory();
                coordTimer=-10;
                System.out.println("memory cleaned");
            }
        }));

        coordTimer = 0;
    }

    /**
     * @param Graphics object that draws to the screen
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, offsetX + (this.getWidth() / 2) - player.getSize() / 2, offsetY + (this.getHeight() / 2) - player.getSize() / 2, 8000, 4000, this);

        g.setColor(new Color (Color.YELLOW.getRed(),Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), 100));
        g.fillRect(offsetX + 400 + (this.getWidth() /2) - player.getSize() / 2 ,offsetY + 1600 + (this.getHeight() / 2) - player.getSize() / 2, 800, 800);
        g.fillRect(offsetX + 6800 + (this.getWidth() /2) - player.getSize() / 2 ,offsetY + 1600 + (this.getHeight() / 2) - player.getSize() / 2, 800, 800);
        g.setColor(new Color (Color.BLUE.getRed(),Color.BLUE.getGreen(), Color.BLUE.getBlue(), 75));
        g.fillRect(offsetX + 7500 + (this.getWidth() /2) - player.getSize() / 2 ,offsetY + 3500 + (this.getHeight() / 2) - player.getSize() / 2, 500, 500);
        g.setColor(new Color (Color.RED.getRed(),Color.RED.getGreen(), Color.RED.getBlue(), 75));
        g.fillRect(offsetX + (this.getWidth() /2) - player.getSize() / 2 ,offsetY  + (this.getHeight() / 2) - player.getSize() / 2, 500, 500);
        g.setColor(new Color (Color.GREEN.getRed(),Color.GREEN.getGreen(), Color.GREEN.getBlue(), 100));
        g.fillRect(offsetX + 3200 + (this.getWidth() /2) - player.getSize() / 2,offsetY + (this.getHeight() / 2) - player.getSize() / 2, 1600, 4000);
        //adjusting the size of the upgrade box depend on the screen size
        if(coordTimer%10==0)
        {
            int a=(int)(40.0*((this.getWidth() / 2)/250.0));
            this.upgradeWindow.setWidth(2*a+25);
            this.upgradeWindow.setHeight(2*a+25);
            this.getUpgradeBoxByType(UpgradeType.SPEED).setWidth(a);
            this.getUpgradeBoxByType(UpgradeType.SPEED).setHeight(a);
            this.getUpgradeBoxByType(UpgradeType.SMALL).setX(a+25);
            this.getUpgradeBoxByType(UpgradeType.SMALL).setWidth(a);
            this.getUpgradeBoxByType(UpgradeType.SMALL).setHeight(a);
            this.getUpgradeBoxByType(UpgradeType.BIG).setY(a+25);
            this.getUpgradeBoxByType(UpgradeType.BIG).setWidth(a);
            this.getUpgradeBoxByType(UpgradeType.BIG).setHeight(a);
            this.getUpgradeBoxByType(UpgradeType.TAGTIMER).setY(a+25);
            this.getUpgradeBoxByType(UpgradeType.TAGTIMER).setWidth(a);
            this.getUpgradeBoxByType(UpgradeType.TAGTIMER).setHeight(a);
            this.getUpgradeBoxByType(UpgradeType.TAGTIMER).setX(a+25);
        }
        if(sprayPaint.isVisible()) {
            sprayPaint.setX(4000 - (sprayPaint.getSize() / 2));
            sprayPaint.setY(100);
            g.setColor(new Color (Color.YELLOW.getRed(),Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), 100));
            g.fillOval(offsetX + sprayPaint.getX() + (this.getWidth() /2) - player.getSize() / 2 - 10, offsetY + sprayPaint.getY() + (this.getHeight() / 2) - player.getSize() / 2 - 10, sprayPaint.getSize() + 20, sprayPaint.getSize() + 20);
            g.drawImage(sprayPaint.getImage(), offsetX + sprayPaint.getX() + (this.getWidth() /2) - player.getSize() / 2, offsetY + sprayPaint.getY() + (this.getHeight() / 2) - player.getSize() / 2, sprayPaint.getSize(), sprayPaint.getSize(), this);
        }

        if(shield.isVisible()) {
            shield.setX(4000 - (shield.getSize() / 2));
            shield.setY(3850);
            g.setColor(new Color (Color.YELLOW.getRed(),Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), 100));
            g.fillOval(offsetX + shield.getX() + (this.getWidth() /2) - player.getSize() / 2 - 10, offsetY + shield.getY() + (this.getHeight() / 2) - player.getSize() / 2 - 10, shield.getSize() + 20, shield.getSize() + 20);
            g.drawImage(shield.getImage(), offsetX + shield.getX() + (this.getWidth() /2) - player.getSize() / 2, offsetY + shield.getY() + (this.getHeight() / 2) - player.getSize() / 2, shield.getSize(), shield.getSize(), this);
        }

        if(star.isVisible()) {
            star.setX(4000 - (star.getSize() / 2));
            star.setY(2000 - (star.getSize() / 2));
            g.setColor(new Color (Color.YELLOW.getRed(),Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), 100));
            g.fillOval(offsetX + star.getX() + (this.getWidth() /2) - player.getSize() / 2 - 10, offsetY + star.getY() + (this.getHeight() / 2) - player.getSize() / 2 - 10, star.getSize() + 20, star.getSize() + 20);
            g.drawImage(star.getImage(), offsetX + star.getX() + (this.getWidth() /2) - player.getSize() / 2, offsetY + star.getY() + (this.getHeight() / 2) - player.getSize() / 2, star.getSize(), star.getSize(), this);
        }

        if(!redFlag.isPickedUp())
            g.drawImage(redFlag.getImage(), offsetX + (this.getWidth() / 2) - player.getSize() / 2 + 775, offsetY + (this.getHeight() / 2) - player.getSize() / 2 +1975, redFlag.getSize(), redFlag.getSize(), this);

        if(!blueFlag.isPickedUp())
            g.drawImage(blueFlag.getImage(), offsetX + (this.getWidth() / 2) - player.getSize() / 2 + 7175, offsetY + (this.getHeight() / 2) - player.getSize() / 2 +1975, blueFlag.getSize(), blueFlag.getSize(), this);

        if(player.isTagged()) {
            g.setColor(Color.ORANGE);
            g.fillRect((this.getWidth() / 2) - (player.getSize() / 2) - 8, (this.getHeight() / 2) - (player.getSize() / 2) - 8, player.getSize() + 16, player.getSize() + 16);
        }

        if(player.getColor() == GameColor.BLUE)
            g.setColor(Color.BLUE);
        else if(player.getColor() == GameColor.RED)
            g.setColor(Color.RED);
        else if(player.getColor() == GameColor.SPECIAL)
        {
            Color color=new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
            g.setColor(color);
        }

        if(player.hasShield()) {
            if(alphaAnimation == null)
                alphaAnimation = new SineAnimation(210, 90, 4);
            g.setColor(player.getOriginalColor() == GameColor.RED ? new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), alphaAnimation.getCurrentValue()) : new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), alphaAnimation.getCurrentValue()));
            alphaAnimation.start();
        }

        if(alphaAnimation != null && alphaAnimation.isRunning() && !player.hasShield())
            alphaAnimation.end();

        g.fillRect((this.getWidth() / 2) - player.getSize() / 2, (this.getHeight() / 2) - player.getSize() / 2, player.getSize(), player.getSize());

        if(player.hasStar()) {
            g.setColor(Color.PINK);
            g.fillRect((this.getWidth() / 2) - player.getSize() / 2, (this.getHeight() / 2) - player.getSize() / 2, player.getSize(), player.getSize());
        }

        g.setColor(Color.WHITE);

        Font f1 = new Font("Comic Sans MS", Font.BOLD, 20-player.getName().length()-1);
        g.setFont(f1);
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(f1);
        // Determine the X coordinate for the text
        int x = (this.getWidth() - metrics.stringWidth(player.getName())) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = ((this.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setColor(Color.WHITE);
        g.drawString(player.getName(), x, y);

        // Set the font
        f1 = new Font("Comic Sans MS", Font.BOLD, 40);

        metrics = g.getFontMetrics(f1);

        g.setFont(f1);
        g.setColor(Color.BLACK);
        g.drawString("-", (this.getWidth() / 2) - (metrics.stringWidth("-")/2), 45);
        g.setColor(Color.RED);
        g.drawString(""+scoreRed, (this.getWidth() / 2) - (metrics.stringWidth("-")/2) - (metrics.stringWidth(""+scoreRed)), 45);
        g.setColor(Color.BLUE);
        g.drawString(""+scoreBlue, (this.getWidth() / 2) + (metrics.stringWidth("-")/2), 45);
        //debug screen
        if(debug)
        {
            f1 = new Font("Comic Sans MS", Font.BOLD, 10);
            g.setFont(f1);
            g.setColor(Color.BLACK);
            g.drawString("MouseX: "+(mouseX-(this.getWidth() / 2)), (this.getWidth() / 2) -200, 125);
            g.drawString("MouseY: "+((this.getWidth() / 2)-mouseY), (this.getWidth() / 2) -50, 125);
            g.drawString("angle:"+Math.toDegrees(Math.atan2(((this.getHeight() / 2)-mouseY),(mouseX-(this.getWidth() / 2)))), (this.getWidth() / 2) +50, 125);
            g.drawString("cos: "+Math.cos(Math.atan2(((this.getHeight() / 2)-mouseY),(mouseX-(this.getWidth() / 2)))), (this.getWidth() / 2) -200, 150);
            g.drawString("X: "+player.getX()+ "  Y: "+player.getY(), (this.getWidth() / 2)-50, 150);
            g.drawString("Tagged: "+Boolean.toString(player.isTagged()), (this.getWidth() / 2) +50, 150);
            g.drawString("Team: "+player.getOriginalColor(), (this.getWidth() / 2) +150, 150);
            g.drawString("sin: "+Math.sin(Math.atan2(((this.getHeight() / 2)-mouseY),(mouseX-(this.getWidth() / 2)))), (this.getWidth() / 2) -200, 175);
            g.drawString("width: "+(this.getWidth() / 2), (this.getWidth() / 2)-50,175 );
            g.drawString("height: "+(this.getHeight() / 2), (this.getWidth() / 2) +50, 175);
            g.drawString("hasFlag: " +Boolean.toString(player.getColor()==GameColor.SPECIAL),(this.getWidth() / 2) +150, 175);
            g.drawString("hasShield: "+Boolean.toString(player.hasShield()),(this.getWidth() / 2) -200, 200);
            g.drawString("hasStar: "+Boolean.toString(player.hasStar()),(this.getWidth() / 2) -50, 200);
            g.drawString("SprayRunning: "+Boolean.toString(sprayPaint.isRunning()),(this.getWidth() / 2) +50, 200);
            g.drawString("ID: "+(player.getID()),(this.getWidth() / 2) +150, 200);
            g.drawString("size: "+(player.getSize()),(this.getWidth() / 2) -200, 225);
            g.drawString("bluePickedUp:"+Boolean.toString(blueFlag.isPickedUp()),(this.getWidth() / 2) -50, 225);
            g.drawString("redPickedUp:"+Boolean.toString(redFlag.isPickedUp()),(this.getWidth() / 2)+50, 225);
            g.drawString("starVisible:"+Boolean.toString(star.isVisible()),(this.getWidth() / 2) +150, 225);
            g.drawString("sprayVisible:"+Boolean.toString(sprayPaint.isVisible()),(this.getWidth() / 2)-200, 250);
            g.drawString("shieldVisible:"+Boolean.toString(shield.isVisible()),(this.getWidth() / 2) -50, 250);
            g.drawString("Maxspeed:"+(1.0+this.getUpgradeBoxByType(UpgradeType.SPEED).getLevel()/10.5),(this.getWidth() / 2) +150, 250);
            g.drawString("canTag: "+Boolean.toString(player.getCanTag()),(this.getWidth() / 2) +50, 250);
        }
        f1 = new Font("Comic Sans MS", Font.BOLD, 20);

        metrics = g.getFontMetrics(f1);

        g.setFont(f1);

        g.setColor(Color.BLACK);

        if(player.isTagged())
            g.drawString(""+(player.getCurrentTimeLeft() + 1)+"s", (this.getWidth() / 2) - (metrics.stringWidth(""+player.getCurrentTimeLeft()+"s")/2), (this.getHeight() / 2) - 35);
        //draw other players
        for(OtherPlayer p : players) {

            if(p.isTagged()) {
                g.setColor(Color.ORANGE);
                g.fillRect(offsetX + p.getX() + (this.getWidth() / 2) - (player.getSize() / 2) - 8, offsetY + p.getY() + (this.getHeight() / 2) - (player.getSize() / 2) - 8, p.getSize() + 16, p.getSize() + 16);
            }

            if(p.getColor() == GameColor.BLUE)
                g.setColor(Color.BLUE);
            else if(p.getColor() == GameColor.RED)
                g.setColor(Color.RED);
            else if(p.getColor() == GameColor.SPECIAL) {
                Color color=new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
                g.setColor(color);
            }

            if(p.hasShield()) {
                if(alphaAnimation1 == null)
                    alphaAnimation1 = new SineAnimation(210, 90, 4);
                g.setColor(p.getColor() == GameColor.RED ? new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), alphaAnimation1.getCurrentValue()) : new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), alphaAnimation1.getCurrentValue()));
                alphaAnimation1.start();
            }

            if(alphaAnimation1 != null && alphaAnimation1.isRunning() && !p.hasShield())
                alphaAnimation1.end();

            g.fillRect(offsetX + p.getX() + (this.getWidth() / 2) - player.getSize() / 2, offsetY + p.getY() + (this.getHeight() / 2) - player.getSize() / 2, p.getSize(), p.getSize());

            if(p.hasStar()) {
                g.setColor(Color.PINK);
                g.fillRect(offsetX + p.getX() + (this.getWidth() / 2) - player.getSize() / 2, offsetY + p.getY() + (this.getHeight() / 2) - player.getSize() / 2, p.getSize(), p.getSize());
            }
            g.setColor(Color.WHITE);

            f1 = new Font("Comic Sans MS", Font.BOLD, 20-p.getName().length()-1);

            metrics = g.getFontMetrics(f1);

            // Determine the X coordinate for the text
            x = (offsetX + p.getX() + (this.getWidth() / 2)) - (metrics.stringWidth(p.getName()) / 2);
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            y = (offsetY + p.getY() + (this.getHeight() / 2)) - (metrics.getHeight() / 2) + metrics.getAscent();
            // Set the font
            g.setFont(f1);
            g.setColor(Color.WHITE);
            g.drawString(p.getName(), x, y);

        }

        //setting up the level bar
        f1 = new Font("Comic Sans MS", Font.BOLD, 14);
        metrics = g.getFontMetrics(f1);
        g.setFont(f1);

        levelBar.setScore(player.getScore());

        g.setColor(levelBar.getColor(0));

        levelBar.setWidth((this.getWidth() / 2));
        levelBar.setX((this.getWidth() / 2) - (levelBar.getWidth() / 2));

        g.fillRect(levelBar.getX(), levelBar.getY(), levelBar.getWidth(), levelBar.getHeight());
        //under this is for an optional outline
        //g.setColor(levelBar.getColor(2));
        //g.drawRect(levelBar.getX(), levelBar.getY(), levelBar.getWidth(), levelBar.getHeight());

        g.setColor(levelBar.getColor(1));

        int barWidth = levelBar.getProgressBarWidth();
        g.fillRect(levelBar.getX() + 5, levelBar.getY() + 5, barWidth > 10 ? barWidth - 10 : barWidth, levelBar.getHeight() - 10);

        g.setColor(Color.BLACK);

        g.drawString(""+levelBar.getScore() + "/" + levelBar.getMaxScore(), (this.getWidth() / 2) - (metrics.stringWidth(""+levelBar.getScore() + "/" + levelBar.getMaxScore()) / 2), levelBar.getY() + levelBar.getHeight() - (levelBar.getHeight() / 2) + 6);
        g.drawString("Level "+(levelBar.getLevel() - 1), (this.getWidth() / 2) - (metrics.stringWidth("Level "+(levelBar.getLevel() - 1)) / 2), levelBar.getY() + levelBar.getHeight() + 16);

        if(upgradeWindow.isEnabled()) {

            g.setColor(new Color(upgradeWindow.getColor(0).getRed(), upgradeWindow.getColor(0).getGreen(), upgradeWindow.getColor(0).getBlue(), upgradeWindow.getAlpha()));
            g.fillRect(upgradeWindow.getX(), upgradeWindow.getY(), upgradeWindow.getWidth(), upgradeWindow.getHeight());

            for(UpgradeBox box : upgradeWindow.getBoxes()) {

                f1 = new Font("Comic Sans MS", Font.BOLD, (18 - box.toString().length() - 1)*(int)((this.getWidth() / 2)/250.0));
                metrics = g.getFontMetrics(f1);
                g.setFont(f1);

                g.setColor(new Color(box.getColor(0).getRed(), box.getColor(0).getGreen(), box.getColor(0).getBlue(), box.getAlpha()));
                g.fillRect(box.getX(), box.getY(), box.getWidth(), box.getHeight());

                g.setColor(Color.WHITE);
                g.drawString(""+box.toString(), box.getX() + (box.getWidth() / 2) - (metrics.stringWidth(""+box.toString()) / 2), box.getY() + 15*(int)((this.getWidth() / 2)/250.0));

                f1 = new Font("Comic Sans MS", Font.BOLD,  (18 - box.toString().length() - 1)*(int)((this.getWidth() / 2)/250.0));
                metrics = g.getFontMetrics(f1);
                g.setFont(f1);

                g.drawString(""+box.getLevel() + "/" + box.getMaxLevel(), box.getX() + (box.getWidth() / 2) - (metrics.stringWidth(""+box.getLevel() + "/" + box.getMaxLevel()) / 2), box.getY() + (box.getHeight() / 2) + 12);

            }

        }

        if(start) start = false;
    }

    /**
     * This method is called every millisecond and is basically the game loop which does math, and paints to the screen
     */
    public void updateGame() {
        repaint();
        if(!start) {
            // determines the spawn place
            if(spawn)
            {
                if(player.getOriginalColor()==GameColor.BLUE)
                {
                    player.setX(7950-(int)(Math.random()*350+1));
                    player.setY(3950-(int)(Math.random()*350+1));
                }
                else
                {
                    player.setY((int)(Math.random()*350+1));
                    player.setX((int)(Math.random()*350+1));
                }
                spawn=false;
            }
            //moves the player according to the direction of the cursor.
            if(out != null && (Math.sqrt((Math.pow(this.getHeight() / 2-mouseY, 2) + Math.pow(mouseX-this.getWidth() / 2, 2))))>150)
                player.move(Math.atan2(((this.getHeight() / 2)-mouseY),(mouseX-(this.getWidth() / 2))),1+this.getUpgradeBoxByType(UpgradeType.SPEED).getLevel()/10.5,false);

            else if(out != null && (Math.sqrt((Math.pow(this.getHeight() / 2-mouseY, 2) + Math.pow(mouseX-this.getWidth() / 2, 2))))>75)
            {
                double distance=Math.sqrt((Math.pow(this.getHeight() / 2-mouseY, 2) + Math.pow(mouseX-this.getWidth() / 2, 2)))-75;
                player.move(Math.atan2(((this.getHeight() / 2)-mouseY),(mouseX-(this.getWidth() / 2))),distance/75,false);
            }
            else if(out !=null)
            {
                player.move(0,0.26,true);
            }
            if(player.getX()>4800 - player.getSize() && redFlag.isPickedUp() && redFlag.getPlayerID() == player.getID())
            {
                redFlag.putDown();
                scoreBlue++;

                player.setScore(player.getScore() + 8);

                if(out!=null)
                {
                    out.println("SCORE BLUE num:"+scoreBlue+"|");
                    out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                    if(blueFlag.isPickedUp())
                        out.println("FLAG red:-1|blue:"+blueFlag.getPlayerID());
                    else if(!blueFlag.isPickedUp())
                        out.println("FLAG red:-1|blue:-1");
                }

            }
            if(player.getX()<3200 && blueFlag.isPickedUp() && blueFlag.getPlayerID() == player.getID())
            {
                blueFlag.putDown();
                scoreRed++;

                player.setScore(player.getScore() + 8);

                if(out!=null)
                {
                    out.println("SCORE RED num:"+scoreRed+"|");
                    out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                    if(redFlag.isPickedUp())
                        out.println("FLAG red:"+redFlag.getPlayerID()+"|blue:-1");
                    else if(!redFlag.isPickedUp())
                        out.println("FLAG red:-1|blue:-1");
                }

            }

            if(player.getX() > 8000 - player.getSize())
                player.setX(8000 - player.getSize());

            if(player.getX() < 0)
                player.setX(0);

            if(player.getY() > 4000 - player.getSize())
                player.setY(4000 - player.getSize());

            if(player.getY() < 0)
                player.setY(0);

            if(player.getOriginalColor() == GameColor.RED && player.getY() > 1600 - player.getSize() && player.getY() < 2400 && player.getX() > 400 - player.getSize() && player.getX() < 1200-player.getSize())
                player.setCanTag(false);
            else if(player.getOriginalColor() == GameColor.RED && !(player.getY() > 1600 - player.getSize() && player.getY() < 2400 && player.getX() > 400 - player.getSize() && player.getX() < 1200-player.getSize()))
                player.setCanTag(true);
            if(player.getOriginalColor() == GameColor.BLUE && player.getY() > 1600 - player.getSize() && player.getY() < 2400 && player.getX() > 6800 - player.getSize() && player.getX() < 7600-player.getSize())
                player.setCanTag(false);
            else if(player.getOriginalColor() == GameColor.BLUE && !(player.getY() > 1600 - player.getSize() && player.getY() < 2400 && player.getX() > 6800 - player.getSize() && player.getX() < 7600-player.getSize()))
                player.setCanTag(true);

            if(levelBar.canLevelUp()) {
                upgradeWindow.setEnabled(true);
            }

            if(redFlag.intersectsWith(player) && player.getOriginalColor() == GameColor.BLUE && !redFlag.isPickedUp()) {
                redFlag.pickUp(player.getID());
                if(out != null) {

                    out.println("COLOR ID:" + player.getID() + "|Color:3|");

                    if(blueFlag.isPickedUp())
                        out.println("FLAG red:"+player.getID()+"|blue:"+blueFlag.getPlayerID());
                    else if(!blueFlag.isPickedUp())
                        out.println("FLAG red:"+player.getID()+"|blue:-1");
                }
            }

            if(blueFlag.intersectsWith(player) && player.getOriginalColor() == GameColor.RED && !blueFlag.isPickedUp()) {
                blueFlag.pickUp(player.getID());

                if(out != null) {

                    out.println("COLOR ID:" + player.getID() + "|Color:3|");

                    if(redFlag.isPickedUp())
                        out.println("FLAG red:"+redFlag.getPlayerID()+"|blue:"+player.getID());
                    else if(!redFlag.isPickedUp())
                        out.println("FLAG red:-1|blue:"+player.getID());
                }
            }

            if(sprayPaint.intersectsWith(player) && sprayPaint.isVisible()) {
                out.println("SPECIAL item:0|visible:-1|");
                out.println("ITEM START SPRAYPAINT ID:"+player.getID()+"|");

                Timer sprayPaintTimer = new Timer();
                sprayPaintTimer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {

                        out.println("ITEM END SPRAYPAINT ID:"+player.getID()+"|");

                        sprayPaint.setRunning(false, null);

                        sprayPaintTimer.cancel();
                        sprayPaintTimer.purge();

                    }
                }, 7500, 7500);

                sprayPaint.setVisible(false);
                sprayPaint.setRunning(true, player.getOriginalColor() == GameColor.RED ? GameColor.BLUE : GameColor.RED);
            }
            if(shield.intersectsWith(player) && shield.isVisible()) {
                out.println("SPECIAL item:1|visible:-1|");
                out.println("ITEM START SHIELD ID:"+player.getID()+"|");

                Timer shieldTimer = new Timer();
                shieldTimer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {

                        out.println("ITEM END SHIELD ID:"+player.getID()+"|");

                        player.setHasShield(false);

                        shieldTimer.cancel();
                        shieldTimer.purge();

                    }
                }, 7500, 7500);

                shield.setVisible(false);
                player.setHasShield(true);
            }
            if(star.intersectsWith(player) && star.isVisible()) {
                out.println("SPECIAL item:2|visible:-1|");
                out.println("ITEM START STAR ID:"+player.getID()+"|");

                Timer starTimer = new Timer();
                starTimer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {

                        out.println("ITEM END STAR ID:"+player.getID()+"|");

                        player.setHasStar(false);

                        starTimer.cancel();
                        starTimer.purge();

                    }
                }, 7500, 7500);

                star.setVisible(false);
                player.setHasStar(true);
            }

            for(OtherPlayer p : players) {

                if(p.getOriginalColor() == GameColor.RED && p.getY() > 1600 - p.getSize() && p.getY() < 2400 && p.getX() > 400 - p.getSize() && p.getX() < 1200-p.getSize())
                    p.setCanTag(false);
                else if(p.getOriginalColor() == GameColor.RED && !(p.getY() > 1600 - p.getSize() && p.getY() < 2400 && p.getX() > 400 - p.getSize() && p.getX() < 1200-p.getSize()))
                    p.setCanTag(true);
                if(p.getOriginalColor() == GameColor.BLUE && p.getY() > 1600 - p.getSize() && p.getY() < 2400 && p.getX() > 6800 - p.getSize() && p.getX() < 7600-p.getSize())
                    p.setCanTag(false);
                else if(p.getOriginalColor() == GameColor.BLUE && !(p.getY() > 1600 - p.getSize() && p.getY() < 2400 && p.getX() > 6800 - p.getSize() && p.getX() < 7600-p.getSize()))
                    p.setCanTag(true);

                if(p.intersectsWith(player)) {

                    if((player.hasStar() && player.getX() < 3200 && player.getOriginalColor() != p.getOriginalColor() && !p.isTagged() && player.getCanTag() && !p.hasShield()) || (player.hasStar() && player.getX() > 4800 && player.getOriginalColor() != p.getOriginalColor() && !p.isTagged() && player.getCanTag() && !p.hasShield())) {
                        out.println("TAG TAGGED by:"+player.getID()+"|to:"+p.getID() + "|");
                        System.out.println("DD1");
                    } else if((p.hasStar() && p.getX() < 3200 && player.getOriginalColor() != p.getOriginalColor() && !player.isTagged() && p.canTag() && !player.hasShield()) || (p.hasStar() && p.getX() > 4800 && player.getOriginalColor() != p.getOriginalColor()  && !player.isTagged() && p.canTag() && !player.hasShield())) {
                        player.setTagged(true);
                        System.out.println("DD2");
                        out.println("TAG TAGGED by:"+p.getID()+"|to:"+player.getID() + "|");
                        if(redFlag.getPlayerID() == player.getID())
                            if(out != null) {
                                out.println("FLAG red:-1|blue:" + blueFlag.getPlayerID());
                                out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                            }
                        if(blueFlag.getPlayerID() == player.getID())
                            if(out != null) {
                                out.println("FLAG red:"+redFlag.getPlayerID()+"|blue:-1");
                                out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                            }
                    } else if(!p.hasStar() && !player.hasStar()) {

                        if(player.getX() < 3200 && player.getOriginalColor() == GameColor.BLUE && p.getOriginalColor() == GameColor.RED && !player.isTagged() && p.canTag() && !player.hasShield()){
                            out.println("TAG TAGGED by:"+p.getID()+"|to:"+player.getID() + "|");
                            System.out.println("DD3");
                            player.setTagged(true);
                            if(redFlag.getPlayerID() == player.getID())
                                if(out != null) {
                                    out.println("FLAG red:-1|blue:" + blueFlag.getPlayerID());
                                    out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                                }
                            if(blueFlag.getPlayerID() == player.getID())
                                if(out != null) {
                                    out.println("FLAG red:"+redFlag.getPlayerID()+"|blue:-1");
                                    out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                                }

                        }
                        if(player.getX() < 3200 && player.getOriginalColor() == GameColor.RED && p.getOriginalColor() == GameColor.BLUE && !p.isTagged() && player.getCanTag() && !p.hasShield()){
                            out.println("TAG TAGGED by:"+player.getID()+"|to:"+p.getID() + "|");
                            System.out.println("DD4");
                        }
                        if(player.getX() > 4800 && player.getOriginalColor() == GameColor.RED && p.getOriginalColor() == GameColor.BLUE && !player.isTagged() && p.canTag() && !player.hasShield()){
                            player.setTagged(true);
                            System.out.println("DD5");
                            out.println("TAG TAGGED by:"+p.getID()+"|to:"+player.getID() + "|");
                            if(redFlag.getPlayerID() == player.getID())
                                if(out != null) {
                                    out.println("FLAG red:-1|blue:" + blueFlag.getPlayerID());
                                    out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                                }
                            if(blueFlag.getPlayerID() == player.getID())
                                if(out != null) {
                                    out.println("FLAG red:"+redFlag.getPlayerID()+"|blue:-1");
                                    out.println("COLOR ID:" + player.getID() + "|Color:"+player.getOriginalColor().getValue()+"|");
                                }
                        }
                        if(player.getX() > 4800 && player.getOriginalColor() == GameColor.BLUE && p.getOriginalColor() == GameColor.RED && !p.isTagged() && player.getCanTag() && !p.hasShield()){
                            out.println("TAG TAGGED by:"+player.getID()+"|to:"+p.getID() + "|");
                            System.out.println("DD6");
                        }
                        if(player.isTagged() && p.getOriginalColor() == player.getOriginalColor())
                        {
                            player.setTagged(false);
                            out.println("TAG UNTAGGED by:"+p.getID()+"|to:"+player.getID() + "|");
                        }
                        if(p.isTagged() && p.getOriginalColor() == player.getOriginalColor())
                        {
                            out.println("TAG UNTAGGED by:"+player.getID()+"|to:"+p.getID() + "|");
                        }
                    }
                }

            }

            coordTimer ++;

            if(offsetX != -player.getX() || offsetY != -player.getY())
                canSendCoords = true;

            offsetX = -player.getX();
            offsetY = -player.getY();

            if(canSendCoords && out != null && coordTimer >= 40) {
                out.println("COORD ID:" + player.getID() + "|X:" + player.getX() + "|Y:" + player.getY());
                coordTimer = 0;
            }

            if(out!=null)
                canSendCoords = false;

        }

    }

    /**
     * This method allows the player to use the keyboard
     */
    public void keyPressed(KeyEvent e) {
        if (out != null) {
            if(player.getName().equals("debug")&& e.getKeyCode() == KeyEvent.VK_W &&debug) {
                player.setScore(player.getScore() + 1);
            }
            if(player.getName().equals("debug")&& e.getKeyCode() == KeyEvent.VK_S &&debug) {
                player.setTagged(true);
                out.println("TAG TAGGED by:0|to:"+player.getID()+"|");
            }
            if(player.getName().equals("debug")&& e.getKeyCode() == KeyEvent.VK_A &&debug) {
                player.setTagged(false);
                out.println("TAG UNTAGGED by:0|to:"+player.getID()+"|");
            }
            if(e.getKeyCode() ==KeyEvent.VK_1 &&upgradeWindow.isEnabled())
            {
                if(upgradeWindow.getBox(0).getLevel() < 5)
                {
                    levelBar.levelUp();
                    upgradeWindow.setEnabled(false);
                    this.upgradeWindow.selectBoxByKey(UpgradeType.SPEED);
                }
            }
            if(e.getKeyCode() ==KeyEvent.VK_2 &&upgradeWindow.isEnabled())
            {
                if( upgradeWindow.getBox(1).getLevel() < 5)
                {
                    levelBar.levelUp();
                    upgradeWindow.setEnabled(false);
                    this.upgradeWindow.selectBoxByKey(UpgradeType.SMALL);
                    if(upgradeWindow.getBox(1).getLevel() > 0) {
                        if(player.getSize() != (50 - (upgradeWindow.getBox(1).getLevel() * 5)))
                            out.println("SIZE ID:" + player.getID() + "|value:" + (50 - (upgradeWindow.getBox(1).getLevel() * 5)) + "|");
                        player.setSize((50 - (upgradeWindow.getBox(1).getLevel() * 5)));
                    }
                }
            }
            if(e.getKeyCode() ==KeyEvent.VK_3 &&upgradeWindow.isEnabled())
            {
                if(upgradeWindow.getBox(2).getLevel() < 5)
                {
                    levelBar.levelUp();
                    upgradeWindow.setEnabled(false);
                    this.upgradeWindow.selectBoxByKey(UpgradeType.BIG);
                    if(upgradeWindow.getBox(2).getLevel() > 0 ) {
                        if(player.getSize() != (50 + (upgradeWindow.getBox(2).getLevel() * 5)))
                            out.println("SIZE ID:" + player.getID() + "|value:" + (50 + (upgradeWindow.getBox(2).getLevel() * 5)) + "|");
                        player.setSize((50 + (upgradeWindow.getBox(2).getLevel() * 5)));
                    }
                }
            }
            if(e.getKeyCode() ==KeyEvent.VK_4 &&upgradeWindow.isEnabled())
            {
                if(upgradeWindow.getBox(3).getLevel() < 5)
                {
                    levelBar.levelUp();
                    upgradeWindow.setEnabled(false);
                    this.upgradeWindow.selectBoxByKey(UpgradeType.TAGTIMER);
                    if(upgradeWindow.getBox(3).getLevel() > 0)
                        player.setTimeLeft(-5*(upgradeWindow.getBox(3).getLevel()) + 30);
                }
            }
            if(e.getKeyCode() == KeyEvent.VK_D)
                debug=debug?false:true;
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    /**
     * This mouse click will be detected only when the upgrade window is enabled and lets you select an upgrade for your character
     */
    public void mouseClicked(MouseEvent e) {
        if(upgradeWindow.isEnabled()) {
            if(upgradeWindow.selectBox(e.getX(), e.getY())) {
                levelBar.levelUp();
                upgradeWindow.setEnabled(false);

                if(upgradeWindow.getBox(1).getLevel() > 0) {
                    if(player.getSize() != (50 - (upgradeWindow.getBox(1).getLevel() * 5)))
                        out.println("SIZE ID:" + player.getID() + "|value:" + (50 - (upgradeWindow.getBox(1).getLevel() * 5)) + "|");
                    player.setSize((50 - (upgradeWindow.getBox(1).getLevel() * 5)));
                }

                if(upgradeWindow.getBox(2).getLevel() > 0) {
                    if(player.getSize() != (50 + (upgradeWindow.getBox(2).getLevel() * 5)))
                        out.println("SIZE ID:" + player.getID() + "|value:" + (50 + (upgradeWindow.getBox(2).getLevel() * 5)) + "|");
                    player.setSize((50 + (upgradeWindow.getBox(2).getLevel() * 5)));
                }

                if(upgradeWindow.getBox(3).getLevel() > 0)
                    player.setTimeLeft(-5*(upgradeWindow.getBox(3).getLevel()) + 30);
            }
        }
    }

    /**
     * updates the position of the mouse
     */
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    /**
     * updates the position of the mouse
     */
    public void mouseMoved(MouseEvent e) {

        mouseX = e.getX();
        mouseY = e.getY();

    }

    /**
     * @param the client's print writer output variable to send data to the server
     */
    public void setOut(PrintWriter out) {
        this.out = out;
        player.setOut(out);
    }

    /**
     * @param Takes in an OtherPlayer object which gets added to the arraylist of players in the game
     */
    public void addPlayer(OtherPlayer player) {
        players.add(player);
        System.out.println("ADDED PLAYER");
    }

    /**
     * @return if able to add a player based on if the id passed in is already contained in the arraylist or not
     * @param the ID to check if able to be added into the array list
     */
    public boolean canAddPlayer(int ID) {
        for(OtherPlayer p : players)
            if(p.getID() == ID)
                return false;

        //dont add this client to players
        if(this.player.getID() == ID)
            return false;

        return true;
    }

    /**
     * @return the soecific upgrade box going by the UpgradeType
     * @param a UpgradeType that will return that specific type form the list if it can find any
     */
    public UpgradeBox getUpgradeBoxByType(UpgradeType type)
    {
        for(UpgradeBox box: upgradeWindow.getBoxes())
            if(box.getType()==type)
                return box;
        return null;
    }

    /**
     * @return an OtherPlayer object that was gotten from the arraylist of connected players
     * @param an Identification to pull out the right one from the arraylist
     */
    public OtherPlayer getOtherPlayerByID(int ID) {
        for(OtherPlayer p : players)
            if(p.getID() == ID)
                return p;
        return null;
    }

    /**
     * removes a connected(OtherPlayer object) player from the connected players arraylist
     * @param identifiaction to tell which one to remove
     */
    public void removeOtherPlayer(int ID) {
        if(players!=null)
            for(int x = 0; x < players.size(); x ++)
                if(players.get(x).getID() == ID)
                    players.remove(x);
    }

    /**
     * @param the value of the score, and the color of the team which has that score'
     */
    public void setScore(int score, int color)
    {
        if(color==1)
            scoreBlue=score;
        if(color==2)
            scoreRed=score;
    }

    /**
     * if val == -1 the redflag gets put back in its spawn location, but the second option pickeds it up with val
     * @param val is the ID of the player who picked it up
     */
    public void setRedFlag(int val) {
        if(val == -1)
            redFlag.putDown();
        else
            redFlag.pickUp(val);
    }

    /**
     * if val == -1 the blueflag gets put back in its spawn location, but the second option pickeds it up with val
     * @param val is the ID of the player who picked it up
     */
    public void setBlueFlag(int val) {
        if(val == -1)
            blueFlag.putDown();
        else
            blueFlag.pickUp(val);
    }

    /**
     * this method takes in data from a server and decides if the items are spawned it or not
     * @param the index of the item being referred to, the value(1 or -1) decides if it will be visible or not
     */
    public void setVisibleItems(int index, boolean value) {
        if(index == 0)
            sprayPaint.setVisible(value);
        if(index == 1)
            shield.setVisible(value);
        if(index == 2)
            star.setVisible(value);
    }

    /**
     * @return a SprayPaint object that can be activated and what not
     */
    public SprayPaint getSprayPaint() {
        return sprayPaint;
    }

    /**
     * allows to set the boolean spawn from client class
     * @param boolean value
     */
    public void setSpawn(boolean spawn)
    {
        this.spawn=spawn;
    }

    /**
     * deallocates memory
     */
    public void freeMemory()
    {
        out=null;
        player=null;
        background=null;
        redFlag=null;
        blueFlag=null;
        sprayPaint=null;
        shield=null;
        star=null;
        players =null;
        levelBar=null;
        upgradeWindow=null;
        alphaAnimation=null;
        alphaAnimation1=null;
    }
}
