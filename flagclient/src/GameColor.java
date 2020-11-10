
/**
 * Enumeration class GameColor - enum for the team color and the special color
 *
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (version number or date here)
 */
public enum GameColor
{
    BLUE(1), RED(2), SPECIAL(3);

    private final int color;

    private GameColor(int color) {
        this.color = color;
    }

    public int getValue() {
        return color;
    }

}
