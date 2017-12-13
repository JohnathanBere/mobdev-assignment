package johnbere.chemistrydd.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.R;

public class MainActivity extends BaseActivity {
    Button startBtn, guideBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void viewActivityBindings() {
        startBtn = findViewById(R.id.startBtn);
        guideBtn = findViewById(R.id.guide);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(getNextActivity());
                finish();
            }
        });

        guideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(new GuideLines());
            }
        });
    }

    @Override
    public BaseActivity getNextActivity() {
        return new Difficulty();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.content_main;
    }

    @Override
    protected int getTimerText() {
        return 0;
    }

    @Override
    protected Context getCurrentContext() {
        return MainActivity.this;
    }

    @Override
    protected void addElementsToLists() {}

    @Override
    protected void pushDataToNextActivity() {}

    @Override
    protected void getDataFromPreviousActivity() {}

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
