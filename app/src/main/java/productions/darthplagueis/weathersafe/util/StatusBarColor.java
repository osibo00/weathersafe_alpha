package productions.darthplagueis.weathersafe.util;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;


public class StatusBarColor {

    public static void setStatusBarColor(FragmentActivity activity, int color) {
        if (activity != null) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }
}
