package johnbere.chemistrydd.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;

public class GuideLines extends BaseActivity {
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_guide_lines;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.content_guidelines;
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
        return GuideLines.this;
    }

    @Override
    public BaseActivity getNextActivity() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void addElementsToLists() {}

    @Override
    protected void pushDataToNextActivity() {}

    @Override
    protected void getDataFromPreviousActivity() {}

    @Override
    protected void viewActivityBindings() {}

    @Override
    protected void setRequirements() {}

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
    public void beginCountdown() {}
}
