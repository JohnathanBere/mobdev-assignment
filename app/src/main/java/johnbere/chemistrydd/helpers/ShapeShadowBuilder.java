package johnbere.chemistrydd.helpers;

import android.graphics.Point;
import android.view.View;

// Existing shadow builder offsets unevenly when a shape is touch
// By including the event coordinates of the touched shape in the constructor of this class
// this will ensure that the touch points of instances of this class are the exact same as what was touched.
public class ShapeShadowBuilder extends View.DragShadowBuilder {
    int x;
    int y;

    public ShapeShadowBuilder(View view, int x, int y) {
        super(view);
        this.x = x;
        this.y = y;
    }

    @Override
    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
        super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint);
        outShadowTouchPoint.set(x, y);
    }
}
