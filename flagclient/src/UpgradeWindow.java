
/**
 * Write a description of class UpgradeWindow here.
 * class for the upgrade window
 * @author: Tim Spaeth, James Lim, Jake Shapiro
 * @version (a version number or a date)
 */
import java.awt.Color;
import java.util.ArrayList;
public class UpgradeWindow extends GUIObject
{

    private ArrayList<UpgradeBox> boxes = new ArrayList<UpgradeBox>();

    public UpgradeWindow(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);

        super.setAlpha(0);
        super.setEnabled(false);
    }

    /**
     * add the upgrade box to the arraylist of upgrade box
     */
    public void addBox(UpgradeBox box) {
        boxes.add(box);
    }

    /**
     * @return the box in the arraylist <UpgradeBox> boxes with the index of the input index, null if the index is out of bound
     *
     */
    public UpgradeBox getBox(int index) {
        return index > -1 && index < boxes.size() ? boxes.get(index) : null;
    }

    /**
     * @return the arraylist <UpgradeBox> boxes
     */
    public ArrayList<UpgradeBox> getBoxes() {
        return boxes;
    }

    @Override
    /**
     * sets wheter the upgrade box is enabled or not
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled)
            super.setAlpha(125);
        else
            super.setAlpha(0);

        for(UpgradeBox box : boxes)
            box.setEnabled(enabled);
    }

    /**
     * @return true if box was selected
     */
    public boolean selectBox(int x, int y) {
        for(UpgradeBox box : boxes)
            if(box.intersectsWith(x,y) && box.isEnabled()) {
                box.setLevel(box.getLevel() + 1);
                return true;
            }
        return false;
    }

    /**
     * levels up the upgrade based on the type
     */
    public void selectBoxByKey(UpgradeType type)
    {
        for(UpgradeBox box : boxes)
            if(box.getType()==type) {
                box.setLevel(box.getLevel() + 1);
            }
    }

}
