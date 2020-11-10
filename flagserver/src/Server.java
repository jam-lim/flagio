import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the server class, it is what takes in data and outputs it to every client connected
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 */
public class Server {

    /**
     * The port that the server listens on.
     */
    private static final int PORT = 9001;

    private static int scoreRed=0, scoreBlue=0;
    private static int[] flagsInSpawn = new int[2]; //0 - RED, 1 - BLUE :: holds the id of person who has the flag, -1 if no one has it

    private static HashSet<OtherPlayer> players = new HashSet<OtherPlayer>();

    /**
     * The set of all the print writers for all the clients.  This
     * set is kept so we can easily broadcast messages.
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    private static Item sprayPaint, shield, star;
    private static Timer timer, timer1, timer2, timer3;

    private static boolean sprayPaintRunning;
    private static int sprayPaintRunningID = -1; //1 - blue, 2 - red

    /**
     * The appplications main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The flag.io server is running.");
        ServerSocket listener = new ServerSocket(PORT);

        sprayPaint = new Item();
        shield = new Item();
        star = new Item();

        sprayPaintRunning = false;

        for(int x = 0; x < flagsInSpawn.length; x ++) {
            flagsInSpawn[x] = -1;
        }

        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }
    /**
     * This thread extension called 'Game' is used for things that need to be centralized like a global timer for when items can spawn in
     */
    private static class Game extends Thread {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        /**
         * @param takes in a socket that allows us to send data and recieve data between clients
         */
        public Game(Socket socket) {
            this.socket = socket;
        }

        /**
         * this method checks if the items are spawned in or not and when they are not and able to be, it spawns them in on a 10 second interval
         */
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                timer3 = new Timer();
                timer3.scheduleAtFixedRate(new TimerTask() {

                    private int time;

                    public void run() {

                        boolean canSendData = false;

                        //25% chance every 10 seconds for the star item to spawn in
                        if(time%10 == 0 && !star.isVisible() && !star.isTaken()) {
                            int index = (int)(Math.random()*4);
                            System.out.println(index);
                            if(index == 2) {
                                canSendData = true;
                                star.setVisible(true);
                            }

                        }

                        if(time%10 == 0) {

                            if(!sprayPaint.isVisible() && !sprayPaint.isTaken()) {
                                sprayPaint.setVisible(true);
                                canSendData = true;
                            }

                            if(!shield.isVisible() && !shield.isTaken()) {
                                shield.setVisible(true);
                                canSendData = true;
                            }

                        }

                        for (Iterator<PrintWriter> it = writers.iterator(); it.hasNext();) {
                            PrintWriter writer = it.next();

                            if(star.isVisible() && !star.isTaken() && canSendData) {
                                writer.println("SPECIAL item:2|visible:1|");
                                System.out.println("SPECIAL item:2|visible:1|");
                            }

                            if(shield.isVisible() && !shield.isTaken() && canSendData) {
                                writer.println("SPECIAL item:1|visible:1|");
                                System.out.println("SPECIAL item:1|visible:1|");
                            }

                            if(sprayPaint.isVisible() && !sprayPaint.isTaken() && canSendData) {
                                writer.println("SPECIAL item:0|visible:1|");
                                System.out.println("SPECIAL item:0|visible:1|");
                            }

                        }

                        time++;

                    }
                }, 0, 1000);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and broadcasting its messages.
     */
    private static class Handler extends Thread {
        private OtherPlayer player;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        /**
         * @param socket that lets us send/recieve data between server and clients
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Gets the name of the player and keeps checking for inputs of data from the client
         */
        public void run() {
            new Game(socket).start();
            try {
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("SUBMITNAME");
                    String line = in.readLine();

                    //parsing info
                    if(line.startsWith("OTHERPLAYER")) {

                        String name = line.substring(17, line.indexOf("|X:"));
                        int x = Integer.parseInt(line.substring(line.indexOf("|X:") + 3,
                                line.indexOf("|Y:")));
                        int y = Integer.parseInt(line.substring(line.indexOf("|Y:") + 3,
                                line.indexOf("|Size:")));
                        int size = Integer.parseInt(line.substring(line.indexOf("|Size:") + 6,
                                line.indexOf("|Color:")));
                        int color = Integer.parseInt(line.substring(line.indexOf("|Color:") + 7,
                                line.indexOf("|OriginalColor:")));
                        int originalColor = Integer.parseInt(line.substring(line.indexOf("|OriginalColor:") + 15,
                                line.indexOf("|ID:")));
                        int ID = Integer.parseInt(line.substring(line.indexOf("|ID:") + 4));

                        player = new OtherPlayer(name, x, y, size, color, originalColor, ID);

                    }

                    if (player == null) {
                        return;
                    }

                    synchronized (players) {

                        boolean getID = true;

                        while(getID) {
                            getID = false;
                            for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                                OtherPlayer p = it.next();
                                if(p.getID() == player.getID()) {
                                    player.setID(p.getID() + 1);
                                    getID = true;
                                }
                            }
                        }

                        players.add(player);
                        break;
                    }
                }

