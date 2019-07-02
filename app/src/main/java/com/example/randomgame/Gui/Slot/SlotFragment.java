package com.example.randomgame.Gui.Slot;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.randomgame.R;
import com.example.randomgame.Utils.CustomGestureDetector;
import com.example.randomgame.Utils.ISlotEventEnd;
import com.example.randomgame.Utils.IViewFlipper;
import com.example.randomgame.Utils.SlotImageScrolling;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.randomgame.Utils.CommonMethods.enableDisableViewGroup;
import static com.example.randomgame.Utils.CommonMethods.setIconsSize;

public class SlotFragment extends Fragment implements ISlotEventEnd, IViewFlipper {

    private final static int MAX_SLOT_COUNT = 50, MIN_SLOT_COUNT = 30;
    int count_done = 0, currentSlot;
    GestureDetector gestureDetector;

    @BindView(R.id.slot1_img1)
    SlotImageScrolling slot1Img1;
    @BindView(R.id.slot1_img2)
    SlotImageScrolling slot1Img2;
    @BindView(R.id.slot1_img3)
    SlotImageScrolling slot1Img3;
    @BindView(R.id.slot_btn1)
    TextView slotBtn1;
    @BindView(R.id.slot2_img1)
    SlotImageScrolling slot2Img1;
    @BindView(R.id.slot2_img2)
    SlotImageScrolling slot2Img2;
    @BindView(R.id.slot2_img3)
    SlotImageScrolling slot2Img3;
    @BindView(R.id.slot_btn2)
    TextView slotBtn2;
    @BindView(R.id.slot3_img1)
    SlotImageScrolling slot3Img1;
    @BindView(R.id.slot3_img2)
    SlotImageScrolling slot3Img2;
    @BindView(R.id.slot3_img3)
    SlotImageScrolling slot3Img3;
    @BindView(R.id.slot_btn3)
    TextView slotBtn3;
    @BindView(R.id.slot_view_flipper)
    ViewFlipper slotViewFlipper;
    @BindView(R.id.slot_icon1)
    ImageView slotIcon1;
    @BindView(R.id.slot_icon2)
    ImageView slotIcon2;
    @BindView(R.id.slot_icon3)
    ImageView slotIcon3;
    Unbinder unbinder;
    @BindView(R.id.slot_layout)
    LinearLayout slotLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slot, container, false);
        unbinder = ButterKnife.bind(this, view);

        slot1Img1.setSlotEventEnd(this);
        slot1Img2.setSlotEventEnd(this);
        slot1Img3.setSlotEventEnd(this);
        slot2Img1.setSlotEventEnd(this);
        slot2Img2.setSlotEventEnd(this);
        slot2Img3.setSlotEventEnd(this);
        slot3Img1.setSlotEventEnd(this);
        slot3Img2.setSlotEventEnd(this);
        slot3Img3.setSlotEventEnd(this);

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
        CustomGestureDetector customGestureDetector = new CustomGestureDetector(getContext(), slotViewFlipper, this);
        gestureDetector = new GestureDetector(getContext(), customGestureDetector);
        resetAllIcons();
        setIconsSize(slotIcon1, 150);
    }

    @Override
    public void eventEnd(int result, int count) {
        if (count_done < 2) {
            count_done++;
        } else {
            count_done = 0;
            slotBtn1.setClickable(true);
            slotBtn2.setClickable(true);
            slotBtn3.setClickable(true);

            currentSlot = slotViewFlipper.getDisplayedChild();
            switch (currentSlot) {
                case 0:
                    Toast.makeText(getContext(), slot1Img1.getValue() + String.valueOf(slot1Img2.getValue()) + slot1Img3.getValue(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getContext(), slot2Img1.getValue() + String.valueOf(slot2Img2.getValue()) + slot2Img3.getValue(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), slot3Img1.getValue() + String.valueOf(slot3Img2.getValue()) + slot3Img3.getValue(), Toast.LENGTH_SHORT).show();
                    break;
            }

            /*Toast.makeText(getContext(), slot1Img1.getValue() + String.valueOf(slot1Img2.getValue()) + slot1Img3.getValue(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), slot2Img1.getValue() + String.valueOf(slot2Img2.getValue()) + slot2Img3.getValue(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), slot3Img1.getValue() + String.valueOf(slot3Img2.getValue()) + slot3Img3.getValue(), Toast.LENGTH_SHORT).show();*/
            /*TODO: Handle swipe while animation*/
            enableDisableViewGroup(slotLayout, true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.slot_btn1, R.id.slot_btn2, R.id.slot_btn3, R.id.slot_icon1, R.id.slot_icon2, R.id.slot_icon3})
    public void onViewClicked(View view) {

        currentSlot = slotViewFlipper.getDisplayedChild();

        switch (view.getId()) {
            case R.id.slot_btn1:
                startSlot(slot1Img1);
                startSlot(slot1Img2);
                startSlot(slot1Img3);
//                disableSlotButtons();
                break;
            case R.id.slot_btn2:
                startSlot(slot2Img1);
                startSlot(slot2Img2);
                startSlot(slot2Img3);
//                disableSlotButtons();
                break;
            case R.id.slot_btn3:
                startSlot(slot3Img1);
                startSlot(slot3Img2);
                startSlot(slot3Img3);
//                disableSlotButtons();
                break;
            case R.id.slot_icon1:
                if (currentSlot == 1 || currentSlot == 2) {
                    slotViewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
                    slotViewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    slotViewFlipper.setDisplayedChild(0);
                    resetAllIcons();
                    setIconsSize(slotIcon1, 150);
                }
                break;
            case R.id.slot_icon2:
                if (currentSlot == 0) {
                    slotViewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                    slotViewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                    slotViewFlipper.setDisplayedChild(1);
                    resetAllIcons();
                } else if (currentSlot == 2) {
                    slotViewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
                    slotViewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
                    slotViewFlipper.setDisplayedChild(1);
                    resetAllIcons();
                }
                setIconsSize(slotIcon2, 150);
                break;
            case R.id.slot_icon3:
                if (currentSlot == 0 || currentSlot == 1) {
                    slotViewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
                    slotViewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
                    slotViewFlipper.setDisplayedChild(2);
                    resetAllIcons();
                    setIconsSize(slotIcon3, 150);
                }
                break;
        }
    }

    private void disableSlotButtons() {
        slotBtn1.setClickable(false);
        slotBtn2.setClickable(false);
        slotBtn3.setClickable(false);
    }

    private void startSlot(SlotImageScrolling image) {
        image.setValueRandom(new Random().nextInt(6) + 1,
                new Random().nextInt((MAX_SLOT_COUNT - MIN_SLOT_COUNT) + 1) + MIN_SLOT_COUNT);
        enableDisableViewGroup(slotLayout, false);
    }

    @Override
    public void resetAllIcons() {
        setIconsSize(slotIcon1, 100);
        setIconsSize(slotIcon2, 100);
        setIconsSize(slotIcon3, 100);
    }

    @Override
    public void chooseIcon() {
        switch (slotViewFlipper.getDisplayedChild()) {
            case 0:
                setIconsSize(slotIcon1, 150);
                break;
            case 1:
                setIconsSize(slotIcon2, 150);
                break;
            case 2:
                setIconsSize(slotIcon3, 150);
                break;
        }
    }
}