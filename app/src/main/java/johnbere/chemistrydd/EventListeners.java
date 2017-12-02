package johnbere.chemistrydd;

import android.content.ClipData;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * A separate class that handles the various event listeners in the app.
 */
public class EventListeners {
    static class ElementTouchListener implements View.OnTouchListener {
    //static View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        }
    }
    static class ElementDragListener implements  View.OnDragListener {
    //static View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int action = event.getAction();
            return false;
        }
    };
}
