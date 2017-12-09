package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

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
    protected void pushDataToNextActivity() {
        pushDifficultyData();
    }

    @Override
    protected void getDataFromPreviousActivity() {
        retrieveDifficultyData();
    }

    @Override
    protected void viewActivityBindings() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected Context getCurrentContext() {
        return SecondQuestion.this;
    }

    @Override
    protected int getAttemptLimitText() {
        return 0;
    }

    @Override
    protected int getScoreText() {
        return 0;
    }
}
