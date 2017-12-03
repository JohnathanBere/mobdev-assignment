package johnbere.chemistrydd;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import java.util.ArrayList;
import johnbere.chemistrydd.helpers.ElementGroup;
import johnbere.chemistrydd.helpers.EventListeners;
import johnbere.chemistrydd.helpers.ViewInteractions;

public class MainActivity extends AppCompatActivity {
    ArrayList<Element> availableElements = new ArrayList<>();
    public ViewInteractions interactions = new ViewInteractions(this);
    ViewGroup content;
    public MediaPlayer player;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.content_main);
        player = MediaPlayer.create(MainActivity.this, R.raw.deraj_pop);


        availableElements.add(new Element(MainActivity.this, "Sodium", "Na",100, 1000, 1, Color.LTGRAY, ElementGroup.GROUP1));
        availableElements.add(new Element(MainActivity.this, "Chlorine", "Cl",400, 1000, 2, Color.BLUE, ElementGroup.GROUP7));
        availableElements.add(new Element(MainActivity.this, "Bromine", "B",700, 1000, 3, Color.BLACK, ElementGroup.GROUP7));

        // The helper class should now observe the elements
        interactions.setElements(availableElements);

        // Log.d("JB", "interactions has " + interactions.getElements().size() + " elements in it on the MainActivity");

        // Sets the event listener before appending this programmatically to the view.
        for (Element el: availableElements) {
            el.setTag(el.getName());
            el.setOnTouchListener(new EventListeners(MainActivity.this).ElementTouchListener);
            content.addView(el);
        }
        content.setOnDragListener(new EventListeners(this).LayoutDragListener);
    }
}
