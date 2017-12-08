package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.R;
import johnbere.chemistrydd.helpers.ElementGroup;

/**
 * Todo
 * Find a way to decompose compounds to their constituent elements.
 * Implement sensor listener that shakes the element
 * Put in place a class that iterates over the compound and element arraylist data structures.
 */
public class MainActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addElementsToLists();
        elementFactory();
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

    @Override
    protected void addElementsToLists() {
        Element firstElement = new Element(this, "Sodium", "Na",list_start_x, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS);
        int elementWidth = firstElement.getSquareW();
        availableElements.add(firstElement);
        availableElements.add(new Element(this, "Chlorine", "Cl",list_start_x + elementWidth, list_start_y, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Bromine", "B",list_start_x + elementWidth * 2, list_start_y, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Lithium", "Li",list_start_x + elementWidth * 3, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
    }
}
