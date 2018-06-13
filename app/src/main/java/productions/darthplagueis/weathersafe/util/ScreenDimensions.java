package productions.darthplagueis.weathersafe.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenDimensions {

    public static int getOneFourthDimens(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double widthPixels = metrics.widthPixels * 25/100;
        return (int) widthPixels;
    }

    public static int getOneHalfDimens(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double widthPixels = metrics.widthPixels * 50/100;
        return (int) widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        double heightPixels = metrics.heightPixels;
        return (int) heightPixels;
    }
}
