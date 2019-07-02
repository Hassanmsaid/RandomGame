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
import com.example.randomgame.Gui.Spin.SpinFragment;
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

        switch (view.getId()) {
            case R.id.home_dice:
                openFragment(new DiceFragment());
                break;
            case R.id.home_lucky:
                break;
            case R.id.home_spin:
                openFragment(new SpinFragment());
                break;
            case R.id.home_slot:
                openFragment(new SlotFragment());
                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }
}
