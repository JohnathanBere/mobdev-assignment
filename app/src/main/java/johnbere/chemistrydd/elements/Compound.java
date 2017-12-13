package johnbere.chemistrydd.elements;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import johnbere.chemistrydd.helpers.ElementGroup;

/**
 * Do bear in mind, that compounds may also contain other compounds.
 */
public class Compound extends Element {
    private ArrayList<Element> elements;
    private boolean splitFlag, hasSplit;
    private int originalShapeColor;
    private GestureDetector gestureDetector;

    public Compound(Context context, String name, String formula, float x, float y, int elementId, int color, ArrayList<Element> elements) {
        super(context, name, formula, x, y, elementId, color, null);
        this.elements = elements;
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.splitFlag = false;
        this.hasSplit = false;

        String form = "";
        String compoundName = "";

        // Do a check to see if there are any elements...
        if (this.elements != null) {
            // Sort constituent elements by element group
            Collections.sort(elements, new Comparator<Element>() {
                @Override
                public int compare(Element element1, Element element2) {
                    return element1.getGroup().compareTo(element2.getGroup());
                }
            });

            for (Element el : elements) {
                form = form + el.getFormula();
                if (el.getGroup() == ElementGroup.ALKALIMETALS)
                    compoundName = compoundName + el.getName() + " ";
                // Halogens will be suffixed with 'ide'
                if (el.getGroup() == ElementGroup.HALOGENS) {
                    StringBuilder halogenName = new StringBuilder(el.getName());
                    halogenName.setCharAt(halogenName.length() - 2, 'd');
                    compoundName = compoundName + halogenName;
                }
            }
        }

        this.name = !compoundName.equals("") ? compoundName : this.name;
        this.formula = form;
        this.redraw();
    }

    /**
     * A newly formed Compound should double in radius
     *
     * Figure out a way to animate the scale
     */
    public void redraw() {
        this.setShapeRadius(this.getShapeRadius() * 2);
        this.setSquareH(this.getSquareH() * 2);
        this.setSquareW(this.getSquareW() * 2);
        this.reCalculateCoord();
    }

    public void triggerCompoundSplit() {
        if (this.splitFlag) {
            // Toast.makeText(getContext(), this.getName() + " is splitting", Toast.LENGTH_SHORT).show();
            this.splitToConstituents();
        }
    }

    public void splitToConstituents() {
        this.setVisibility(INVISIBLE);
        String str = "Retrieved: \n";
        int elCount = this.elements.size() - 1;
        for (Element el : this.elements) {
            if (this.elements.indexOf(el) < elCount)
                str += el.getName() + ", \n";
            else
                str += el.getName();
        }
        // Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        this.hasSplit = true;
    }

    public boolean getSplitFlag()
    {
        return this.splitFlag;
    }

    // primes the compound for a split.
    public void setSplitFlag() {

        if (!this.splitFlag) {
            // Store the initial color of the compound
            this.splitFlag = true;
            this.setInitialShapeColor();
            this.setShapeColor(Color.MAGENTA);
        }
        else {
            this.splitFlag = false;
            this.setShapeColor(this.getOriginalShapeColor());
        }
        this.invalidate();
    }

    // checks to see if compound has split, before removing itself from the interactions class.
    public boolean hasCompoundSplit() {
        return this.hasSplit;
    }

    public ArrayList<Element> getElements() {
        return this.elements;
    }

    public int getOriginalShapeColor() {
        return this.originalShapeColor;
    }

    public void setInitialShapeColor() {
        this.originalShapeColor = this.getShapeColor();
    }

    // On the touchEvent of a compound, it should detect for possible double taps and then proceed to
    // set the flag for a compound to split.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    // A compound needs to be double tapped to be primed for a split and eventual deletion from the activity layout.
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            setSplitFlag();
            String message = getSplitFlag() ? " is marked to split" : " is not going to split";
            // Toast.makeText(getContext(), getName() + message, Toast.LENGTH_SHORT).show();
            return true;
        }


        @Override
        public boolean onDown(MotionEvent event) {
            /**
             * The following handles logic autonomously for the triggering of the split flags
             * This gesture listener will basically mark a compound for splitting.
             */
            // As long as the touched compound in question has a flag that is either true or false, contact
            // will have been made.
            if
            (
                (!splitFlag) &&
                (event.getX() > getRect().left && event.getX() < getRect().right) &&
                (event.getY() > getRect().top && event.getY() < getRect().bottom)
            )
            {
                return true;
            }
            else if
            (
                (splitFlag) &&
                (event.getX() > getRect().left && event.getX() < getRect().right) &&
                (event.getY() > getRect().top && event.getY() < getRect().bottom)
            )
            {
                return true;
            }
            return false;
        }
    };
}
