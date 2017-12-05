package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
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
    public MediaPlayer pop, buzzer;
    SensorManager sensorManager;
    Sensor accelerometer;
    public Vibrator vibr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content_main);
        pop = MediaPlayer.create(this, R.raw.deraj_pop);
        buzzer = MediaPlayer.create(this, R.raw.hypocore__buzzer);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        availableElements.add(new Element(this, "Sodium", "Na",100, 1000, 1, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(this, "Chlorine", "Cl",400, 1000, 2, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Bromine", "B",700, 1000, 3, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Lithium", "Li",850, 900, 4, Color.LTGRAY, ElementGroup.ALKALIMETALS));

        // The helper class should now observe the elements
        interactions.setElements(availableElements);
        interactions.setIncr(availableElements.size());

        // Sets the event listener before appending this programmatically to the view.
        for (Element el : availableElements) {
            el.setTag(el.getName());
            el.setOnTouchListener(new EventListeners(this).ElementTouchListener);
            content.addView(el);
        }

        for (Compound co : availableCompounds) {
            co.setOnTouchListener(new EventListeners(this).ElementTouchListener);
            content.addView(co);
        }
        content.setOnDragListener(new EventListeners(this).LayoutDragListener);
        sensorManager.registerListener(new EventListeners(this).SensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
