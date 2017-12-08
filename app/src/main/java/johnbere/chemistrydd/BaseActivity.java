package johnbere.chemistrydd;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.ShakeEventListener;
import johnbere.chemistrydd.helpers.ViewInteractions;

public abstract class BaseActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private ShakeEventListener sensorListener;

    public Context context;
    public ViewGroup content;
    public MediaPlayer pop, buzzer, bang;
    public Vibrator vibr;
    public ArrayList<Element> availableElements = new ArrayList<>();
    public ArrayList<Compound> availableCompounds = new ArrayList<>();
    public ViewInteractions interactions = new ViewInteractions(this);
    public int elementId = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceLayoutId());
        content = findViewById(getContentLayoutId());
        context = getCurrentContext();

        pop = MediaPlayer.create(context, R.raw.deraj_pop);
        buzzer = MediaPlayer.create(context, R.raw.hypocore__buzzer);
        bang = MediaPlayer.create(context, R.raw.cydon_bang);
        vibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorListener = new ShakeEventListener();
        sensorListener.setOnShakeListener(new EventListeners(this).OnShakeListener);
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

    protected abstract int getResourceLayoutId();
    protected abstract int getContentLayoutId();
    protected abstract Context getCurrentContext();
}
