package johnbere.chemistrydd;

import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public final class ElementEventBindings {
    private static RelativeLayout.LayoutParams params;
    private static float x = 300, y = 500, prevX, prevY;

    /**
     * Event listener to move the element along the screen.
     */
    static View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // Gets current coordinates
            x = event.getX();
            y = event.getY();

            if (event.getAction() == event.ACTION_DOWN) {
                prevX = x;
                prevY = y;
            }

            view.invalidate();
            return true;
        }
    };

    static View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            params = (RelativeLayout.LayoutParams)view.getLayoutParams();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    x = (int) event.getX();
                    y = (int) event.getY();
                    params.leftMargin = x;
                    params.topMargin = y;
                    view.setLayoutParams(params);
                    break;

                default:
                    break;
            }
            return true;
        }
    };
}
