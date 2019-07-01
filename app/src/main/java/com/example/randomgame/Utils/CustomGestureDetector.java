package com.example.randomgame.Utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

import com.example.randomgame.R;

public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private Context context;
    private ViewFlipper viewFlipper;
    private IViewFlipper iViewFlipper;

    public CustomGestureDetector() {
    }

    public CustomGestureDetector(Context context, ViewFlipper viewFlipper, IViewFlipper iViewFlipper) {
        this.context = context;
        this.viewFlipper = viewFlipper;
        this.iViewFlipper = iViewFlipper;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Swipe left (next)
        if (e1.getX() > e2.getX()) {
            if (viewFlipper.getDisplayedChild() != 2) {
                viewFlipper.setInAnimation(context, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(context, R.anim.slide_out_left);
                viewFlipper.showNext();
            }
        }

        // Swipe right (previous)
        if (e1.getX() < e2.getX()) {
            if (viewFlipper.getDisplayedChild() != 0) {
                viewFlipper.setInAnimation(context, android.R.anim.slide_in_left);
                viewFlipper.setOutAnimation(context, android.R.anim.slide_out_right);
                viewFlipper.showPrevious();
            }
        }
        iViewFlipper.resetAllIcons();

        iViewFlipper.chooseIcon();

        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
