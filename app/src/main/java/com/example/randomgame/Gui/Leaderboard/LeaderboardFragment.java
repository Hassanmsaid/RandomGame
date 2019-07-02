package com.example.randomgame.Gui.Leaderboard;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.randomgame.R;

public class LeaderboardFragment extends Fragment {

    TextView leadTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leadTxt = view.findViewById(R.id.leaderboard_txt);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Calendar c = Calendar.getInstance();
                    leadTxt.setText(c.get(Calendar.HOUR) + " : " + c.get(Calendar.MINUTE) + " : " + c.get(Calendar.SECOND));
                }
            }

            public void onFinish() {

            }
        };
        newtimer.start();
    }
}
