package johnbere.chemistrydd.helpers;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;

import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.R;


/**
 * This class is a helper class that will act as an intermediary between the basic activity sub-classes and the event listeners.
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
     * Displaced elements that intersect should respond by checking if they will react or not.
     * So if it was dropped near another element, and they can react they should combine (both disappearing) and re-appearing as
     * another 'compound'
     */
    private void findNearbyElements(View param) {
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
                // Trigger sound and vibrations to indicate that the element drop point was indeed valid
                activity.pop.start();
                activity.vibr.vibrate(500);
                this.reactElements(el, element);

                // Now updates the previous coordinates to a valid position
                el.setPrevX(el.getX());
                el.setPrevY(el.getY());

                // check if the reaction created one of the desired elements
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

                // Resets the element position to its last dragged position
                el.setX(el.getPrevX());
                el.setY(el.getPrevY());
                el.setSquareX((int)el.getPrevX());
                el.setSquareY((int)el.getPrevY());
                el.reCalculateCoord();

                activity.attemptsRemaining--;
                activity.numberOfAttempts++;
                String attemptsMessage = String.format(activity.res.getString(R.string.attempts), activity.attemptsRemaining);
                activity.attemptsText.setText(attemptsMessage);
                String failMsg = String.format(activity.res.getString(R.string.draggingFailedAction), el.getName(), element.getName());
                activity.questionText.setText(failMsg);

                if (activity.attemptsRemaining <= 0) {
                    activity.wereAttemptsExceeded = true;
                    activity.getResults();
                }
                break;
            }
        }
    }

    private void reactElements(Element firstElement, Element secondElement) {
        ArrayList<Element> reactants = new ArrayList<>();
        final int compoundMargins = activity.getResources().getInteger(R.integer.co_dim);

        // Add the reactants to formulate a formula name
        reactants.add(firstElement);
        reactants.add(secondElement);

        // Trigger the visibility of the elements to invisible
        firstElement.setVisibility(View.INVISIBLE);
        secondElement.setVisibility(View.INVISIBLE);

        // Increment the incrementor, and assign the elementId index to equal that
        this.incr++;
        activity.elementId = incr;

        // Instantiate a new compound, it will contain a list of the reacted elements.
        Compound compound = new Compound(activity, "", "", firstElement.getX(), firstElement.getY(),  this.incr, Color.GREEN, reactants);

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

        compound.setOnTouchListener(new EventListeners(activity).ElementTouchListener);

        activity.content.addView(compound);

        // Remove the reacted elements from the view
        activity.content.removeView(firstElement);
        activity.content.removeView(secondElement);

        String successMsg = String.format(activity.res.getString(R.string.draggingSuccessAction), compound.getName());
        activity.questionText.setText(successMsg);
    }

    /**
     * Updates the element in a list
     */
    void updateElementInList(View param) {
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

    public BaseActivity getActivity() {
        return this.activity;
    }
}
