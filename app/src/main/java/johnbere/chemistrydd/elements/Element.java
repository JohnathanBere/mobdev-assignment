package johnbere.chemistrydd.elements;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.view.MotionEvent;
import android.view.View;

import johnbere.chemistrydd.helpers.*;
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
    protected String name, formula;
    private Paint formulaColor, shapeColor, rectColor;
    private Rectangle r;
    private Rect rect;
    private int elementId, color, shapeRadius;
    private float x, y;
    private ElementGroup group;

    public boolean handleTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                // Ensure that the shadow touch point is equal to the positions of the clicks.
                ShapeShadowBuilder shadowBuilder = new ShapeShadowBuilder(v, (int)this.getX(), (int)this.getY());
                v.startDrag(data, shadowBuilder, v, 0);
                return true;

            default:
                return false;
        }
    }

    /**
     * Constructor to ensure that whenever this element is instantiated, it has a name and formula
     */
    public Element(Context context, String name, String formula, float x, float y, int elementId, int color, ElementGroup group) {
        super(context);
        this.name = name;
        this.formula = formula;
        this.x = x;
        this.y = y;
        this.elementId = elementId;
        this.color = color;
        this.group = group;
        this.shapeRadius = 80;

        formulaColor = new Paint();
        shapeColor = new Paint();
        rectColor = new Paint();

        // This will act as an internal stencil for the rect property to begin drawing around the Element bubble.
        r = new Rectangle();
        r.x= (int)x;
        r.y= (int)y;
        r.width= this.shapeRadius * 2;
        r.height= this.shapeRadius * 2;

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
        rectColor.setColor(Color.RED);
        formulaColor.setColor(Color.WHITE);
        shapeColor.setColor(color);
        formulaColor.setTextSize(60);
        formulaColor.setTextAlign(Paint.Align.CENTER);

        canvas.drawCircle(x, y, this.shapeRadius, shapeColor);
        canvas.drawText(this.formula, x, y + 16, formulaColor);
        canvas.drawRect(this.rect, rectColor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getSquareY() {
        return this.r.y;
    }

    public void setSquareY(int y) {
        this.r.y = y;
    }

    public int getSquareX() {
        return this.r.x;
    }

    public void setSquareX(int x) {
        this.r.x = x;
    }

    public int getSquareH() {
        return this.r.height;
    }

    public void setSquareH(int h) {
        this.r.height = h;
    }

    public int getSquareW() {
        return this.r.width;
    }

    public void setSquareW(int w) {
        this.r.width = w;
    }

    public Rect getRect() {
        return this.rect;
    }

    public int getShapeRadius() {
        return this.shapeRadius;
    }

    public void setShapeRadius(int radius) {
        this.shapeRadius = radius;
    }

    public void setShapeColor (int shapeColor) {
        this.color = shapeColor;
    }

    public int getShapeColor() {
        return this.color;
    }

    public ElementGroup getGroup() {
        return group;
    }

    public void setGroup(ElementGroup group) {
        this.group = group;
    }
}