                out.println("NAMEACCEPTED ID:" + player.getID());
                writers.add(out);

                for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                    OtherPlayer p = it.next();
                    out.println("NEWPLAYER Name:" + p.getName() + "|X:" + p.getX() + "|Y:" + p.getY() +
                            "|Size:" + p.getSize() + "|Color:" + p.getColor() + "|OriginalColor:" + p.getOriginalColor() + "|TAGGED:" + p.isTaggedInt() + "|SHIELD:" + (p.hasShield ? 1 : -1) + "|STAR:" + (p.hasStar ? 1 : -1) + "|ID:" + p.getID());
                }

                for (Iterator<PrintWriter> it = writers.iterator(); it.hasNext();) {
                    PrintWriter writer = it.next();
                    writer.println("NEWPLAYER Name:" + player.getName() + "|X:" + player.getX() + "|Y:" + player.getY() +
                            "|Size:" + player.getSize() + "|Color:" + player.getColor() + "|OriginalColor:" + player.getOriginalColor() + "|TAGGED:" + player.isTaggedInt() + "|SHIELD:" + (player.hasShield ? 1 : -1) + "|STAR:" + (player.hasStar ? 1 : -1) + "|ID:" + player.getID());
                }

                out.println("SCORE RED num:"+scoreRed+"|");
                out.println("SCORE BLUE num:"+scoreBlue+"|");
                out.println("FLAG red:" + flagsInSpawn[0] + "|blue:" + flagsInSpawn[1]);
                out.println("SPECIAL item:0|visible:"+(sprayPaint.isVisible() ? 1 : -1)+"|");
                out.println("SPECIAL item:1|visible:"+(shield.isVisible() ? 1 : -1)+"|");
                out.println("SPECIAL item:2|visible:"+(star.isVisible() ? 1 : -1)+"|");

                if(sprayPaintRunning) {
                    out.println("ITEM START SPRAYPAINT ID:"+sprayPaintRunningID+"|");
                }

                // Accepts messages from this client and broadcasts them to the other connected clients.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }

                    if(input.startsWith("COORD")) {

                        int x = Integer.parseInt(input.substring(input.indexOf("X:") + 2, input.lastIndexOf("|")));
                        int y = Integer.parseInt(input.substring(input.indexOf("Y:") + 2));
                        int ID = Integer.parseInt(input.substring(input.indexOf("ID:") + 3, input.indexOf("|")));

                        for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                            OtherPlayer p = it.next();
                            if(p.getID() == ID) {
                                p.setX(x);
                                p.setY(y);
                            }
                        }

                    }

