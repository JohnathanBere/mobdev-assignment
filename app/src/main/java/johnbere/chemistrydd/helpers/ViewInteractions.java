package johnbere.chemistrydd.helpers;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import johnbere.chemistrydd.BaseActivity;
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
    private BaseActivity activity;
    private int incr;
    private Element element1, element2;

    public ViewInteractions(BaseActivity activity) {
        this.activity = activity;
        this.compounds = new ArrayList<>();
        this.element1 = null;
        this.element2 = null;
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
     * Todo
     * [x] Rename elements to reactant (as not only elements can form a compound, compounds can as well)
     * Need to create a check to see whether an element or compound is one of the reactants.
     */
    void findNearbyElements(View param) {
        Element el = (Element)param;
        for (Element element : this.elements) {
            // check if an element that isn't the same as what's on the parameter, is nearby
            if
            (
                // that the evaluated elements are not instances of compounds
                !(el instanceof Compound) &&
                !(element instanceof Compound) &&
                // that for these exercises, they do not belong in the same group
                (el.getGroup() != element.getGroup()) &&
                // and that the element is not trying to react with itself.
                (el.getElementId() != element.getElementId()) &&
                // Check if displaced element intersects with nearby shapes
                (el.getRect().intersect(element.getRect()))
            )
            {
                activity.pop.start();
                activity.vibr.vibrate(500);
                this.reactElements(el, element);
                break;
            }
            // If the statement similiar to the previous checks except the element and intersectioned element
            // Are of the same group, prevent the elements from combining.
            else if
            (
                !(el instanceof Compound) &&
                !(element instanceof Compound) &&
                (el.getGroup() == element.getGroup()) &&
                (el.getElementId() != element.getElementId()) &&
                (el.getRect().intersect(element.getRect()))
            )
            {
                activity.buzzer.start();
                activity.vibr.vibrate(3000);
                //Toast.makeText(activity, el.getName() + " & " + element.getName() + " cannot react!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    void reactElements(Element firstElement, Element secondElement) {
        ArrayList<Element> reactants = new ArrayList<>();
        final int compoundMargins = activity.getResources().getInteger(R.integer.co_dim);

        // Add the reactants to formulate a formula name
        reactants.add(firstElement);
        reactants.add(secondElement);

        // Trigger the visibility of the elements to invisible
        firstElement.setVisibility(View.INVISIBLE);
        secondElement.setVisibility(View.INVISIBLE);

        this.incr++;

        // Instantiate a new compound, it will contain a list of the reacted elements.
        Compound compound = new Compound(activity, "", "", firstElement.getX(), firstElement.getY(),  this.incr, Color.GREEN, reactants);
//        Toast.makeText(activity, compound.getName() + " was created!", Toast.LENGTH_SHORT).show();


        // Properly align the compound to its minimum confines as to not appear clipped when it has been created.
        if (compound.getX() < activity.content.getLeft() + compoundMargins) {
            compound.setX(compoundMargins);
            compound.setSquareX(compoundMargins);
            compound.reCalculateCoord();
        }
        else if (compound.getX() > activity.content.getRight() - compoundMargins) {
            int rightLimit = activity.content.getRight() - compoundMargins;
            compound.setX(rightLimit);
            compound.setSquareX(rightLimit);
            compound.reCalculateCoord();
        }
        if (compound.getY() < activity.content.getTop() + compoundMargins) {
            compound.setY(compoundMargins);
            compound.setSquareY(compoundMargins);
            compound.reCalculateCoord();
        }
        else if (compound.getY() > activity.content.getBottom() - compoundMargins) {
            int bottomLimit = activity.content.getBottom() - compoundMargins;
            compound.setY(bottomLimit);
            compound.setSquareY(bottomLimit);
            compound.reCalculateCoord();
        }

        addCompoundToList(compound);

        // Make the parameters equivalent to the property of the view interactions observer
        this.element1 = firstElement;
        this.element2 = secondElement;

        ViewGroup content = activity.findViewById(R.id.content_main);

        //Log.d("JB", "The new id of the compound is " + compound.getElementId());

        compound.setOnTouchListener(new EventListeners(activity).ElementTouchListener);

        content.addView(compound);

        // Remove the reacted elements from the view
        content.removeView(firstElement);
        content.removeView(secondElement);
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
                // updates the element with the given index in terms of axes.
                this.elements.set(index, castEl);
                // proceeds to find nearby elements to form a compound with
                this.findNearbyElements(castEl);
            }
        }

        // Remove the elements that were compared if they were acknowledged by this class
        if (element1 != null && element2 != null) {
            this.removeElementFromList(element1);
            this.removeElementFromList(element2);
            this.element1 = null;
            this.element2 = null;
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

    public void addElementToList(Element element) {
        this.elements.add(element);
    }

    public void removeElementFromList(Element element) {
        this.elements.remove(element);
    }

    public void removeCompoundFromList(Compound compound) {
        this.compounds.remove(compound);
    }

    public void setIncr (int incr) {
        this.incr = incr;
    }
}
