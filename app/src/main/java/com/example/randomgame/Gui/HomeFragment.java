package com.example.randomgame.Gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.randomgame.Gui.Dice.DiceFragment;
import com.example.randomgame.Gui.Slot.SlotFragment;
import com.example.randomgame.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {

    @BindView(R.id.home_dice)
    ImageView homeDice;
    @BindView(R.id.home_lucky)
    ImageView homeLucky;
    @BindView(R.id.home_spin)
    ImageView homeSpin;
    @BindView(R.id.home_slot)
    ImageView homeSlot;
    Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.home_dice, R.id.home_lucky, R.id.home_spin, R.id.home_slot})
    public void onViewClicked(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.home_dice:
                ft.replace(R.id.container, new DiceFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.home_lucky:
                break;
            case R.id.home_spin:
                ft.replace(R.id.container, new SpinFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.home_slot:
                ft.replace(R.id.container, new SlotFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
