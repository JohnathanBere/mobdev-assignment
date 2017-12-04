package johnbere.chemistrydd;

import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import johnbere.chemistrydd.helpers.ElementGroup;

/**
 * Do bear in mind, that compounds may also contain other compounds.
 */
public class Compound extends Element {
    private ArrayList<Element> elements;

    public Compound(Context context, String name, String formula, float x, float y, int elementId, int color, ArrayList<Element> elements) {
        super(context, name, formula, x, y, elementId, color, null);
        this.elements = elements;

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
}