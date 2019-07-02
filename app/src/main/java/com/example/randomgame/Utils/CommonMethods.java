package com.example.randomgame.Utils;

import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ThreadLocalRandom;

public class CommonMethods {
    public static void setIconsSize(View view, int dimen) {
        view.getLayoutParams().width = dimen;
        view.getLayoutParams().height = dimen;
    }

    public static void random(int min, int max){
        int random = ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }
}
