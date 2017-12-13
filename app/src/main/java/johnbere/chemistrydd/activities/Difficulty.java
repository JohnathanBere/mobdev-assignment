package johnbere.chemistrydd.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.helpers.Game;

public class Difficulty extends BaseActivity {
    Button easy, medium, hard;
    // Once a difficulty is selected, that needs to be passed along the intents.
    View.OnClickListener difficultyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.easyBtn) {
                difficulty = Game.EASY;
            }
            else if (view.getId() == R.id.medBtn) {
                difficulty = Game.MEDIUM;
            }
            else if (view.getId() == R.id.hardBtn) {
                difficulty = Game.HARD;
            }
            moveToNextActivity(getNextActivity());
            finish();
        }
    };

    @Override
    protected void pushDataToNextActivity() {
        pushDifficultyData();
    }

    @Override
    protected void viewActivityBindings() {
        easy = findViewById(R.id.easyBtn);
        medium = findViewById(R.id.medBtn);
        hard = findViewById(R.id.hardBtn);

        easy.setOnClickListener(difficultyListener);
        medium.setOnClickListener(difficultyListener);
        hard.setOnClickListener(difficultyListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_difficulty;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.content_difficulty;
    }

    @Override
    protected int getTimerText() {
        return 0;
    }

    @Override
    protected Context getCurrentContext() {
        return Difficulty.this;
    }

    @Override
    public BaseActivity getNextActivity() {
        return new FirstQuestion();
    }

    @Override
    protected void addElementsToLists() {}

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
