package johnbere.chemistrydd.helpers;

import android.graphics.Color;
import android.util.Log;
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
    private int incr;

    public ViewInteractions(MainActivity activity) {
        this.activity = activity;
        this.compounds = new ArrayList<>();
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
     * Need to create a check to see whether an element or compound is one of the reactants.
     */


    public void findNearbyElements(View param) {
        Element el = (Element)param;
        int marginOfError = 50;
        for (Element element : this.elements) {
            // check if an element that isn't the same as what's on the parameter, is nearby
            if (
                    !(el instanceof Compound) &&
                    !(element instanceof Compound) &&
                    (el.getElementId() != element.getElementId()) &&
                    (
                            (element.getX() > el.getX() - marginOfError && element.getX() < el.getX() + marginOfError) &&
                            (element.getY() > el.getY() - marginOfError && element.getY() < el.getY() + marginOfError))
                    ) {
                Element foundElement = element;
                // Log.d(element.getName(), "The other element has the formula of " + element.getFormula());
                this.reactElements(el, element);
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

        this.incr = this.compounds != null ? this.elements.size() + this.compounds.size() : this.elements.size();
        this.incr++;

        // Instantiate
        Compound compound = new Compound(activity, "", "", firstElement.getX(), firstElement.getY(),  this.incr, Color.BLUE, reactants);
        addCompoundToList(compound);

        ViewGroup content = activity.findViewById(R.id.content_main);

        Log.d("JB", "The new id of the compound is " + compound.getElementId());

        // addCompoundToList(compound);

        compound.setOnTouchListener(new EventListeners(activity).ElementTouchListener);
        activity.player.start();

        content.addView(compound);

    }

    /**
     * Updates the element in a list
     * @param param
     */
    public void updateElementInList(View param) {
        Element castEl = (Element)param;
        for (Element element : this.elements) {
            // add this to the if statement otherwise el.getName().equals(element.getName())
            if (castEl.getElementId() == element.getElementId()) {
                // gets the index of the selected element provided that it is the same element as the instance element
                int index = this.elements.indexOf(element);
                this.elements.set(index, castEl);
                this.findNearbyElements(castEl);
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

    public void addCompoundToList(Compound compound) {
        this.compounds.add(compound);
    }

    public void removeElementFromList(Element element) {
        this.elements.remove(element);
    }
}
