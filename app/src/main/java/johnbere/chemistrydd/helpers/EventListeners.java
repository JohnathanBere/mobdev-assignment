package johnbere.chemistrydd.helpers;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import johnbere.chemistrydd.BaseActivity;
import johnbere.chemistrydd.Compound;
import johnbere.chemistrydd.Element;
import johnbere.chemistrydd.MainActivity;
import johnbere.chemistrydd.R;

// Just need to abstract away the event listeners to make the code a bit tidier
public class EventListeners {
    /**
     * Todo
     * If at all possible, perhaps create a super class that includes main activity,
     * meaning that subclasses will factor in all activities.
     */
    private BaseActivity activity;

    public EventListeners(BaseActivity activity) {
        this.activity = activity;
    }

    public View.OnDragListener LayoutDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();
            Element el = (Element)event.getLocalState();

            // Ensures that the shadow is not dragged away from view.
            int dropMargin = activity.getResources().getInteger(R.integer.el_dim);

            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //Log.d("JB", "Something is being dragged? Is it " + el.getName() + "?");
                    float new_x;
                    float new_y;
                    return true;

                // Set the dragged element to being invisible when the dragging has been initiated
                case DragEvent.ACTION_DRAG_ENTERED:
                    el.setVisibility(View.INVISIBLE);
                    // Toast.makeText(activity, "Dragging " + el.getName(), Toast.LENGTH_SHORT).show();
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    //Log.d("JB", "The dragging of " + el.getName() + " ended lol");
                    return false;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return false;

                case DragEvent.ACTION_DROP:
                    // Log.d("JB", "The new location of " + el.getName() + " puts it at around " + event.getX() + " by " + event.getY());

                    // Error, when you drop outside of the main layout, the element disappears.
                    // This issue has been fixed, the element shadow returns to the original position
                    // of the element
                    new_x = event.getX();
                    new_y = event.getY();
                    float prevX = el.getX();
                    float prevY = el.getY();

                    // Double the margin if the dragged view is a compound
                    if (event.getLocalState() instanceof Compound)
                        dropMargin = activity.getResources().getInteger(R.integer.co_dim);

                    if
                    (
                        (event.getX() > view.getLeft() + dropMargin && event.getX() < view.getRight() - dropMargin) &&
                        (event.getY() > view.getTop() + dropMargin && event.getY() < view.getBottom() - dropMargin)
                    )
                    {
                        setShapeCoords(el, new_x, new_y);
                        return true;
                    }
                    else {
                        setShapeCoords(el, prevX, prevY);
                        return false;
                    }

                // Get the element to turn visible once dragging has stopped
                case DragEvent.ACTION_DRAG_EXITED:
                    el.setVisibility(View.VISIBLE);
                    return false;
            }
            return true;
        }

        private void setShapeCoords(Element el, float new_x, float new_y) {
            el.setX(new_x);
            el.setY(new_y);
            el.setSquareX((int)new_x);
            el.setSquareY((int)new_y);

            // re-aligns the element rectangle, detecting if any intersections have been made.
            el.reCalculateCoord();
            el.setVisibility(View.VISIBLE);

            activity.interactions.updateElementInList(el);
        }
    };

    public View.OnTouchListener ElementTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Element el = (Element)v;
            // Checks if the axes of the touch points match the axes of an element's grid box. If it does, it should proceed to move
            // that element
            if
            (
                (event.getX() > el.getSquareX() - el.getSquareW() && event.getX() < el.getSquareX() + el.getSquareW()) &&
                (event.getY() > el.getSquareY() - el.getSquareH() && event.getY() < el.getSquareY() + el.getSquareH())
            )
            {
                el.handleTouch(v, event);
            }
            return false;
        }
    };

    public ShakeEventListener.OnShakeListener OnShakeListener = new ShakeEventListener.OnShakeListener() {
        ArrayList<Compound> splitCompounds = new ArrayList<>();
        /**
         * Figure out a way to gracefully delete compounds from the activity as well as remove it from the interactions class... :)
         */
        @Override
        public void onShake() {
            iterateOverCompounds(activity.interactions.getCompounds());
            // The interactions class should proceed to remove any compound that has been split
            // from its observation
            if (splitCompounds.size() > 0) {
                // If at least 2 or more compounds were split a bang sound should be made
                if (splitCompounds.size() >= 2) {
                    activity.bang.start();
                    activity.vibr.vibrate(3000);
                }
                // other wise just another pop
                else if (splitCompounds.size() < 2) {
                    activity.pop.start();
                    activity.vibr.vibrate(500);
                }

                deletePrimedCompounds();
            }
            splitCompounds.clear();
        }

        void iterateOverCompounds(ArrayList<Compound> compounds) {
            for (Compound co : compounds) {
                co.triggerCompoundSplit();
                if (co.hasCompoundSplit()) {
                    elementsInCompound(co.getElements(), co.getX(), co.getY());
                    activity.content.removeView(co);
                    activity.pop.start();
                    activity.vibr.vibrate(500);
                    splitCompounds.add(co);
                }
            }
        }

        void elementsInCompound(ArrayList<Element> elements, float x, float y) {
            int margin = activity.getResources().getInteger(R.integer.el_dim);

            for (Element el : elements) {
                Element element = new Element(activity, el.getName(), el.getFormula(), x, y, activity.elementId++, el.getShapeColor(), el.getGroup());
                int currentIndex = elements.indexOf(el);

                if (currentIndex > 0) {
                    // Places the element in the array in a place relative to the previous element
                    int x_multiply = currentIndex * element.getSquareW();
                    // setting it right
                    if (x > activity.content.getLeft() + margin && x < activity.content.getWidth() / 2) {
                        element.setX(x + x_multiply);
                        element.setSquareX((int)(x + x_multiply));
                        element.reCalculateCoord();
                    }
                    // or setting it left
                    else if (x < activity.content.getRight() - margin && x > activity.content.getWidth() / 2) {
                        element.setX(x - x_multiply);
                        element.setSquareX((int)(x - x_multiply));
                        element.reCalculateCoord();
                    }
                }

                element.setOnTouchListener(new EventListeners(activity).ElementTouchListener);
                activity.content.addView(element);
                activity.interactions.addElementToList(element);
            }
        }

        // Finds out which compounds were split and should be removed from the interactions compound collection.
        void deletePrimedCompounds() {
            for (Compound decomposedCompound : splitCompounds) {
                activity.interactions.removeCompoundFromList(decomposedCompound);
            }
        }
    };
}
