package com.example.randomgame.Gui.Spin;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.randomgame.Gui.HomeActivity;
import com.example.randomgame.R;
import com.example.randomgame.Utils.CustomGestureDetector;
import com.example.randomgame.Utils.IViewFlipper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.randomgame.Utils.CommonMethods.setIconsSize;
import static com.example.randomgame.Utils.CommonMethods.showToast;

public class SpinFragment extends Fragment implements IViewFlipper, RewardedVideoAdListener {

    GestureDetector gestureDetector;
    ImageView selectedWheel;
    ViewFlipper viewFlipper;
    RewardedVideoAd videoAd;
    boolean rewarded;

    //check if wheel is rotating
    boolean rotating = false;
    //number of wheel sections
    int number = 8;
    float degrees = 0;

    @BindView(R.id.spinning_wheel1)
    ImageView spinningWheel1;
    @BindView(R.id.spinning_wheel2)
    ImageView spinningWheel2;
    @BindView(R.id.spinning_wheel3)
    ImageView spinningWheel3;
    @BindView(R.id.spin_view_flipper)
    ViewFlipper spinViewFlipper;
    Unbinder unbinder;
    @BindView(R.id.spin_icon1)
    ImageView spinIcon1;
    @BindView(R.id.spin_icon2)
    ImageView spinIcon2;
    @BindView(R.id.spin_icon3)
    ImageView spinIcon3;
    @BindView(R.id.rotate_btn1)
    TextView rotateBtn1;
    @BindView(R.id.rotate_btn2)
    TextView rotateBtn2;
    @BindView(R.id.rotate_btn3)
    TextView rotateBtn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_spin, container, false);

        // Admob
        MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
        videoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        videoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        //////////////////////

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        viewFlipper = view.findViewById(R.id.spin_view_flipper);
        selectedWheel = view.findViewById(R.id.spinning_wheel1);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void loadRewardedVideoAd() {
        videoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    private void startVideoAd() {
        if (videoAd.isLoaded()) {
            videoAd.show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector(getContext(), viewFlipper, this);
        gestureDetector = new GestureDetector(getContext(), customGestureDetector);
        resetAllIcons();
        setIconsSize(spinIcon1, 150);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void spin(final int currentWheel) {
        if (!rotating) {
            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.btn_click_sound1);
            mp.start();

            long random = new Random().nextInt(360) + 3600;
            RotateAnimation animation = new RotateAnimation(degrees, degrees + random,
                    1, (float) 0.5, 1, (float) 0.5);
            degrees = (degrees + random) % 360;

            animation.setDuration(5000);
            animation.setFillAfter(true);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    rotating = true;
                    ((HomeActivity) getActivity()).disableAllTouches();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int valueWon = (int) (((double) number) - Math.floor(((double) degrees) / (360.0d / ((double) number))));
                    ((HomeActivity) getActivity()).enableAllTouches();
                    switch (valueWon) {
                        case 1:
                            if (currentWheel == 1)
                                valueWon = 0;
                            else if (currentWheel == 2)
                                valueWon = 50;
                            else
                                valueWon = 100;
                            break;
                        case 2:
                            if (currentWheel == 1)
                                valueWon = 400;
                            else if (currentWheel == 2)
                                valueWon = 400;
                            else
                                valueWon = 700;
                            break;
                        case 3:
                            if (currentWheel == 1)
                                valueWon = 100;
                            else if (currentWheel == 2)
                                valueWon = 250;
                            else
                                valueWon = 250;
                            break;
                        case 4:
                            if (currentWheel == 1)
                                valueWon = 500;
                            else if (currentWheel == 2)
                                valueWon = 500;
                            else
                                valueWon = 450;
                            break;
                        case 5:
                            if (currentWheel == 1)
                                valueWon = 50;
                            else if (currentWheel == 2)
                                valueWon = 650;
                            else
                                valueWon = 600;
                            break;
                        case 6:
                            if (currentWheel == 1)
                                valueWon = 200;
                            else if (currentWheel == 2)
                                valueWon = 800;
                            else
                                valueWon = 800;
                            break;
                        case 7:
                            if (currentWheel == 1)
                                valueWon = 150;
                            else if (currentWheel == 2)
                                valueWon = 150;
                            else
                                valueWon = 950;
                            break;
                        case 8:
                            if (currentWheel == 1)
                                valueWon = 650;
                            else if (currentWheel == 2)
                                valueWon = 900;
                            else
                                valueWon = 1000;
                            break;
                        default:
                            break;
                    }
                    Toast.makeText(getContext(), String.valueOf(valueWon), Toast.LENGTH_SHORT).show();
                    rotating = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            switch (currentWheel) {
                case 1:
                    spinningWheel1.startAnimation(animation);
                    break;
                case 2:
                    spinningWheel2.startAnimation(animation);
                    break;
                case 3:
                    spinningWheel3.startAnimation(animation);
                    break;
            }
        }
    }

    @OnClick({R.id.spin_icon1, R.id.spin_icon2, R.id.spin_icon3, R.id.rotate_btn1, R.id.rotate_btn2, R.id.rotate_btn3})
    public void onViewClicked(View view) {

        int currentWheel = viewFlipper.getDisplayedChild();

        switch (view.getId()) {
            case R.id.spin_icon1:
                if (currentWheel == 1 || currentWheel == 2) {
                    viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
                    viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    viewFlipper.setDisplayedChild(0);
                    resetAllIcons();
                    setIconsSize(spinIcon1, 150);
                }
                break;
            case R.id.spin_icon2:
                if (currentWheel == 0) {
                    viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                    viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                    viewFlipper.setDisplayedChild(1);
                    resetAllIcons();
                } else if (currentWheel == 2) {
                    viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
                    viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    viewFlipper.setDisplayedChild(1);
                    resetAllIcons();
                }
                setIconsSize(spinIcon2, 150);
                break;
            case R.id.spin_icon3:
                if (currentWheel == 0 || currentWheel == 1) {
                    viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                    viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                    viewFlipper.setDisplayedChild(2);
                    resetAllIcons();
                    setIconsSize(spinIcon3, 150);
                }
                break;
            case R.id.rotate_btn1:
                spin(1);
                break;
            case R.id.rotate_btn2:
//                spin(2);
                rewarded = false;
                startVideoAd();
                break;
            case R.id.rotate_btn3:
                spin(3);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity) getActivity()).enableAllTouches();
    }

    @Override
    public void resetAllIcons() {
        setIconsSize(spinIcon1, 100);
        setIconsSize(spinIcon2, 100);
        setIconsSize(spinIcon3, 100);
    }

    @Override
    public void chooseIcon() {
        switch (viewFlipper.getDisplayedChild()) {
            case 0:
                setIconsSize(spinIcon1, 150);
                break;
            case 1:
                setIconsSize(spinIcon2, 150);
                break;
            case 2:
                setIconsSize(spinIcon3, 150);
                break;
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        if (rewarded) {
            spin(2);
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        // TODO: handle force close video then rotate again
        rewarded = true;
        showToast(getContext(), "Video rewarded");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
