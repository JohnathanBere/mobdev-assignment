package johnbere.chemistrydd.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;

public class FinalScreen extends BaseActivity {

    @Override
    protected void viewActivityBindings() {

    }

    @Override
    protected void setRequirements() {

    }

    @Override
    protected int setContBtn() {
        return 0;
    }

    @Override
    protected int setInfoBtn() {
        return 0;
    }

    @Override
    protected int setUndoBtn() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure the timer does not begin running, an exception will form other wise...
        timer.cancel();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_final_screen;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.content_final_screen;
    }

    @Override
    protected int getTimerText() {
        return 0;
    }

    @Override
    protected int getNumberOfAttempts() {
        return 0;
    }

    @Override
    protected int getScoreText() {
        return 0;
    }

    @Override
    protected int getQuestionText() {
        return 0;
    }

    @Override
    protected Context getCurrentContext() {
        return FinalScreen.this;
    }

    @Override
    public BaseActivity getNextActivity() {
        return null;
    }

    @Override
    protected void addElementsToLists() {

    }

    @Override
    protected void pushDataToNextActivity() {

    }

    @Override
    protected void getDataFromPreviousActivity() {

    }

    @Override
    public void beginCountdown() {}
}
