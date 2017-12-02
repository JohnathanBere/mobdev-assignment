package johnbere.chemistrydd.helpers;

import android.graphics.Point;
import android.view.View;

import java.util.ArrayList;

import johnbere.chemistrydd.Element;
import johnbere.chemistrydd.MainActivity;

public class ViewInteractions {
    private ArrayList<Element> elements;

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }


    /**
     * Write logic to check if an element's rectangle intersects with another.
     */

    /**
     * Updates the element in a list
     * @param paramElement
     */
    public void updateElementInList(Element paramElement) {
        for (Element element : this.elements) {
            // add this to the if statement otherwise el.getName().equals(element.getName())
            if (paramElement.getElementId() == element.getElementId()) {
                // gets the index of the selected element provided that it is the same element as the instance element
                int index = this.elements.indexOf(element);
                this.elements.set(index, paramElement);
            }
        }
    }
}
