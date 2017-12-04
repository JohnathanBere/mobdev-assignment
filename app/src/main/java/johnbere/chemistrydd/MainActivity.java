package johnbere.chemistrydd;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import java.util.ArrayList;
import johnbere.chemistrydd.helpers.ElementGroup;
import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.ViewInteractions;

/**
 * Todo
 * Find a way to decompose compounds to their constituent elements.
 *
 * Implement sensor listener that shakes the element
 *
 * Put in place a class that iterates over the compound and element arraylist datastructures.
 */
public class MainActivity extends AppCompatActivity {
    ArrayList<Element> availableElements = new ArrayList<>();
    ArrayList<Compound> availableCompounds = new ArrayList<>();
    public ViewInteractions interactions = new ViewInteractions(this);
    ViewGroup content;
    public MediaPlayer player;
    SensorManager sensorManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content_main);
        player = MediaPlayer.create(MainActivity.this, R.raw.deraj_pop);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        availableElements.add(new Element(MainActivity.this, "Sodium", "Na",100, 1000, 1, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(MainActivity.this, "Chlorine", "Cl",400, 1000, 2, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(MainActivity.this, "Bromine", "B",700, 1000, 3, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(MainActivity.this, "Lithium", "Li",850, 900, 4, Color.LTGRAY, ElementGroup.ALKALIMETALS));

        // The helper class should now observe the elements
        interactions.setElements(availableElements);
        interactions.setIncr(availableElements.size());

        // Log.d("JB", "interactions has " + interactions.getElements().size() + " elements in it on the MainActivity");

        // Sets the event listener before appending this programmatically to the view.
        for (Element el : availableElements) {
            el.setTag(el.getName());
            el.setOnTouchListener(new EventListeners(MainActivity.this).ElementTouchListener);
            content.addView(el);
        }

        for (Compound co : availableCompounds) {
            co.setOnTouchListener(new EventListeners(MainActivity.this).ElementTouchListener);
            content.addView(co);
        }
        content.setOnDragListener(new EventListeners(this).LayoutDragListener);
        // sensorManager.registerListener(new EventListeners(this).SensorEventListener);
    }
}
