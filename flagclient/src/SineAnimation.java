
/**
 * Write a description of class SineAnimation here.
 * class for the animation
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
import java.util.Timer;
import java.util.TimerTask;
public class SineAnimation
{

    private int currentValue;

    private int start, end, time;

    Timer timer;

    private boolean isRunning;

    /**
     * @param starting value of the sine animation, ending value of the sine animation to turn around at, time for it to update
     */
    public SineAnimation(int start, int end, int time) {
        this.start = start;
        this.end = end;
        this.time = time;

        currentValue = start;
        isRunning = false;
    }

    /**
     * runs the animation
     */
    public void start() {

        if(!isRunning) {

            isRunning = true;

            currentValue = start;

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                private int increment = 1;

                public void run() {

                    if(currentValue == start) {

                        if(start > end)
                            increment = -1;
                        else
                            increment = 1;

                    } else if(currentValue == end) {

                        if(end > start)
                            increment = -1;
                        else
                            increment = 1;

                    }

                    currentValue += increment;

                }
            }, 0, time);

        }
    }

    /**
     * ends the animation
     */
    public void end() {

        if(isRunning) {
            timer.cancel();
            timer.purge();

            isRunning = false;
        }

    }

    /**
     * @return the amount of time the animation had run
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * @return the start timer
     */
    public int getStartValue() {
        return start;
    }

    /**
     * @return the time the animation is supposed to end
     */
    public int getEndValue() {
        return end;
    }

    /**
     * set the start time of the animation
     */
    public void setStartValue(int start) {
        this.start = start;
    }

    /**
     * set the time the animation is supposed to end
     */
    public void setEndValue(int end) {
        this.end = end;
    }

    /**
     * @return boolean wheter the animation is running or not
     */
    public boolean isRunning() {
        return isRunning;
    }

}
