package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


/**
 * This view will have many other elements. Another class will handle the combination of elements to form new compounds.
 */
public class GameView extends SurfaceView {
    Thread thread;
    SurfaceHolder holder;

    Element el;

    public GameView(Context context) {
        super(context);
    }
}
