package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Compound;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.helpers.ElementGroup;

public class SecondQuestion extends BaseActivity {
    @Override
    protected void addElementsToLists() {
        ArrayList<Element> h2O = new ArrayList<>();
        h2O.add(new Element(context, "Hydrogen", "H", 0, 0, 20, Color.DKGRAY, ElementGroup.HYDROGEN));
        h2O.add(new Element(context, "Hydrogen", "H", 0, 0, 21, Color.DKGRAY, ElementGroup.HYDROGEN));
        h2O.add(new Element(context, "Oxygen", "O", 0, 0, 22, Color.RED, ElementGroup.CHALCOGENS));

        ArrayList<Element> liCl = new ArrayList<>();
        liCl.add(new Element(context, "Lithium", "Li", 0, 0, 23, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        liCl.add(new Element(context, "Chlorine", "Cl", 0, 0, 24, Color.BLUE, ElementGroup.HALOGENS));


        availableCompounds.add(new Compound(context, "Water", "H2O", list_start_x, list_start_y, elementId++, Color.GREEN, h2O));
        availableCompounds.add(new Compound(context, "Lithium Chloride", "LiCl", list_start_x + getPositionOffset(co_width), list_start_y, elementId++, Color.GREEN, liCl));
    }

    // Todo 12/12 Be rigorous regarding substance requirements.
    @Override
    protected void setRequirements() {
        requiredElements.add(new Element(context, "Hydrogen", "H", 0, 0, 34, 0, ElementGroup.HYDROGEN));
        requiredElements.add(new Element(context, "Hydrogen", "H", 0, 0, 35, 0, ElementGroup.HYDROGEN));
        requiredElements.add(new Element(context, "Oxygen", "O", 0, 0, 36, 0, ElementGroup.CHALCOGENS));
        requiredElements.add(new Element(context, "Lithium", "Li", 0, 0, 37, 0, ElementGroup.ALKALIMETALS));
        requiredElements.add(new Element(context, "Chlorine", "Cl", 0, 0, 38, 0, ElementGroup.HALOGENS));

        requirements = res.getString(R.string.requirements);

        // Process the requested substances
        textMessageProcessor();

        questionText.setText(requirements);
        questionText.bringToFront();
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
