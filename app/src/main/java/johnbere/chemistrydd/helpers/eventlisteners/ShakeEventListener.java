package johnbere.chemistrydd.helpers.eventlisteners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;

/**
 * Work on this to cause compound fission.
 *
 * i.e. break a compound down to its constituent reactants.
 */
public class ShakeEventListener implements SensorEventListener {
    // Minimum force to factor in
    private static final int MIN_FORCE = 5;

    // Minimum required number of shakes
    private static final int MIN_DIRECTION_CHANGE = 2;

    // Maximum pause between movements
    private static final int MAX_PAUSE_BETWEEN_DIRECTION_CHANGE = 500;

    // Minimum permitted duration of shakes
    private static final int MIN_TOTAL_DURATION_OF_SHAKE = 2000;

    // Time when first gesture started
    private long initialGestureChangeTime = 0;

    // Time when last movement occurred
    private long recentGestureChangeTime;

    // How many movements are factored in
    private int directionChangeCounter = 0;

    // Last x position
    private float lastX = 0;

    // Last y position
    private float lastY = 0;

    // Last z position
    private float lastZ = 0;

    private OnShakeListener onShakeListener;

    public interface OnShakeListener {

        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[SensorManager.DATA_X];
        float y = event.values[SensorManager.DATA_Y];
        float z = event.values[SensorManager.DATA_Z];

        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        // Calculate movement
        if (totalMovement > MIN_FORCE) {
            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (initialGestureChangeTime == 0) {
                initialGestureChangeTime = now;
                recentGestureChangeTime = now;
            }

            long lastChangeWasAgo = now - recentGestureChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETWEEN_DIRECTION_CHANGE) {
                //  store movement data
                recentGestureChangeTime = now;
                directionChangeCounter++;

                lastX = x;
                lastY = y;
                lastZ = z;

                // evaluate number of movements so far
                if (directionChangeCounter >= MIN_DIRECTION_CHANGE) {
                    // check the total duration
                    long totalDuration = now - initialGestureChangeTime;
                    if (totalDuration >= MIN_TOTAL_DURATION_OF_SHAKE) {
                        onShakeListener.onShake();
                        resetShakeParameters();
                    }
                }
            } else {
                resetShakeParameters();
            }
        }
    }

    // Resets the shake parameters to their default values
    private void resetShakeParameters() {
        initialGestureChangeTime = 0;
        directionChangeCounter = 0;
        recentGestureChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
