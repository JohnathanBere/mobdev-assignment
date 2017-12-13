package johnbere.chemistrydd.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import johnbere.chemistrydd.R;
import johnbere.chemistrydd.activities.base.BaseActivity;
import johnbere.chemistrydd.helpers.Grade;

public class FinalScreen extends BaseActivity {
    TextView grade, resultsTxt;
    float scorePercentage;
    Button retryQuiz;

    @Override
    protected void viewActivityBindings() {
        grade = findViewById(R.id.grade);
        retryQuiz = findViewById(R.id.retry);
        grade.setTextSize(200);

        resultsTxt = findViewById(R.id.resultsText);

        scorePercentage = totalScore / maxPossibleScore * 100;

//        Toast.makeText(context, "score percentage: " + scorePercentage + "%", Toast.LENGTH_SHORT).show();

        if (scorePercentage <= 100 && scorePercentage >= 76){
            grade.setText(Grade.A.toString());
            resultsTxt.setText("Excellent!");
        }
        else if(scorePercentage <= 75 && scorePercentage >= 66) {
            grade.setText(Grade.B.toString());
            resultsTxt.setText("Pretty good!");
        }
        else if(scorePercentage <= 65 && scorePercentage >= 56) {
            grade.setText(Grade.C.toString());
            resultsTxt.setText("Decent!");
        }
        else if(scorePercentage <= 55 && scorePercentage >= 46) {
            grade.setText(Grade.D.toString());
            resultsTxt.setText("Uh oh...");
        }
        else if(scorePercentage <= 45 && scorePercentage >= 36) {
            grade.setText(Grade.E.toString());
            resultsTxt.setText("Not looking good");
        }
        else if(scorePercentage <= 35 && scorePercentage >= 0) {
            grade.setText(Grade.F.toString());
            resultsTxt.setText("Try again!");
        }
        else {
            grade.setText("?");
            resultsTxt.setText("You did so bad, it made a negative score!");
        }

        retryQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Properly dispose of everything else if the app glitches out
                moveToNextActivity(getNextActivity());
                finish();
            }
        });
    }

    @Override
    protected void getDataFromPreviousActivity() {
        retrieveDifficultyData();
    }

    @Override
    protected Context getCurrentContext() {
        return FinalScreen.this;
    }

    @Override
    public BaseActivity getNextActivity() {
        return new MainActivity();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make sure the timer does not begin running, an exception will form other wise...
        timer.cancel();
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.activity_final_screen;
    }

    @Override
    protected int getContentLayoutId() {
        return R.id.content_final_screen;
    }

    @Override
    protected int getTimerText() {
        return 0;
    }

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
    protected void addElementsToLists() {}

    @Override
    protected void pushDataToNextActivity() {}

    @Override
    public void beginCountdown() {}
}
