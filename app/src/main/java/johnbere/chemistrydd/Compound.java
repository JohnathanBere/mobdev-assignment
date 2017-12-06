package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Switch;
import android.widget.Toast;

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

        // Sort constituent elements by group
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

        this.name = compoundName;
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
            Toast.makeText(getContext(), "The compound " + this.getName() + " is about to split", Toast.LENGTH_SHORT).show();
            this.splitToConstituents();
        }
    }

    public void splitToConstituents() {
        this.setVisibility(INVISIBLE);
        String str = "Retrieved: ";
        for (Element el : this.elements) {
            str += el.getName() + ", \n";
        }
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        this.hasSplit = true;
    }

    public boolean getSplitFlag()
    {
        return this.splitFlag;
    }

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            Log.d("Double", "CLICK");
            setSplitFlag();
            String message = getSplitFlag() ? " is marked for death" : " is not gonna split, yo";
            Toast.makeText(getContext(), getName() + message, Toast.LENGTH_SHORT).show();
            return true;
        }

        /**
         * Todo
         *
         * When a compound is set to be split, shaking the phone will split the compound to its
         * composing elements
         */
        @Override
        public boolean onDown(MotionEvent event) {
            /**
             * The following handles logic autonomously for the triggering of the split flags
             * This gesture listener will basically mark a compound for splitting.
             */
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
