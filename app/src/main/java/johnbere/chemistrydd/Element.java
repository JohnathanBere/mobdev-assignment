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
    String name, formula;
    private Paint formulaColor, shapeColor, rectColor;
    public Rectangle r;
    public Rect rect;
    int elementId, color;
    public float x, y;

    public boolean handleTouch(View v, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");

            // Ensure that the shadow touch point is equal to the positions of the clicks.
            ShapeShadowBuilder shadowBuilder = new ShapeShadowBuilder(v, (int)event.getX(), (int)event.getY());

//            Log.d("JB", "" + data);

            v.startDrag(data, shadowBuilder, v, 0);
            return true;
        }
        return false;
    }

    /**
     * Constructor to ensure that whenever this element is instantiated, it has a name and formula
     */
    public Element(Context context, String name, String formula, float x, float y, int elementId, int color) {
        super(context);
        this.name = name;
        this.formula = formula;
        this.x = x;
        this.y = y;
        this.elementId = elementId;
        this.color = color;

        formulaColor = new Paint();
        shapeColor = new Paint();
        rectColor = new Paint();

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

    public void reCalculateCoord() {
        // This method will be called when the drag state is acknowledged by the layout, thereby recalculating the internal rectangle
        // of the element.
        this.rect.left = this.r.x - this.r.width / 2;
        this.rect.top = this.r.y - this.r.height / 2;

        this.rect.right = this.rect.left + this.r.width;
        this.rect.bottom = this.rect.top + this.r.height;
    }

    /**
     * Ensures that a circle is drawn whenever this element is called.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the colors
        rectColor.setColor(Color.TRANSPARENT);
        formulaColor.setColor(Color.WHITE);
        shapeColor.setColor(color);
        formulaColor.setTextSize(60);
        formulaColor.setTextAlign(Paint.Align.CENTER);


        canvas.drawCircle(x, y, 80, shapeColor);
        canvas.drawText(this.formula, x, y + 16, formulaColor);
        canvas.drawRect(this.rect, rectColor);
    }

    public String getName() {
        return name;
    }

    public int getElementId() {
        return elementId;
    }

    public String getFormula() {
        return formula;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }
}
