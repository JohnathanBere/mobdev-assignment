package johnbere.chemistrydd;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;

import johnbere.chemistrydd.helpers.ElementGroup;
import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.ShakeEventListener;
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
    public ViewGroup content;
    public MediaPlayer pop, buzzer, bang;
    SensorManager sensorManager;
    private ShakeEventListener sensorListener;
    public Vibrator vibr;
    int elementId = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content_main);
        pop = MediaPlayer.create(this, R.raw.deraj_pop);
        buzzer = MediaPlayer.create(this, R.raw.hypocore__buzzer);
        bang = MediaPlayer.create(this, R.raw.cydon_bang);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorListener = new ShakeEventListener();
        vibr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            ArrayList<Compound> splitCompounds = new ArrayList<>();
            /**
             * Figure out a way to gracefully delete compounds from the activity as well as remove it from the interactions class... :)
             */
            @Override
            public void onShake() {
                iterateOverCompounds(interactions.getCompounds());
                // The interactions class should proceed to remove any compound that has been split
                // from its observation
                if (splitCompounds.size() > 0) {
                    // If at least 2 or more compounds were split a bang sound should be made
                    if (splitCompounds.size() >= 2) {
                        bang.start();
                        vibr.vibrate(3000);
                    }
                    // other wise just another pop
                    else if (splitCompounds.size() < 2) {
                        pop.start();
                        vibr.vibrate(500);
                    }

                    deletePrimedCompounds();
                }
                splitCompounds.clear();
            }

            void iterateOverCompounds(ArrayList<Compound> compounds) {
                for (Compound co : compounds) {
                    co.triggerCompoundSplit();
                    if (co.hasCompoundSplit()) {
                        elementsInCompound(co.getElements(), co.getX(), co.getY());
                        content.removeView(co);
                        pop.start();
                        vibr.vibrate(500);
                        splitCompounds.add(co);
                    }
                }
            }

            void elementsInCompound(ArrayList<Element> elements, float x, float y) {
                int margin = getResources().getInteger(R.integer.el_dim);

                for (Element el : elements) {
                    Element element = new Element(MainActivity.this, el.getName(), el.getFormula(), x, y, elementId++, el.getShapeColor(), el.getGroup());
                    int currentIndex = elements.indexOf(el);

                    if (currentIndex > 0) {
                        // Places the element in the array in a place relative to the previous element
                        int x_multiply = currentIndex * element.getSquareW();
                        // setting it right
                        if (x > content.getLeft() + margin && x < content.getWidth() / 2) {
                            element.setX(x + x_multiply);
                            element.setSquareX((int)(x + x_multiply));
                            element.reCalculateCoord();
                        }
                        // or setting it left
                        else if (x < content.getRight() - margin && x > content.getWidth() / 2) {
                            element.setX(x - x_multiply);
                            element.setSquareX((int)(x - x_multiply));
                            element.reCalculateCoord();
                        }
                    }

                    element.setOnTouchListener(new EventListeners(MainActivity.this).ElementTouchListener);
                    content.addView(element);
                    interactions.addElementToList(element);
                }
            }

            // Finds out which compounds were split and should be removed from the interactions compound collection.
            void deletePrimedCompounds() {
                for (Compound decomposedCompound : splitCompounds) {
                    interactions.removeCompoundFromList(decomposedCompound);
                }
            }
        });

        availableElements.add(new Element(this, "Sodium", "Na",100, 1000, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(this, "Chlorine", "Cl",400, 1000, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Bromine", "B",700, 1000, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(this, "Lithium", "Li",850, 900, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }
}
