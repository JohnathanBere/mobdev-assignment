package johnbere.chemistrydd.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.R;

public class MainActivity extends BaseActivity {
    Button startBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void viewActivityBindings() {
        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(new Difficulty());
                finish();
            }
        });
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
    protected int getAttemptLimitText() {
        return 0;
    }

    @Override
    protected int getScoreText() {
        return 0;
    }
}
