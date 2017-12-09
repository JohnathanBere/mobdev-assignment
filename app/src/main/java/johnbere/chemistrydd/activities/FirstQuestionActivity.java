package johnbere.chemistrydd.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.elements.Element;
import johnbere.chemistrydd.helpers.ElementGroup;
import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.ViewInteractions;

public class FirstQuestionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return FirstQuestionActivity.this;
    }

    @Override
    protected void addElementsToLists() {
        availableElements.add(new Element(FirstQuestionActivity.this, "Sodium", "Na",list_start_x, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
        availableElements.add(new Element(FirstQuestionActivity.this, "Chlorine", "Cl",list_start_x + getPositionOffset(el_width), list_start_y, elementId++, Color.BLUE, ElementGroup.HALOGENS));
        availableElements.add(new Element(FirstQuestionActivity.this, "Bromine", "B",list_start_x + (getPositionOffset(el_width)) * 2, list_start_y, elementId++, Color.BLACK, ElementGroup.HALOGENS));
        availableElements.add(new Element(FirstQuestionActivity.this, "Lithium", "Li",list_start_x + (getPositionOffset(el_width))* 3, list_start_y, elementId++, Color.LTGRAY, ElementGroup.ALKALIMETALS));
    }
}
