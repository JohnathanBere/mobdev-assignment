package johnbere.chemistrydd;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Ensures multiple touch event listeners is possible
 */
public class CompositeTouchListener implements View.OnTouchListener {
    List<View.OnTouchListener> touchListeners;
    public CompositeTouchListener() {
        touchListeners = new ArrayList<View.OnTouchListener>();
    }

    public void addTouchListener (View.OnTouchListener touchListener) {
        touchListeners.add(touchListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        for (View.OnTouchListener listener: touchListeners) {
            listener.onTouch(v, event);
        }
        return false;
    }
}
