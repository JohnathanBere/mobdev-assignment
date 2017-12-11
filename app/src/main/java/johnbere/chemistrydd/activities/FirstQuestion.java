package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.helpers.ElementGroup;

public class FirstQuestion extends BaseActivity {
    // Button nextBtn, resetBtn;
    ImageButton contBtn, undoBtn, infoBtn;
    boolean isPaused;
    protected void pushDataToNextActivity() {
        // int val = 123
        // intent.putExtra("numbah", val)
        pushDifficultyData();
    }

    @Override
    protected void getDataFromPreviousActivity() {
        // Bundle extras = getIntent().getExtras()
        // if (extras != null) { int val = extras.getInt("numbah") }
        retrieveDifficultyData();
        // Toast.makeText(context, difficulty.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void viewActivityBindings() {
        contBtn = findViewById(R.id.contBtnQ1);
        undoBtn = findViewById(R.id.undoBtnQ1);
        infoBtn = findViewById(R.id.infoBtnQ1);

        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(getNextActivity());
                finish();
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionText.setText(requirements);
            }
        });
        infoBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                moveToNextActivity(new GuideLines());
                return false;
            }
        });
        undoBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                resetActivityLayout();
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // If we are navigating away to the guidelines page, pause the timer
        timer.cancel();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Resume the timer when we return to the question
        if (isPaused) {
            setCountdownTimer();
            isPaused = false;
        }
    }

    @Override
    protected void addElementsToLists() {
        availableElements.add(new Element(context, "Sodium", "Na",list_start_x, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(context, "Chlorine", "Cl",list_start_x + getPositionOffset(el_width), list_start_y, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(context, "Bromine", "B",list_start_x + (getPositionOffset(el_width)) * 2, list_start_y, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(context, "Lithium", "Li",list_start_x + (getPositionOffset(el_width))* 3, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
    }

    @Override
    protected void setRequirements() {
        requiredCompounds.add(new Compound(context, "Sodium Chloride", "NaCl", 0, 0, 11, 0, null));
        requiredCompounds.add(new Compound(context, "Lithium Bromide", "LiB", 0, 0, 12, 0, null));
        requirements = res.getString(R.string.requirements);

        // Process the requested substances
        textMessageProcessor();

        questionText.setText(requirements);
        questionText.bringToFront();
    }

    @Override
    public BaseActivity getNextActivity() {
        return new SecondQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getTimerText() {
        return R.id.q1Timer;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_first_question;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.first_question;
    }

    @Override
    protected Context getCurrentContext() {
        return FirstQuestion.this;
    }

    @Override
    protected int getNumberOfAttempts() {
        return R.id.q1Attempts;
    }

    @Override
    protected int getScoreText() {
        return 0;
    }

    @Override
    protected int getQuestionText() {
        return R.id.q1Txt;
    }
}
