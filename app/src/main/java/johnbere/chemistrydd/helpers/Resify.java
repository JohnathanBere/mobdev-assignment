package johnbere.chemistrydd.helpers;

import johnbere.chemistrydd.activities.base.BaseActivity;

public class Resify {
    private BaseActivity activity;

    public Resify(BaseActivity activity) {
        this.activity = activity;
    }

    public String stringResify(int string) {
        return activity.getResources().getString(string);
    }

    public int intResify(int integer) {
        return activity.getResources().getInteger(integer);
    }
}
