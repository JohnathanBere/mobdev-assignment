package johnbere.chemistrydd.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.R;

public class MainActivity extends BaseActivity {
    Button startBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(new FirstQuestionActivity());
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
    protected Context getCurrentContext() {
        return MainActivity.this;
    }

    @Override
    protected void addElementsToLists() {}
}
