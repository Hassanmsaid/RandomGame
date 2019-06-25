package com.example.randomgame.Gui.Dice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.randomgame.R;

public class DiceFragment extends Fragment {

    ImageView firstDice1, secondDice1;
    TextView rollDiceBtn1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dice, container, false);

        firstDice1 = view.findViewById(R.id.first_dice1);
        secondDice1 = view.findViewById(R.id.second_dice1);
        rollDiceBtn1 = view.findViewById(R.id.dice_btn1);
        rollDiceBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 360 * 20, Animation.RELATIVE_TO_SELF, 0.5f,          Animation.RELATIVE_TO_SELF, 0.5f);
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
            }
        });
        return view;
    }
}
