package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import johnbere.chemistrydd.helpers.ElementGroup;
import johnbere.chemistrydd.helpers.EventListeners;

/**
 * Todo
 * Find a way to decompose compounds to their constituent elements.
 *
 * Implement sensor listener that shakes the element
 *
 * Put in place a class that iterates over the compound and element arraylist datastructures.
 */
public class MainActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        availableElements.add(new Element(this, "Sodium", "Na",100, 1000, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(this, "Chlorine", "Cl",400, 1000, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Bromine", "B",700, 1000, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Lithium", "Li",850, 900, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));

        // The helper class should now observe the elements
        interactions.setElements(availableElements);
        interactions.setIncr(availableElements.size());

        // Sets the event listener before appending this programmatically to the view.
        for (Element el : availableElements) {
            el.setTag(el.getName());
            el.setOnTouchListener(new EventListeners(this).ElementTouchListener);
            content.addView(el);
        }

        for (Compound co : availableCompounds) {
            co.setOnTouchListener(new EventListeners(this).ElementTouchListener);
            content.addView(co);
        }
        content.setOnDragListener(new EventListeners(this).LayoutDragListener);
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.content_main;
    }

    @Override
    protected Context getCurrentContext() {
        return MainActivity.this;
    }
}
