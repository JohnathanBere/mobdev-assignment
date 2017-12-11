package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.helpers.ElementGroup;

public class FirstQuestion extends BaseActivity {
    Button nextBtn, resetBtn;
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            resetActivityLayout();
            return false;
        }
    };
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
        nextBtn = findViewById(R.id.q1btn);
        resetBtn = findViewById(R.id.q1Reset);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNextActivity(getNextActivity());
                finish();
            }
        });
        resetBtn.setOnLongClickListener(longClickListener);
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

        // Todo store this in a method. Proceed to do the same with the 
        // This loop processes the list of requests that the game will ask of the user.
        for (Compound co : requiredCompounds) {
            int index = requiredCompounds.indexOf(co);
            if (index < requiredCompounds.size()) {
                // If the index is at the second to last item.
                if (index == requiredCompounds.size() - 2) {
                    requirements = requirements + " " + co.getName() + " &";
                }

                // Otherwise should be a separation leading up to the last item
                else if (index < requiredCompounds.size() - 1) {
                    requirements = requirements + " " + co.getName() + ", ";
                }

                // Other wise we can assume we're at the last item
                else {
                    requirements = requirements + " " + co.getName();
                }
            }
        }

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
