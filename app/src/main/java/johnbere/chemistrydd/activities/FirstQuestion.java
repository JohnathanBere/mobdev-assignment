package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.helpers.enums.ElementGroup;

public class FirstQuestion extends BaseActivity {
    protected void pushDataToNextActivity() {
        pushDifficultyAndScoreData();
    }

    @Override
    protected void getDataFromPreviousActivity() {
        retrieveDifficultyAndScoreData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onNavigateReturn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onNavigateAway();
    }

    @Override
    protected void viewActivityBindings() {
        contBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResults();
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
                return true;
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
    protected void addElementsToLists() {
        availableElements.add(new Element(context, "Sodium", "Na",list_start_x, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(context, "Chlorine", "Cl",list_start_x + getPositionOffset(el_width), list_start_y, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(context, "Bromine", "B",list_start_x + (getPositionOffset(el_width)) * 2, list_start_y, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(context, "Lithium", "Li",list_start_x + (getPositionOffset(el_width))* 3, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
    }

    @Override
    protected void setRequirements() {
        ArrayList<Element> naCl = new ArrayList<>();
        naCl.add(new Element(context, "Sodium", "Na", 0, 0, 18, 0, ElementGroup.ALKALIMETALS));
        naCl.add(new Element(context, "Chlorine", "Cl", 0, 0, 19, 0, ElementGroup.HALOGENS));

        ArrayList<Element> liB = new ArrayList<>();
        liB.add(new Element(context, "Lithium", "Li", 0,0, 22, 0, ElementGroup.ALKALIMETALS));
        liB.add(new Element(context, "Bromine", "B", 0,0, 23, 0, ElementGroup.HALOGENS));

        requiredCompounds.add(new Compound(context, "Sodium Chloride", "NaCl", 0, 0, 11, 0, naCl));
        requiredCompounds.add(new Compound(context, "Lithium Bromide", "LiB", 0, 0, 12, 0, liB));

        requirements = res.getString(R.string.requirements);

        // Process the requested substances
        textMessageProcessor();

        questionText.setText(requirements);
        questionText.bringToFront();
    }

    @Override
    public void beginCountdown() {
        startCountdownTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BaseActivity getNextActivity() {
        return new SecondQuestion();
    }

    @Override
    protected int setContBtn() {
        return R.id.contBtnQ1;
    }

    @Override
    protected int setInfoBtn() {
        return R.id.infoBtnQ1;
    }

    @Override
    protected int setUndoBtn() {
        return R.id.undoBtnQ1;
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
        return R.id.q1Score;
    }

    @Override
    protected int getQuestionText() {
        return R.id.q1Txt;
    }
}
