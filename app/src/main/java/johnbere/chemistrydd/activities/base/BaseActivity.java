package johnbere.chemistrydd.activities.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.R;
import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.Resify;
import johnbere.chemistrydd.helpers.ShakeEventListener;
import johnbere.chemistrydd.helpers.ViewInteractions;

public abstract class BaseActivity extends AppCompatActivity {
    public SensorManager sensorManager;
    public ShakeEventListener sensorListener;

    public Context context;
    public ViewGroup content;
    public MediaPlayer pop, buzzer, bang;
    public Vibrator vibr;
    public ArrayList<Element> availableElements = new ArrayList<>();
    public ArrayList<Compound> availableCompounds = new ArrayList<>();
    public ViewInteractions interactions;
    public Intent intent;
    public Resify resify;
    public int
            elementId = 0,
            list_start_x,
            list_start_y,
            el_margin,
            el_width,
            co_width,
            numberOfAttempts = 0,
            attemptLimits = 0;

    // Abstract methods that will be overridden by the sub-classes
    // Ensuring consistency with the helper classes the require activities of type
    // BaseActivity for proper functionality
    protected abstract int getResourceLayoutId();
    protected abstract int getContentLayoutId();
    protected abstract Context getCurrentContext();
    protected abstract void addElementsToLists();
    protected abstract void pushDataToNextActivity();
    protected abstract void getDataFromPreviousActivity();

    protected void onActivityStart() {
        getDataFromPreviousActivity();
        addElementsToLists();
        elementFactory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceLayoutId());

        content = findViewById(getContentLayoutId());
        context = getCurrentContext();
        resify = new Resify(this);

        list_start_x = resify.intResify(R.integer.list_start_x);
        list_start_y = resify.intResify(R.integer.list_start_y);

        el_margin = resify.intResify(R.integer.el_margin);
        el_width = resify.intResify(R.integer.el_width);
        co_width = resify.intResify(R.integer.co_width);


        pop = MediaPlayer.create(context, R.raw.deraj_pop);
        buzzer = MediaPlayer.create(context, R.raw.hypocore__buzzer);
        bang = MediaPlayer.create(context, R.raw.cydon_bang);
        vibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorListener = new ShakeEventListener();
        sensorListener.setOnShakeListener(new EventListeners(this).OnShakeListener);

        interactions = new ViewInteractions(this);

        // Proceed to add the items to the array and then display on the view.
        onActivityStart();
    }

    protected void moveToNextActivity(BaseActivity nextActivity) {
        // Gets a sub class of the current activity
        BaseActivity currentActivity = (BaseActivity)getCurrentContext();
        // Creates a new intent based on the parameterised desired next
        intent = new Intent(currentActivity, nextActivity.getClass());

        // Overrideable method to put extra things in the form of i.putExtra
        pushDataToNextActivity();
        //


        startActivity(intent);
    }

    protected void elementFactory() {
        // The helper class should now observe the elements
        interactions.setElements(availableElements);
        interactions.setCompounds(availableCompounds);

        // Sets the value of the incrementor to assign a proper elementId to a newly added compound
        elementId = availableElements.size() + availableCompounds.size();
        interactions.setIncr(elementId);

        // Sets the event listener before appending this programmatically to the view.
        // The logic for looping through the data structures may be stored as methods
        // in the base class and then be called.
        if (availableElements.size() > 0) {
            for (Element el : availableElements) {
                el.setOnTouchListener(new EventListeners(this).ElementTouchListener);
                content.addView(el);
            }
        }

        if (availableCompounds.size() > 0) {
            for (Compound co : availableCompounds) {
                co.setOnTouchListener(new EventListeners(this).ElementTouchListener);
                content.addView(co);
            }
        }

        content.setOnDragListener(new EventListeners(this).LayoutDragListener);
    }

    protected int getPositionOffset(int width) {
        return width + el_margin;
    }

    // Run this when you are ready to start a new activity... Finishing the former one.
    // REMEMBER TO KEEP SCORE AND TOTAL TIME
    protected void cleanseInputData() {
        interactions.getElements().clear();
        interactions.getCompounds().clear();
        interactions.setIncr(0);
        elementId = 0;
        availableCompounds.clear();
        availableElements.clear();
    }

    // If the user chooses to reset their activity, reruns the process from scratch again.
    protected void resetActivity() {
        cleanseInputData();
        onActivityStart();
    }

    // Asks if the user is sure about wanting to close the app
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.close_message))
                .setPositiveButton(resify.stringResify(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BaseActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(resify.stringResify(R.string.no), null)
                .show();
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
