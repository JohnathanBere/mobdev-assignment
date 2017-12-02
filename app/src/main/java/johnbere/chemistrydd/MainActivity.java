package johnbere.chemistrydd;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.ViewInteractions;

public class MainActivity extends AppCompatActivity {
    ArrayList<Element> availableElements = new ArrayList<>();
    public ViewInteractions interactions = new ViewInteractions(this);
    ViewGroup content;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = (ViewGroup) findViewById(R.id.content_main);


        availableElements.add(new Element(MainActivity.this, "Sodium", "Na",100, 1000, 1, Color.LTGRAY));
        availableElements.add(new Element(MainActivity.this, "Chlorine", "Cl",400, 1000, 2, Color.BLUE));
        availableElements.add(new Element(MainActivity.this, "Bromine", "B",700, 1000, 3, Color.BLACK));

        // The helper class should now observe the elements
        interactions.setElements(availableElements);

        // Log.d("JB", "interactions has " + interactions.getElements().size() + " elements in it on the MainActivity");

        // Sets the event listener before appending this programmatically to the view.
        for (Element el: availableElements) {
            el.setTag(el.getName());
            el.setOnTouchListener(new EventListeners(MainActivity.this).ElementTouchListener);
            content.addView(el);
        }
        content.setOnDragListener(new EventListeners(this).LayoutDragListener);
    }
}
