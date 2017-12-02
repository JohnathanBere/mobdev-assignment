package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


/**
 * This view will have many other availableElements. Another class will handle the combination of availableElements to form new compounds.
 */
public class GameView extends SurfaceView implements Runnable {
    // Properties relevant to what was derived from base class
    Thread thread;
    SurfaceHolder holder;
    boolean isRunning = true;
    Paint screenColor = new Paint();

    // Figure out a way to add the element views to this surface view. :)
    ArrayList<Element> availableElements;

    public GameView(Context context) {
        super(context);

        screenColor.setColor(Color.WHITE);
        holder = getHolder();
        thread = new Thread(this);
        thread.start();

//        availableElements = new ArrayList<Element>();
//        availableElements.add(new Element(context,"Sodium", "Na", 500, 300));
//        availableElements.add(new Element(context, "Chlorine", "Cl", 520, 300));
    }

    @Override
    public void run() {
        while(isRunning) {
            if (!holder.getSurface().isValid())
                continue;

            Canvas canvas = holder.lockCanvas();
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), screenColor);

            for (Element el : availableElements) {
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
