package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class is a game object, that can be dragged and dropped
 * Todo
 * 1. If needed, begin subclassing this view, namely for combinations of elements
 * 2. Add other properties like element type, group etc.
 * 3. Proceed to add instances of this class to an ArrayAdapter class (written somewhere else)
 */
public class Element extends View {
    // Set basic information about the element such as its name and formula
    // Shape should be a circle
    private String name;
    private String formula;
    private Paint formulaColor;
    private Paint shapeColor;

    float x = 500, y = 300, prevX, prevY;

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

    public Element(Context context, String name, String formula) {
        super(context);
        this.name = name;
        this.formula = formula;

        formulaColor = new Paint();
        shapeColor = new Paint();

        setOnTouchListener(touch);
    }

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

}
