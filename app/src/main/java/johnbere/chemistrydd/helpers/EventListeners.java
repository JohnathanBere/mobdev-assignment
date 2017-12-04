package johnbere.chemistrydd.helpers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import johnbere.chemistrydd.Element;
import johnbere.chemistrydd.MainActivity;

// Just need to abstract away the event listeners to make the code a bit tidier
public class EventListeners {
    MainActivity activity;

    public EventListeners(MainActivity activity) {
        this.activity = activity;
    }

    public View.OnDragListener LayoutDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();

            DisplayMetrics dimensions = view.getResources().getDisplayMetrics();
            int windowWidth = dimensions.widthPixels;
            int windowHeight = dimensions.heightPixels;

            ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams)view.getLayoutParams();

            Element el = (Element)event.getLocalState();

            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("JB", "Something is being dragged? Is it " + el.getName() + "?");
                    float new_x;
                    float new_y;
                    float prev_x = el.getX();
                    float prev_y = el.getY();
                    el.setVisibility(View.INVISIBLE);

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("JB", "The dragging of " + el.getName() + " ended lol");
                    return false;

                case DragEvent.ACTION_DRAG_LOCATION:
                    // Log.d("JB", event.getX() + " " + event.getY());
//                    if (el.getX() > 80 && el.getX() < windowWidth - 80 && el.getY() > 80 && el.getY() < windowHeight - 80)
//                        return true;
                    return false;

                case DragEvent.ACTION_DROP:
                    Log.d("JB", "The new location of " + el.getName() + " puts it at around " + event.getX() + " by " + event.getY());

                    // Error, when you drop outside of the main layout, the element disappears.
                    new_x = event.getX();
                    new_y = event.getY();

//                    if (event.getX() + event.getX() * 0.1 > windowWidth && event.getX() - event.getX() / 2 < windowWidth)
//                    {
//                        Log.d("X-axes", "Safe here!");
//                    }

                    el.setX(new_x);
                    el.setY(new_y);
                    el.setSquareX((int)new_x);
                    el.setSquareY((int)new_y);

                    // re-aligns the element rectangle, detecting if any intersections have been made.
                    el.reCalculateCoord();

                    el.setVisibility(View.VISIBLE);

                    // Very untidy, this unreliable check sees to it that an element will not turn into another
                    // compound from the same
                    // if (!(event.getLocalState() instanceof Compound))
                    activity.interactions.updateElementInList(el);

                    return true;
            }
            return true;
        }
    };

    public View.OnTouchListener ElementTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Element el = (Element)v;
            // Log.d("JB", "Screen dimensions ");
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

    /**
     * Work on this to cause compound fission.
     *
     * i.e. break a compound down to its constituent reactants.
     */
    public SensorEventListener SensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
