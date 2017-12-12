package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.helpers.ElementGroup;

public class SecondQuestion extends BaseActivity {
    @Override
    protected void addElementsToLists() {
        availableElements.add(new Element(context, "Potassium", "K",list_start_x, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(context, "Iodine", "I",list_start_x + getPositionOffset(el_width), list_start_y, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(context, "Astatine", "At",list_start_x + (getPositionOffset(el_width)) * 2, list_start_y, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(context, "Rubidium", "Rb",list_start_x + (getPositionOffset(el_width))* 3, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startCountdownTimer();
    }

    @Override
    protected void setRequirements() {}

    @Override
    protected void pushDataToNextActivity() {
        pushDifficultyData();
    }

    @Override
    protected void getDataFromPreviousActivity() {
        retrieveDifficultyData();
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
    public void beginCountdown() {
        startCountdownTimer();
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
    protected int setContBtn() {
        return R.id.contBtnQ2;
    }

    @Override
    protected int setInfoBtn() {
        return R.id.infoBtnQ2;
    }

    @Override
    protected int setUndoBtn() {
        return R.id.undoBtnQ2;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_second_question;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.second_question;
    }

    @Override
    protected int getTimerText() {
        return R.id.q2Timer;
    }

    @Override
    protected int getScoreText() {
        return R.id.q2Score;
    }

    @Override
    protected int getQuestionText() {
        return R.id.q2Txt;
    }

    @Override
    protected Context getCurrentContext() {
        return SecondQuestion.this;
    }

    @Override
    protected int getNumberOfAttempts() {
        return R.id.q2Attempts;
    }

    @Override
    public BaseActivity getNextActivity() {
        return new FinalScreen();
    }
}
