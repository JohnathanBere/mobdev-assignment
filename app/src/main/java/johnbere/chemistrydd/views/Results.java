package johnbere.chemistrydd.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

// This will be the results panel that will feed the user back their scores for that question.
// Depending if they got all of the questions, or some of the questions right.
// If all the answers were correct, the text should say perfect! With a button that then takes them to the next page.
// While this pane is active, no other buttons should be active, and said pane should be at the front.
// For any incorrect answers, just make scientific formulas that tell them what they should have made.
// Or decomposed
public class Results extends View {
    private Paint paneColor, titleTextColor, solutionTextColor, panelColor;
    private int paneShader, textShader, panelShader;
    private String titleText;
    private ArrayList<String> solutionText;
    private boolean isCompletelyCorrect;
    private Rect panel, paneHeading;

    public Results(Context context, String titleText, ArrayList<String> solutionText, boolean isCompletelyCorrect) {
        super(context);

        this.titleText = titleText;
        this.solutionText = solutionText;
        this.isCompletelyCorrect = isCompletelyCorrect;

        panel = new Rect();
        panel.left = 80;
        panel.right = 950;
        panel.top = 80;

        if (isCompletelyCorrect) {
            panel.top = 500;
            panel.bottom = panel.top + 300;
        }
        else {
            panel.bottom = panel.top;
        }

        paneHeading = new Rect();
        paneHeading.left = panel.left;
        paneHeading.right = panel.right;
        paneHeading.top = panel.top;
        paneHeading.bottom = paneHeading.top + 125;

        paneColor = new Paint();
        panelColor = new Paint();
        titleTextColor = new Paint();
        solutionTextColor = new Paint();

        if (isCompletelyCorrect) {
            paneShader = Color.rgb(51, 204, 51);
            textShader = Color.rgb(0, 153, 51);
            panelShader = Color.rgb(102, 255, 102);
        } else {
            paneShader = Color.rgb(255, 92, 51);
            textShader = Color.rgb(179, 36, 0);
            panelShader = Color.rgb(255, 153, 128);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        panelColor.setColor(panelShader);
        titleTextColor.setColor(textShader);
        solutionTextColor.setColor(textShader);
        paneColor.setColor(paneShader);

        titleTextColor.setTextSize(60);
        solutionTextColor.setTextSize(45);

        canvas.drawRect(this.panel, panelColor);
        canvas.drawRect(this.paneHeading, paneColor);
        canvas.drawText(this.titleText, (float)(paneHeading.left + 15), (float)(paneHeading.top + 75), titleTextColor);

        if (this.solutionText.size() > 0) {
            for (int i = 0; i < this.solutionText.size(); i++) {
                canvas.drawText(this.solutionText.get(i), (float)(panel.left + 15), (float)(paneHeading.top + 200 + (200 * i)), solutionTextColor);
                if (i == this.solutionText.size() - 1) {
                    canvas.drawText("...Touch to continue", (float)(panel.left + 15), (float)(paneHeading.top + 200 + (200 * (i + 1))), solutionTextColor);
                }
            }
            panel.bottom = panel.top + (this.solutionText.size() * 450);
            this.invalidate();
        }
        if (this.solutionText.size() == 0) {
            canvas.drawText("...Touch to continue", (float)(panel.left + 15), (float)(paneHeading.top + 200), solutionTextColor);
        }
    }
}
