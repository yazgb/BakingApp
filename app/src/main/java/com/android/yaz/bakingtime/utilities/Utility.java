package com.android.yaz.bakingtime.utilities;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utility {

    protected Utility() {}

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 250);
        return noOfColumns;
    }
}
