package com.example.randomgame.Utils;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.randomgame.R;

import java.util.Random;

public class SLotImageScrolling extends FrameLayout {

    public static int ANIMATION_DURATION = 20, NUM_OF_IMAGES = 6;
    public ImageView currentImage, nextImage;
    public int lastResult = 0, oldValue = 0;
    public ISlotEventEnd slotEventEnd;

    public void setSlotEventEnd(ISlotEventEnd slotEventEnd) {
        this.slotEventEnd = slotEventEnd;
    }

    public SLotImageScrolling(Context context) {
        super(context);
        init(context);
    }

    public SLotImageScrolling(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.slot_image_scrolling, this);
        currentImage = findViewById(R.id.current_image);
        nextImage = findViewById(R.id.next_image);
        nextImage.setTranslationY(getHeight());

        int randomInitImage = new Random().nextInt((6 - 1) + 1) + 1;
        switch (randomInitImage){
            case 1:
                currentImage.setImageResource(R.drawable.slot_image1);
                nextImage.setImageResource(R.drawable.slot_image1);
                break;
            case 2:
                currentImage.setImageResource(R.drawable.slot_image2);
                nextImage.setImageResource(R.drawable.slot_image2);
                break;
            case 3:
                currentImage.setImageResource(R.drawable.slot_image3);
                nextImage.setImageResource(R.drawable.slot_image3);
                break;
            case 4:
                currentImage.setImageResource(R.drawable.slot_image4);
                nextImage.setImageResource(R.drawable.slot_image4);
                break;
            case 5:
                currentImage.setImageResource(R.drawable.slot_image5);
                nextImage.setImageResource(R.drawable.slot_image5);
                break;
            case 6:
                currentImage.setImageResource(R.drawable.slot_image6);
                nextImage.setImageResource(R.drawable.slot_image6);
                break; 
        }
    }

    public void setValueRandom(final int image, final int rotateCount) {
        currentImage.animate().translationY(-getHeight()).setDuration(ANIMATION_DURATION).start();
        nextImage.setTranslationY(nextImage.getHeight());
        nextImage.animate().translationY(0)
                .setDuration(ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setImage(currentImage, oldValue % NUM_OF_IMAGES);
                        currentImage.setTranslationY(0);
                        if (oldValue != rotateCount) {
                            setValueRandom(image, rotateCount);
                            oldValue++;
                        } else {
                            lastResult = 0;
                            oldValue = 0;
                            setImage(nextImage, image);
                            slotEventEnd.eventEnd(image % NUM_OF_IMAGES, rotateCount);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void setImage(ImageView imageView, int value) {
        switch (value) {
            case 1:
                imageView.setImageResource(R.drawable.slot_image1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.slot_image2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.slot_image3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.slot_image4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.slot_image5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.slot_image6);
                break;
        }
        imageView.setTag(value);
        lastResult = value;
    }

    public void setInitialImage(ImageView imageView, int value) {
        switch (value) {
            case 1:
                imageView.setImageResource(R.drawable.slot_image1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.slot_image2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.slot_image3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.slot_image4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.slot_image5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.slot_image6);
                break;
        }
    }

    public int getValue() {
        return Integer.parseInt(nextImage.getTag().toString());
    }
}
