package com.example.randomgame.Gui.Slot;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomgame.R;
import com.example.randomgame.Utils.ISlotEventEnd;
import com.example.randomgame.Utils.SLotImageScrolling;

import java.util.Random;

public class SlotFragment extends Fragment implements ISlotEventEnd {

    TextView slotBtn1;
    SLotImageScrolling slotImage1, slotImage2, slotImage3;
    int count_done = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slot, container, false);

        //Clone test

        slotBtn1 = view.findViewById(R.id.slot_btn1);
        slotImage1 = view.findViewById(R.id.slot_img1);
        slotImage2 = view.findViewById(R.id.slot_img2);
        slotImage3 = view.findViewById(R.id.slot_img3);

        slotImage1.setSlotEventEnd(this);
        slotImage2.setSlotEventEnd(this);
        slotImage3.setSlotEventEnd(this);

        slotBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slotImage1.setValueRandom(new Random().nextInt(SLotImageScrolling.NUM_OF_IMAGES),
                        new Random().nextInt((15 - 5) + 1) + 5);
                slotImage2.setValueRandom(new Random().nextInt(SLotImageScrolling.NUM_OF_IMAGES),
                        new Random().nextInt((15 - 5) + 1) + 5);
                slotImage3.setValueRandom(new Random().nextInt(SLotImageScrolling.NUM_OF_IMAGES),
                        new Random().nextInt((15 - 5) + 1) + 5);
                slotBtn1.setClickable(false);
            }
        });
        return view;
    }

    @Override
    public void eventEnd(int result, int count) {
        if(count_done < 2){
            count_done++;
        }
        else{
            count_done = 0;
            Toast.makeText(getContext(), "DONE !!", Toast.LENGTH_SHORT).show();
            slotBtn1.setClickable(true);
        }
    }
}
