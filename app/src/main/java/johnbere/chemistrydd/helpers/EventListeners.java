package johnbere.chemistrydd.helpers;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import johnbere.chemistrydd.Element;
import johnbere.chemistrydd.MainActivity;

// Just need to abstract away the event listeners to make the code a bit tidier
public class EventListeners {
    /**
     * Todo
     * If at all possible, perhaps create a super class that includes main activity,
     * meaning that subclasses will factor in all activities.
     */
    private MainActivity activity;

    public EventListeners(MainActivity activity) {
        this.activity = activity;
    }

    public View.OnDragListener LayoutDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();
            Element el = (Element)event.getLocalState();

            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //Log.d("JB", "Something is being dragged? Is it " + el.getName() + "?");
                    float new_x;
                    float new_y;
                    return true;

                // Set the dragged element to being invisible when the dragging has been initiated
                case DragEvent.ACTION_DRAG_ENTERED:
                    el.setVisibility(View.INVISIBLE);
                    Toast.makeText(activity, "Dragging " + el.getName(), Toast.LENGTH_SHORT).show();
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    //Log.d("JB", "The dragging of " + el.getName() + " ended lol");
                    return false;

                case DragEvent.ACTION_DRAG_LOCATION:
                    // Log.d("JB", event.getX() + " " + event.getY());
                    if
                    (
                            // Acknowledges the limits of the screen
                            (event.getX() > view.getLeft() + 80 && event.getX() < view.getRight() - 80) &&
                            (event.getY() > view.getTop() + 80 && event.getY() < view.getBottom() - 80)
                    )
                    {
                        // Log.d("Bounds", "Acceptable");
                    }
                    else {
                        Toast.makeText(activity, el.getName() + " is out of bounds, reverting to its original position", Toast.LENGTH_SHORT).show();
                    }

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

                    if
                    (
                        (event.getX() > view.getLeft() + 80 && event.getX() < view.getRight() - 80) &&
                        (event.getY() > view.getTop() + 80 && event.getY() < view.getBottom() - 80)
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
}
