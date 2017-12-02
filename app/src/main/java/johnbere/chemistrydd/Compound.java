package johnbere.chemistrydd;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class Compound extends Element {
    ArrayList<Element> elements;

    public Compound(Context context, String name, String formula, float x, float y, int elementId, int color, ArrayList<Element> elements) {
        super(context, name, formula, x, y, elementId, color);
        this.elements = elements;

        String form = "";
        for (Element el : elements) {
            form = form + el.getFormula();
        }

        this.formula = form;
    }
}
