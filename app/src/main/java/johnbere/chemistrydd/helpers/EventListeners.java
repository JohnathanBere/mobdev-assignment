package johnbere.chemistrydd.helpers;


import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import johnbere.chemistrydd.Compound;
import johnbere.chemistrydd.Element;
import johnbere.chemistrydd.MainActivity;

// Just need to abstract away the event listeners to make the code a bit tidier
public class EventListeners {
    public MainActivity activity;

    public EventListeners(MainActivity activity) {
        this.activity = activity;
    }

    public View.OnDragListener LayoutDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();

            Element el = (Element)event.getLocalState();

            // Log.d("JB", "Elements in interaction container" + interactions.getElements());

            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("JB", "Something is being dragged? Is it " + el.getName() + "?");
                    float new_x;
                    float new_y;

                    el.setVisibility(View.INVISIBLE);

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("JB", "The dragging of " + el.getName() + " ended lol");
                    return false;

                case DragEvent.ACTION_DROP:
                    Log.d("JB", "The new location of " + el.getName() + " puts it at around " + event.getX() + " by " + event.getY());

                    // Error, when you drop outside of the main layout, the element disappears.
                    new_x = event.getX();
                    new_y = event.getY();

                    el.x = new_x;
                    el.y = new_y;

                    el.r.x = (int)new_x;
                    el.r.y = (int)new_y;

                    // re-aligns the element rectangle, detecting if any intersections have been made.
                    el.reCalculateCoord();

                    el.setVisibility(View.VISIBLE);

                    // Log.d("JB", "The listener detects  " + activity.interactions.getElements().size + " elements");
//                    for (Element e : activity.interactions.getElements()) {
//                        Log.d(e.getName(), "Positions: " + e.x + ", " + e.y);
//                    }

                    activity.interactions.updateElementInList(el);

                    return true;
            }
            return true;
        }
    };


    public View.OnTouchListener ElementTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                Element el = (Element)v;
                // Checks if the axes of the touch points match the axes of an element's grid box. If it does, it should proceed to move
                // that element
                if ((event.getX() > el.r.x - el.r.width && event.getX() < el.r.x + el.r.width)
                        && (event.getY() > el.r.y - el.r.height && event.getY() < el.r.y + el.r.width)) {
                    el.handleTouch(el, event);
                }
            }
            catch(Exception ex) {
                Compound el = (Compound) v;
                // Checks if the axes of the touch points match the axes of an element's grid box. If it does, it should proceed to move
                // that element
                if ((event.getX() > el.r.x - el.r.width && event.getX() < el.r.x + el.r.width)
                        && (event.getY() > el.r.y - el.r.height && event.getY() < el.r.y + el.r.width)) {
                    el.handleTouch(el, event);
                }

                else {
                    Toast.makeText(activity, "There has been a serious error: " +  ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };
}
