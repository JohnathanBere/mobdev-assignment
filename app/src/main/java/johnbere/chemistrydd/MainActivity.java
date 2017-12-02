package johnbere.chemistrydd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Element el, el2, el3;
    ArrayList<Element> availableElements = new ArrayList<Element>();
    // RelativeLayout relativeLayout;
    ViewGroup content;
    View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Element el = (Element)v;

            // Checks if the axes of the touch points match the axes of an element's grid box. If it does, it should proceed to move
            // that element
            if ((event.getX() > el.r.x - el.r.width && event.getX() < el.r.x + el.r.width)
                && (event.getY() > el.r.y - el.r.height && event.getY() < el.r.y + el.r.width)) {
                el.handleTouch(el, event);
            }

            return false;
        }
    };

    View.OnDragListener drag = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            Element el = (Element)v;

            if ((event.getX() > el.r.x - el.r.width && event.getX() < el.r.x + el.r.width)
                && (event.getY() > el.r.y - el.r.height && event.getX() < el.r.y + el.r.width)) {
                el.handleDrag(v, event);
            }
            return true;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         content = (ViewGroup) findViewById(R.id.content_main);

        availableElements.add(new Element(MainActivity.this, "Sodium", "Na",100, 1000));
        availableElements.add(new Element(MainActivity.this, "Chlorine", "Cl",400, 1000));
        availableElements.add(new Element(MainActivity.this, "Bromine", "B",700, 1000));

        // Sets the event listener before appending this programmatically to the view.
        for (Element el: availableElements) {
            el.setOnTouchListener(touch);
            el.setOnDragListener(drag);
            content.addView(el);
        }


        /*
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                el = new Element(MainActivity.this, "Sodium", "Na",400, 300, 1);

                relativeLayout.addView(el);
            }
        });
        */
    }
}
