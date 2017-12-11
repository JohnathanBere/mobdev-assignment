package johnbere.chemistrydd.activities.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.R;
import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.Game;
import johnbere.chemistrydd.helpers.ShakeEventListener;
import johnbere.chemistrydd.helpers.ViewInteractions;

// use a difficulty setting (Before the first question) that determines the time taken and attempts made
// determining the scores a user gets
public abstract class BaseActivity extends AppCompatActivity {
    public SensorManager sensorManager;
    public ShakeEventListener sensorListener;

    public Context context;
    public ViewGroup content;
    public MediaPlayer pop, buzzer, bang;
    public Vibrator vibr;
    public ArrayList<Element> availableElements = new ArrayList<>();
    public ArrayList<Compound> availableCompounds = new ArrayList<>();
    public ArrayList<Element> requiredElements = new ArrayList<>();
    public ArrayList<Compound> requiredCompounds = new ArrayList<>();
    public ViewInteractions interactions;
    public Intent intent;
    public TextView questionText, attemptsText, scoreText;
    public String attemptsMessage, requirements;
    public Game difficulty;
    public Resources res;
    public CountDownTimer timer;
    long totalTime = 11000;
    public int
            elementId = 0,
            list_start_x,
            list_start_y,
            el_margin,
            el_width,
            co_width,
            totalScore = 0,
            numberOfAttempts = 0,
            attemptLimit = 5;

    // Abstract methods that will be overridden by the sub-classes
    // Ensuring consistency with the helper classes the require activities of type
    // BaseActivity for proper functionality
    protected abstract int getResourceLayoutId();
    protected abstract int getContentLayoutId();
    protected abstract int getTimerText();
    protected abstract int getNumberOfAttempts();
    protected abstract int getScoreText();
    protected abstract int getQuestionText();
    protected abstract Context getCurrentContext();
    public abstract BaseActivity getNextActivity();
    protected abstract void addElementsToLists();
    protected abstract void pushDataToNextActivity();
    protected abstract void getDataFromPreviousActivity();
    protected abstract void viewActivityBindings();
    protected abstract void setRequirements();

    /**
     * Todo:
     * Create an abstract method that sets up the desired compounds / elements
     * Create a method here that evaluates if the current compounds / elements match the desired outcome
     * Also, the result screen should trigger at different times: When time has run out, when the number have attempts
     * have reached max, and when the desired outcome has been achieved. If the former have occurred, a user either gets some
     * points for what they got correct or no points at all.
     *
     * The less attempts a user has had, the higher the amount of points they get
     *
     * Create either another quiz activity or a results page that grades the user performance based on the amount of points.
     */

    protected void onActivityStart() {
        addElementsToLists();
        elementFactory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceLayoutId());

        content = findViewById(getContentLayoutId());
        context = getCurrentContext();
        res = context.getResources();

        list_start_x = res.getInteger(R.integer.list_start_x);
        list_start_y = res.getInteger(R.integer.list_start_y);

        el_margin = res.getInteger(R.integer.el_margin);
        el_width = res.getInteger(R.integer.el_width);
        co_width = res.getInteger(R.integer.co_width);

        pop = MediaPlayer.create(context, R.raw.deraj_pop);
        buzzer = MediaPlayer.create(context, R.raw.hypocore__buzzer);
        bang = MediaPlayer.create(context, R.raw.cydon_bang);
        vibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        questionText = findViewById(getQuestionText());
        attemptsText = findViewById(getNumberOfAttempts());
        scoreText = findViewById(getScoreText());
        requirements = res.getString(R.string.requirements);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorListener = new ShakeEventListener();
        sensorListener.setOnShakeListener(new EventListeners(this).OnShakeListener);

        interactions = new ViewInteractions(this);

        // Gets existing data from a previous intent if there is any
        getDataFromPreviousActivity();

        // Binds the activity to views widgets exclusive to that activity
        viewActivityBindings();

        // Depending on the difficulty, attempts and time are
        difficultySettings();

        // Proceed to add the items to the array and then display on the view.
        onActivityStart();

        // Set the title to be the condition of requirements
        setRequirements();

        // Sets the timer, depending on difficulty however
        setCountdownTimer();

        // Reacts accordingly to player actions such as attempts or time management
        setAttemptsText();
    }

    protected void setAttemptsText() {
        if (attemptsText != null) {
            attemptsMessage = String.format(res.getString(R.string.attempts), numberOfAttempts);
            attemptsText.setText(attemptsMessage);
        }
    }

    public void moveToNextActivity(BaseActivity nextActivity) {
        // Gets a sub class of the current activity
        BaseActivity currentActivity = (BaseActivity)getCurrentContext();
        // Creates a new intent based on the parameterised desired next
        intent = new Intent(currentActivity, nextActivity.getClass());

        // Overrideable method to put extra things in the form of i.putExtra
        pushDataToNextActivity();
        //

        startActivity(intent);
    }

    protected void pushDifficultyData() {
        intent.putExtra("GameDifficulty", difficulty);
    }

    protected void retrieveDifficultyData() {
        difficulty = (Game)getIntent().getSerializableExtra("GameDifficulty");
    }

    protected void difficultySettings() {
        if (difficulty != null) {
            switch (difficulty) {
                case EASY:
                    totalTime = totalTime + (totalTime / 3);
                    attemptLimit = attemptLimit + 3;
                    break;
                case MEDIUM:
                    break;
                case HARD:
                    totalTime = totalTime - (totalTime / 3);
                    attemptLimit = attemptLimit - 3;
                    break;
            }
        }
    }

    protected void setCountdownTimer() {
        timer = new CountDownTimer(totalTime, 1000) {
            TextView timerTxt = (TextView)findViewById(getTimerText());
            @Override
            public void onTick(long time) {
                if (timerTxt != null) {
                    timerTxt.setText(String.format(res.getString(R.string.time_remaining), time / 1000));
                }
            }

            @Override
            public void onFinish() {
                if (timerTxt != null)
                    timerTxt.setText("0");
                // Todo Executes a method that compares user inputs with the expected inputs, giving points
                // based on attempts and time left
            }
        }.start();
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

    // Guarantees the distance of separation between elements/compounds
    protected int getPositionOffset(int width) {
        return width + el_margin;
    }

    // Run this when you are ready to start a new activity... Finishing the former one.
    // REMEMBER TO KEEP SCORE AND TOTAL TIME
    protected void cleanseInputData() {
        if (interactions.getElements().size() > 0) {
            for (Element el : interactions.getElements()) {
                content.removeView(el);
            }
        }

        if (interactions.getCompounds().size() > 0) {
            for (Compound co : interactions.getCompounds()) {
                content.removeView(co);
            }
        }

        // Even public accessors are mutable,
        // Remember that this might not be very good practice at all.
        // As this defeats the purpose of having accessors in the first place
        // If possible, set the properties to be immutable (or final) and just create copies
        interactions.getElements().clear();
        interactions.getCompounds().clear();
        interactions.setIncr(0);
        elementId = 0;

        availableCompounds.clear();
        availableElements.clear();
    }

    // If the user chooses to reset their activity, reruns the process from scratch again.
    protected void resetActivityLayout() {
        cleanseInputData();
        onActivityStart();
    }

    // Asks if the user is sure about wanting to close the app
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(res.getString(R.string.close_message))
                .setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BaseActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(res.getString(R.string.no), null)
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
