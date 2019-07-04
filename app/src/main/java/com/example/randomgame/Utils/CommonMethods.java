package com.example.randomgame.Utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class CommonMethods {
    public static void setIconsSize(View view, int dimen) {
        view.getLayoutParams().width = dimen;
        view.getLayoutParams().height = dimen;

        /*int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
        view.getLayoutParams().height = dimensionInDp;
        view.getLayoutParams().width = dimensionInDp;
        view.requestLayout();*/
    }

    public static int random(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
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

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
