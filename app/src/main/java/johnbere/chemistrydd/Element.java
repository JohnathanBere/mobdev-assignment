package johnbere.chemistrydd;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import johnbere.chemistrydd.helpers.ShapeShadowBuilder;

/**
 * This class is a game object, that can be dragged and dropped
 * Todo
 * 1. If needed, begin subclassing this view, namely for combinations of availableElements
 * 2. Add other properties like element type, group etc.
 * 3. Proceed to add instances of this class to an ArrayAdapter class (written somewhere else)
 */
public class Element extends View {
    // Set basic information about the element such as its name and formula
    // a lot of properties can be mutated directly, need to make those private and put in place accessors instead.
    private String name, formula;
    private Paint formulaColor, shapeColor, redColor;
    public Rectangle r;
    public Rect rect;
    int elementId;


    public float x = 500, y = 300;

    public boolean handleTouch(View v, MotionEvent event) {
        int action = event.getAction();
        // Element el = (Element)v;

        if (action == MotionEvent.ACTION_DOWN) {
            // ClipData data = ClipData.newPlainText("", "");
            ClipData.Item item = new ClipData.Item(v.getTag().toString());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

            ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);

            // Ensure that the shadow touch point is equal to the positions of the clicks.
            ShapeShadowBuilder shadowBuilder = new ShapeShadowBuilder(v, (int)event.getX(), (int)event.getY());

//            Log.d("JB", "" + data);

            v.startDrag(data, shadowBuilder, v, 0);
            return true;
        }

        return false;
    }

    public void handleDrag(View v, DragEvent event) {
        int action = event.getAction();
        Element el = (Element)event.getLocalState();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("JB", "The element " + el.getName() + " is being dragged");
                Log.d("JB", el.getName() + " tag info: " + el.getTag());
                /**
                 * TODO Log the el axis and with to observe if any changes or coords are detected in any of the events.
                 */
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("JB", "DRAG_ENTERED w/ " + el.getName());
//                int x_coord = (int)event.getX();
//                int y_coord = (int)event.getY();
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("JB", "DRAG_EXITED w/ " + el.getName());

//                this.shapeColor.setColor(Color.DKGRAY);
//                this.x = event.getX();
//                this.y = event.getY();
                break;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d("JB", "DRAG_LOCATION w/ " + el.getName());
//                x_coord = (int)event.getX();
//                y_coord = (int)event.getY();
//                this.x = x_coord;
//                this.y = y_coord;
//                this.r.x = x_coord;
//                this.r.y = y_coord;

                break;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("JB", "DRAG_ENDED w/ " + el.getName());

                break;

            case DragEvent.ACTION_DROP:
                Log.d("JB", "DRAG_DROP w/ " + el.getName());

                break;

            default:
                Log.d("JB", "Program unsure of user intentions");
                break;
        }
    }

    /**
     * Constructor to ensure that whenever this element is instantiated, it has a name and formula
     * @param context
     * @param name
     * @param formula
     */
    public Element(Context context, String name, String formula, float x, float y, int elementId) {
        super(context);
        this.name = name;
        this.formula = formula;
        this.x = x;
        this.y = y;
        this.elementId = elementId;

        formulaColor = new Paint();
        shapeColor = new Paint();
        redColor = new Paint();

        // This will act as an internal stencil for the rect property to begin drawing around the Element bubble.
        r = new Rectangle();
        r.x=(int)x;
        r.y=(int)y;
        r.width=160;
        r.height=160;

        // To ensure proper alignment, the rect begins drawing at each axis take away half of the stencil dimensions.
        rect = new Rect();
        rect.top = (int)this.y - r.height / 2;
        rect.bottom = rect.top + r.height;
        rect.left = (int)this.x - r.width / 2;
        rect.right = rect.left + r.width;
    }

    /**
     * Ensures that a circle is drawn whenever this element is called.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the colors
        redColor.setColor(Color.RED);
        formulaColor.setColor(Color.WHITE);
        shapeColor.setColor(Color.BLACK);
        formulaColor.setTextSize(60);
        formulaColor.setTextAlign(Paint.Align.CENTER);


        canvas.drawCircle(x, y, 80, shapeColor);
        canvas.drawText(this.formula, x, y + 16, formulaColor);
        canvas.drawRect(this.rect, redColor);
    }

    public String getName() {
        return name;
    }

    public int getElementId() {
        return elementId;
    }

    public void reCalculateCoord() {
        // This method will be called when the drag state is acknowledged by the layout, thereby recalculating the internal rectangle
        // of the element.
        this.rect.left = this.r.x - this.r.width / 2;
        this.rect.top = this.r.y - this.r.height / 2;

        this.rect.right = this.rect.left + this.r.width;
        this.rect.bottom = this.rect.top + this.r.height;
    }
}
