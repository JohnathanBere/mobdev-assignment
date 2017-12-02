package johnbere.chemistrydd.helpers;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import johnbere.chemistrydd.Compound;
import johnbere.chemistrydd.Element;
import johnbere.chemistrydd.MainActivity;
import johnbere.chemistrydd.R;


/**
 * This class is a helper class that will act as an intermediary between the main activity and the event listeners.
 */
public class ViewInteractions {
    private ArrayList<Element> elements;
    private ArrayList<Compound> compounds;
    private MainActivity activity;

    public ViewInteractions(MainActivity activity) {
        this.activity = activity;
    }


    /**
     * Write logic to check if an element's rectangle intersects with another.
     *
     * What I have:
     * The ability to check for the elements in-memory. Adjusting their axes on the fly.
     * I want to now be able to detect the presence of another element within 5-10 pixels among one another.
     * So if it was dropped near another element, they should combine (both disappearing) and re-appearing as
     * another 'compound'
     *
     * To-do
     * Rename elements to reactant (as not only elements can form a compound, compounds can as well)
     */


    public void findNearByElements(Element param) {
        int marginOfError = 50;
        for (Element element : this.elements) {
            // check if an element that isn't the same as what's on the parameter, is nearby
            if (
                    (param.getElementId() != element.getElementId()) &&
                    (
                            (element.x > param.x - marginOfError && element.x < param.x + marginOfError) &&
                            (element.y > param.y - marginOfError && element.y < param.y + marginOfError))
                    ) {
                Element foundElement = element;
                // Log.d(element.getName(), "The other element has the formula of " + element.getFormula());
                this.reactElements(param, element);
                break;
            }
        }
    }

    public void reactElements(Element firstElement, Element secondElement) {
        ArrayList<Element> reactants = new ArrayList<>();

        // Add the reactants to formulate a formula name
        reactants.add(firstElement);
        reactants.add(secondElement);

        // Trigger the visibility of the elements to invisible
        firstElement.setVisibility(View.INVISIBLE);
        secondElement.setVisibility(View.INVISIBLE);
        // Log.d("REACTION", "The resulting compound is " + firstElement.getFormula() + secondElement.getFormula());

        // Instantiate
        Compound compound = new Compound(activity, "", "", firstElement.x, firstElement.y, 6, Color.BLUE, reactants);
        ViewGroup content = activity.findViewById(R.id.content_main);
        // compound.setOnTouchListener(new EventListeners(activity).ElementTouchListener);
        content.addView(compound);
        compound.setOnTouchListener(new EventListeners(activity).ElementTouchListener);
    }

    /**
     * Updates the element in a list
     * @param param
     */
    public void updateElementInList(Element param) {
        for (Element element : this.elements) {
            // add this to the if statement otherwise el.getName().equals(element.getName())
            if (param.getElementId() == element.getElementId()) {
                // gets the index of the selected element provided that it is the same element as the instance element
                int index = this.elements.indexOf(element);
                this.elements.set(index, param);
                this.findNearByElements(param);
            }
        }
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public ArrayList<Compound> getCompounds() {
        return compounds;
    }

    public void setCompounds(ArrayList<Compound> compounds) {
        this.compounds = compounds;
    }
}
