package com.example.randomgame.Gui.Dice;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.randomgame.R;
import com.example.randomgame.Utils.CustomGestureDetector;
import com.example.randomgame.Utils.IViewFlipper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.randomgame.Utils.CommonMethods.setIconsSize;

public class DiceFragment extends Fragment implements IViewFlipper {
    GestureDetector gestureDetector;

    @BindView(R.id.first_dice1)
    ImageView firstDice1;
    @BindView(R.id.second_dice1)
    ImageView secondDice1;
    @BindView(R.id.dice_btn1)
    TextView diceBtn1;
    @BindView(R.id.first_dice2)
    ImageView firstDice2;
    @BindView(R.id.second_dice2)
    ImageView secondDice2;
    @BindView(R.id.dice_btn2)
    TextView diceBtn2;
    @BindView(R.id.first_dice3)
    ImageView firstDice3;
    @BindView(R.id.second_dice3)
    ImageView secondDice3;
    @BindView(R.id.dice_btn3)
    TextView diceBtn3;
    @BindView(R.id.dice_view_flipper)
    ViewFlipper diceViewFlipper;
    @BindView(R.id.dice_icon1)
    ImageView diceIcon1;
    @BindView(R.id.dice_icon2)
    ImageView diceIcon2;
    @BindView(R.id.dice_icon3)
    ImageView diceIcon3;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);
        unbinder = ButterKnife.bind(this, view);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector(getContext(), diceViewFlipper, this);
        gestureDetector = new GestureDetector(getContext(), customGestureDetector);
        resetAllIcons();
        setIconsSize(diceIcon1, 150);
    }

    @Override
    public void resetAllIcons() {
        setIconsSize(diceIcon1, 100);
        setIconsSize(diceIcon2, 100);
        setIconsSize(diceIcon3, 100);
    }

    @Override
    public void chooseIcon() {
        switch (diceViewFlipper.getDisplayedChild()) {
            case 0:
                setIconsSize(diceIcon1, 150);
                break;
            case 1:
                setIconsSize(diceIcon2, 150);
                break;
            case 2:
                setIconsSize(diceIcon3, 150);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.dice_btn1, R.id.dice_btn2, R.id.dice_btn3, R.id.dice_icon1, R.id.dice_icon2, R.id.dice_icon3})
    public void onViewClicked(View view) {
        int currentDice = diceViewFlipper.getDisplayedChild();
        switch (view.getId()) {
            case R.id.dice_btn1:
                RotateAnimation rotate = new RotateAnimation(0, 360 * 20, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(3000);
                rotate.setInterpolator(new LinearInterpolator());

                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        firstDice1.setImageResource(R.drawable.dice2);
                        secondDice1.setImageResource(R.drawable.dice5);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                firstDice1.startAnimation(rotate);
                secondDice1.startAnimation(rotate);
                break;
            case R.id.dice_btn2:
                break;
            case R.id.dice_btn3:
                break;
            case R.id.dice_icon1:
                if (currentDice == 1 || currentDice == 2) {
                    diceViewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
                    diceViewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    diceViewFlipper.setDisplayedChild(0);
                    resetAllIcons();
                    setIconsSize(diceIcon1, 150);
                }
                break;
            case R.id.dice_icon2:
                if (currentDice == 0) {
                    diceViewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                    diceViewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                    diceViewFlipper.setDisplayedChild(1);
                    resetAllIcons();
                } else if (currentDice == 2) {
                    diceViewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
                    diceViewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    diceViewFlipper.setDisplayedChild(1);
                    resetAllIcons();
                }
                setIconsSize(diceIcon2, 150);
                break;
            case R.id.dice_icon3:
                if (currentDice == 0 || currentDice == 1) {
                    diceViewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                    diceViewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                    diceViewFlipper.setDisplayedChild(2);
                    resetAllIcons();
                    setIconsSize(diceIcon3, 150);
                }
                break;
        }
    }
}
