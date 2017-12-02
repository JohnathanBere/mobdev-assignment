package johnbere.chemistrydd;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class is a game object, that can be dragged and dropped
 * Todo
 * 1. If needed, begin subclassing this view, namely for combinations of availableElements
 * 2. Add other properties like element type, group etc.
 * 3. Proceed to add instances of this class to an ArrayAdapter class (written somewhere else)
 */
public class Element extends View {
    // Set basic information about the element such as its name and formula
    private String name;
    private String formula;
    private Paint formulaColor;
    private Paint shapeColor;
    Rectangle r;


    float x = 500, y = 300, prevX, prevY;

    /**
     * Event listener to move the element along the screen.
     */
    OnTouchListener touch = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // Gets current coordinates
            x = motionEvent.getX();
            y = motionEvent.getY();

            if (motionEvent.getAction() == motionEvent.ACTION_DOWN) {
                prevX = x;
                prevY = y;
            }

            view.invalidate();
            return true;
        }
    };

    public boolean handleTouch(View v, MotionEvent event) {
        int action = event.getAction();
        Element el = (Element)v;

        if (action == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");

            // Ensure that the shadow touch point is equal to the positions of the clicks.
            ElementShadowBuilder shadowBuilder = new ElementShadowBuilder(el, (int)event.getX(), (int)event.getY());


            el.startDrag(data, shadowBuilder, el, 0);
            return true;
        }

        return false;
    }

    public void handleDrag(View v, DragEvent event) {
        int action = event.getAction();
        Element el = (Element)event.getLocalState();
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)v.getLayoutParams();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("JB", "The element " + el.getName() + " is being dragged");
                /**
                 * TODO Log the el axis and with to observe if any changes or coords are detected in any of the events.
                 */
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("JB", "DRAG_ENTERED w/ " + el.getName());
                int x_coord = (int)event.getX();
                int y_coord = (int)event.getY();
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("JB", "DRAG_EXITED w/ " + el.getName());

                this.shapeColor.setColor(Color.DKGRAY);
                this.x = event.getX();
                this.y = event.getY();
                break;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d("JB", "DRAG_LOCATION w/ " + el.getName());
                x_coord = (int)event.getX();
                y_coord = (int)event.getY();

                break;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("JB", "DRAG_ENDED w/ " + el.getName());

                break;

            case DragEvent.ACTION_DROP:
                Log.d("JB", "DRAG_DROP");

                el.setVisibility(VISIBLE);
                el.setX(event.getX());
                el.setY(event.getY());
                break;

            default:
                break;
        }
    }

    /**
     * Constructor to ensure that whenever this element is instantiated, it has a name and formula
     * @param context
     * @param name
     * @param formula
     */
    public Element(Context context, String name, String formula, float x, float y) {
        super(context);
        this.name = name;
        this.formula = formula;
        this.x = x;
        this.y = y;

        formulaColor = new Paint();
        shapeColor = new Paint();

        r = new Rectangle();
        r.x=(int)x;
        r.y=(int)y;
        r.width=160;
        r.height=160;
    }

    /**
     * Ensures that a circle is drawn whenever this element is called.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the colors

        formulaColor.setColor(Color.WHITE);
        shapeColor.setColor(Color.BLACK);
        formulaColor.setTextSize(60);
        formulaColor.setTextAlign(Paint.Align.CENTER);

        canvas.drawCircle(x, y, 80, shapeColor);
        canvas.drawText(this.formula, x, y + 16, formulaColor);
    }

    public String getName() {
        return name;
    }
}

class ElementShadowBuilder extends View.DragShadowBuilder {
    int x;
    int y;

    public ElementShadowBuilder(View view, int x, int y) {
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
