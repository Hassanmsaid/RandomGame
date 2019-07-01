package com.example.randomgame.Utils;

import android.view.View;

public class CommonMethods {
    public static void setIconsSize(View view, int dimen) {
        view.getLayoutParams().width = dimen;
        view.getLayoutParams().height = dimen;
    }
}
