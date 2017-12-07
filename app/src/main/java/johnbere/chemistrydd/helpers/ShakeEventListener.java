package johnbere.chemistrydd.helpers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Work on this to cause compound fission.
 *
 * i.e. break a compound down to its constituent reactants.
 */
public class ShakeEventListener implements SensorEventListener {
    // Minimum force to factor in
    private static final int MIN_FORCE = 5;

    // Minimum required number of shakes
    private static final int MIN_DIRECTION_CHANGE = 3;

    // Maximum pause between movements
    private static final int MAX_PAUSE_BETWEEN_DIRECTION_CHANGE = 500;

    // Minimum permitted duration of shakes
    private static final int MIN_TOTAL_DURATION_OF_SHAKE = 2000;

    // Time when first gesture started
    private long mFirstDirectionChangeTime = 0;

    // Time when last movement occurred
    private long mLastDirectionChangeTime;

    // How many movements are factored in
    private int mDirectionChangeCount = 0;

    // Last x position
    private float lastX = 0;

    // Last y position
    private float lastY = 0;

    // Last z position
    private float lastZ = 0;

    private OnShakeListener mShakeListener;

    public interface OnShakeListener {

        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
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
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETWEEN_DIRECTION_CHANGE) {
                //  store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;

                lastX = x;
                lastY = y;
                lastZ = z;

                // evaluate number of movements so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {
                    // check the total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    if (totalDuration >= MIN_TOTAL_DURATION_OF_SHAKE) {
                        mShakeListener.onShake();
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
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
