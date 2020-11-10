import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import java.util.*;

/**
 * This is a client class which will be connecting to a server and sending and recieving data
 *  @author: Tim Spaeth, James Lim, Jake Shapiro
 */
public class Client {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Flag.io");
    Player player = new Player("name", 50, 1, 225, 225, 1, 1);
    GamePanel panel = new GamePanel(new Dimension(500,500), player);

    /**
     * Sets up the JFrame and packs it
     */
    public Client() {
        frame.getContentPane().add(panel);
        frame.pack();
    }

    /**
     * @return IP address of the server
     */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Connect to the server",
                JOptionPane.QUESTION_MESSAGE);

    }

    /**
     * @return name of player
     * cuts the name if it has more than 8 letters
     * returns "" if the input is null
     */
    private String getName() {
        String n = JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);

        int maxLength = n==null ?0 :(n.length() < 9)?n.length():8;
        n = n==null? null :n.substring(0, maxLength);
        return n != null ? n : "";
    }

    /**
     * @return randomly selected team color for the player
     */
    private GameColor getColor() {
        return (int)(Math.random()*2 + 1) == 1 ? GameColor.BLUE : GameColor.RED;
        //return GameColor.BLUE;
    }

    /**
     * Connects to the server then enters the processing loop which keeps checking for data sent by the server
     */
    private void run() throws IOException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        while (true) {
            String line = in.readLine();
            if(!line.startsWith("COORD"))System.out.println(line);
            if (line.startsWith("SUBMITNAME")) {
                String name = getName();
                GameColor c = getColor();
                out.println("OTHERPLAYER Name:" + name + "|X:" + player.getX() + "|Y:" + player.getY() +
                        "|Size:" + player.getSize() + "|Color:" + c.getValue() + "|OriginalColor:" + c.getValue() + "|ID:" + player.getID());
                player.setColor(c);
                player.setOriginalColor(c);
                panel.setSpawn(true);
                player.setName(name);
            } else if (line.startsWith("NAMEACCEPTED")) {
                player.setID(Integer.parseInt(line.substring(line.indexOf("ID:") + 3)));
                panel.setOut(out);
            } else if (line.startsWith("NEWPLAYER")) {
                String name = line.substring(15, line.indexOf("|X:"));
                int x = Integer.parseInt(line.substring(line.indexOf("|X:") + 3,
                        line.indexOf("|Y:")));
                int y = Integer.parseInt(line.substring(line.indexOf("|Y:") + 3,
                        line.indexOf("|Size:")));
                int size = Integer.parseInt(line.substring(line.indexOf("|Size:") + 6,
                        line.indexOf("|Color:")));
                int color = Integer.parseInt(line.substring(line.indexOf("|Color:") + 7,
                        line.indexOf("|OriginalColor:")));
                int originalColor = Integer.parseInt(line.substring(line.indexOf("|OriginalColor:") + 15,
                        line.indexOf("|TAGGED:")));
                int tagged = Integer.parseInt(line.substring(line.indexOf("|TAGGED:") + 8,
                        line.indexOf("|SHIELD:")));
                int shield = Integer.parseInt(line.substring(line.indexOf("|SHIELD:") + 8,
                        line.indexOf("|STAR:")));
                int star = Integer.parseInt(line.substring(line.indexOf("|STAR:") + 6,
                        line.indexOf("|ID:")));
                int ID = Integer.parseInt(line.substring(line.indexOf("|ID:") + 4));
                if(panel.canAddPlayer(ID))
                    panel.addPlayer(new OtherPlayer(name, x, y, size, color == 1 ? GameColor.BLUE : color == 2 ? GameColor.RED : GameColor.SPECIAL, originalColor == 1 ? GameColor.BLUE : originalColor == 2 ? GameColor.RED : GameColor.SPECIAL, tagged, shield, star, ID));
            } else if (line.startsWith("COORD")) {
                int x = Integer.parseInt(line.substring(line.indexOf("X:") + 2, line.lastIndexOf("|")));
                int y = Integer.parseInt(line.substring(line.indexOf("Y:") + 2));
                int ID = Integer.parseInt(line.substring(line.indexOf("ID:") + 3, line.indexOf("|")));
                if(panel.getOtherPlayerByID(ID) != null) {
                    OtherPlayer p = panel.getOtherPlayerByID(ID);
                    p.setX(x);
                    p.setY(y);
                }
            } else if(line.startsWith("COLOR")) {

                int color = Integer.parseInt(line.substring(line.indexOf("Color:") + 6, line.lastIndexOf("|")));
                int ID = Integer.parseInt(line.substring(line.indexOf("ID:") + 3, line.indexOf("|")));

                if(panel.getOtherPlayerByID(ID) != null) {
                    OtherPlayer p = panel.getOtherPlayerByID(ID);
                    p.setColor(color == 1 ? GameColor.BLUE : color == 2 ? GameColor.RED : GameColor.SPECIAL);
                } else if(player.getID() == ID) {
                    player.setColor(color == 1 ? GameColor.BLUE : color == 2 ? GameColor.RED : GameColor.SPECIAL);
                }

            } else if(line.startsWith("SIZE")) {

                int ID = Integer.parseInt(line.substring(line.indexOf("ID:") + 3, line.indexOf("|value:")));
                int size = Integer.parseInt(line.substring(line.indexOf("value:") + 6, line.lastIndexOf("|")));

                if(panel.getOtherPlayerByID(ID) != null) {
                    OtherPlayer p = panel.getOtherPlayerByID(ID);
                    p.setSize(size);
                }

            } else if(line.startsWith("SCORE")) {

                if(line.contains("RED"))
                {
                    panel.setScore(Integer.parseInt(line.substring(line.indexOf("num:") + 4, line.lastIndexOf("|"))),2);
                }

                if(line.contains("BLUE"))
                {
                    panel.setScore(Integer.parseInt(line.substring(line.indexOf("num:") + 4, line.lastIndexOf("|"))),1);
                }

            } else if(line.startsWith("SPECIAL")) {

                int item = Integer.parseInt(line.substring(line.indexOf("item:") + 5, line.indexOf("|visible:")));
                int isOn = Integer.parseInt(line.substring(line.indexOf("visible:") + 8, line.lastIndexOf("|")));

                panel.setVisibleItems(item, isOn > 0 ? true : false);

            } else if(line.startsWith("ITEM")) {
                int ID = Integer.parseInt(line.substring(line.indexOf("ID:") + 3, line.lastIndexOf("|")));

                if(line.contains("SHIELD")) {

                    if(panel.getOtherPlayerByID(ID) != null) {
                        OtherPlayer p = panel.getOtherPlayerByID(ID);
                        if(line.contains("START"))
                            p.setHasShield(true);
                        if(line.contains("END"))
                            p.setHasShield(false);
                    }

                }

                if(line.contains("STAR ")) {

                    if(panel.getOtherPlayerByID(ID) != null) {
                        OtherPlayer p = panel.getOtherPlayerByID(ID);
                        if(line.contains("START"))
                            p.setHasStar(true);
                        if(line.contains("END"))
                            p.setHasStar(false);
                    }

                }

                if(line.contains("SPRAYPAINT")) {

                    if(panel.getOtherPlayerByID(ID) != null) {
                        OtherPlayer p = panel.getOtherPlayerByID(ID);
                        if(line.contains("START")) {
                            panel.getSprayPaint().setRunning(true, p.getOriginalColor() == GameColor.BLUE ? GameColor.RED : GameColor.BLUE);
                            if(panel.getSprayPaint().isRunning() && player.getOriginalColor() == panel.getSprayPaint().getTeamColor()) {
                                out.println("COLOR ID:"+player.getID()+"|Color:"+ (p.getOriginalColor() == GameColor.RED ? 2 : 1)+"|");
                            }
                        }
                        if(line.contains("END")) {
                            panel.getSprayPaint().setRunning(false, null);
                            out.println("COLOR ID:"+player.getID()+"|Color:"+ (player.getOriginalColor() == GameColor.RED ? 2 : 1)+"|");
                        }
                    }

                }

            } else if(line.startsWith("FLAG")) {

                if(line.contains("red"))
                {
                    panel.setRedFlag(Integer.parseInt(line.substring(line.indexOf("red:") + 4, line.lastIndexOf("|"))));
                }

                if(line.contains("blue"))
                {
                    panel.setBlueFlag(Integer.parseInt(line.substring(line.indexOf("blue:") + 5)));
                }

            } else if(line.startsWith("TAG")) {
                if(!line.contains("UNTAGGED") && line.contains("TAGGED")) {
                    int byID = Integer.parseInt(line.substring(line.indexOf("by:") + 3, line.indexOf("|to:")));
                    int toID = Integer.parseInt(line.substring(line.indexOf("to:") + 3, line.lastIndexOf("|")));

                    if(player.getID() == byID)
                        if(panel.getOtherPlayerByID(toID) != null) {
                            OtherPlayer p = panel.getOtherPlayerByID(toID);
                            if(!p.isTagged())
                                player.setScore(player.getScore() + 1);
                        }

                    if(panel.getOtherPlayerByID(toID) != null) {
                        OtherPlayer p = panel.getOtherPlayerByID(toID);
                        p.setTagged(true);
                    }

                }

                if(line.contains("UNTAGGED")) {
                    int byID = Integer.parseInt(line.substring(line.indexOf("by:") + 3, line.indexOf("|to:")));
                    int toID = Integer.parseInt(line.substring(line.indexOf("to:") + 3, line.lastIndexOf("|")));

                    if(player.getID() == byID)
                        if(panel.getOtherPlayerByID(toID) != null) {
                            OtherPlayer p = panel.getOtherPlayerByID(toID);
                            System.out.println(p.isTagged());
                            if(p.isTagged())
                                player.setScore(player.getScore() + 1);
                        }

                    if(panel.getOtherPlayerByID(toID) != null) {
                        OtherPlayer p = panel.getOtherPlayerByID(toID);
                        p.setTagged(false);
                    }
                }

            } else if(line.startsWith("EXITED")) {
                int ID = Integer.parseInt(line.substring(line.indexOf("ID:") + 3));


                panel.removeOtherPlayer(ID);

            } else if(line.startsWith("MESSAGE")) {
                System.out.println(line);
            }
        }

    }

    /**
     * Creats a new client and runs it
     */
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}