                    if(input.startsWith("SIZE")) {

                        int ID = Integer.parseInt(input.substring(input.indexOf("ID:") + 3, input.indexOf("|value:")));
                        int size = Integer.parseInt(input.substring(input.indexOf("value:") + 6, input.lastIndexOf("|")));

                        for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                            OtherPlayer p = it.next();
                            if(p.getID() == ID) {
                                p.setSize(size);
                            }
                        }

                    }

                    if(input.startsWith("COLOR")) {

                        int color = Integer.parseInt(input.substring(input.indexOf("Color:") + 6, input.lastIndexOf("|")));
                        int ID = Integer.parseInt(input.substring(input.indexOf("ID:") + 3, input.indexOf("|")));

                        for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                            OtherPlayer p = it.next();
                            if(p.getID() == ID) {
                                p.setColor(color);
                            }
                        }

                    }

                    if(input.startsWith("ITEM")) {
                        int ID = Integer.parseInt(input.substring(input.indexOf("ID:") + 3, input.lastIndexOf("|")));

                        if(input.contains("SHIELD")) {

                            for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                                OtherPlayer p = it.next();
                                if(p.getID() == ID) {
                                    if(input.contains("START")) {
                                        shield.setTaken(true);
                                        p.setHasShield(true);
                                    }
                                    if(input.contains("END")) {
                                        shield.setTaken(false);
                                        p.setHasShield(false);
                                    }
                                }
                            }

                        }

                        if(input.contains("STAR ")) {

                            for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                                OtherPlayer p = it.next();
                                if(p.getID() == ID) {
                                    if(input.contains("START")) {
                                        star.setTaken(true);
                                        p.setHasStar(true);
                                    }
                                    if(input.contains("END")) {
                                        star.setTaken(false);
                                        p.setHasStar(false);
                                    }
                                }
                            }

                        }

                        if(input.contains("SPRAYPAINT")) {

                            for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                                OtherPlayer p = it.next();
                                if(p.getID() == ID) {
                                    if(input.contains("START")) {
                                        sprayPaint.setTaken(true);
                                        sprayPaintRunning = true;
                                        sprayPaintRunningID = ID;
                                    }
                                    if(input.contains("END")) {
                                        sprayPaint.setTaken(false);
                                        sprayPaintRunning = false;
                                        sprayPaintRunningID = -1;
                                    }
                                }
                            }

                        }

                    }

                    if(input.startsWith("SPECIAL")) {

                        int item = Integer.parseInt(input.substring(input.indexOf("item:") + 5, input.indexOf("|visible:")));
                        int isOn = Integer.parseInt(input.substring(input.indexOf("visible:") + 8, input.lastIndexOf("|")));

                        if(item == 0)
                            sprayPaint.setVisible(isOn > 0 ? true : false);
                        if(item == 1)
                            shield.setVisible(isOn > 0 ? true : false);
                        if(item == 2)
                            star.setVisible(isOn > 0 ? true : false);

                    }

                    if(input.startsWith("SCORE")) {

                        if(input.contains("RED"))
                        {
                            scoreRed=Integer.parseInt(input.substring(input.indexOf("num:") + 4, input.lastIndexOf("|")));
                        }

                        if(input.contains("BLUE"))
                        {
                            scoreBlue=Integer.parseInt(input.substring(input.indexOf("num:") + 4, input.lastIndexOf("|")));
                        }

                    }

                    if(input.startsWith("FLAG")) {

                        if(input.contains("red"))
                        {
                            flagsInSpawn[0] = Integer.parseInt(input.substring(input.indexOf("red:") + 4, input.lastIndexOf("|")));
                        }

                        if(input.contains("blue"))
                        {
                            flagsInSpawn[1] = Integer.parseInt(input.substring(input.indexOf("blue:") + 5));
                        }

                    }

                    if(input.startsWith("TAG")) {

                        if(input.contains("TAGGED")) {
                            int byID = Integer.parseInt(input.substring(input.indexOf("by:") + 3, input.indexOf("|to:")));
                            int toID = Integer.parseInt(input.substring(input.indexOf("to:") + 3, input.lastIndexOf("|")));

                            for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                                OtherPlayer p = it.next();
                                if(p.getID() == toID) {
                                    p.setTagged(true);
                                }
                            }

                            if(player.getID() == toID)
                                player.setTagged(true);

                        }

                        if(input.contains("UNTAGGED")) {
                            int byID = Integer.parseInt(input.substring(input.indexOf("by:") + 3, input.indexOf("|to:")));
                            int toID = Integer.parseInt(input.substring(input.indexOf("to:") + 3, input.lastIndexOf("|")));

                            for(Iterator<OtherPlayer> it = players.iterator(); it.hasNext();) {
                                OtherPlayer p = it.next();
                                if(p.getID() == toID) {
                                    p.setTagged(false);
                                }
                            }

                            if(player.getID() == toID)
                                player.setTagged(false);

                        }

                    }

                    for (Iterator<PrintWriter> it = writers.iterator(); it.hasNext();) {
                        PrintWriter writer = it.next();
                        writer.println(input);
                        System.out.println(input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                players.remove(player);
            }
            if (out != null) {
                writers.remove(out);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}