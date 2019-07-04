package com.example.randomgame.Gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.randomgame.Gui.Leaderboard.LeaderboardFragment;
import com.example.randomgame.Gui.Login.LoginActivity;
import com.example.randomgame.Gui.Splash.SplashActivity;
import com.example.randomgame.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.profile_btn)
    ImageView profileBtn;
    @BindView(R.id.lang_btn)
    ImageView langBtn;
    @BindView(R.id.leadrboard_btn)
    ImageView leaderboardBtn;
    @BindView(R.id.sound_btn)
    ImageView soundBtn;
    @BindView(R.id.points_btn)
    ImageView pointsBtn;
    @BindView(R.id.top_bar)
    LinearLayout topBar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.settings_btn)
    ImageView settingsBtn;
    @BindView(R.id.close_btn)
    ImageView closeBtn;

    Locale myLocale;
    String currentLanguage = "en";
    AlertDialog.Builder builder;
    HomeFragment homeFragment;
    public static MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        SharedPreferences preferences = getSharedPreferences("lang_pref", MODE_PRIVATE);
        currentLanguage = preferences.getString("current_lang", "en");
        switch (currentLanguage) {
            case "en":
                langBtn.setImageDrawable(getResources().getDrawable(R.drawable.english_icon));
                break;
            case "fr":
                langBtn.setImageDrawable(getResources().getDrawable(R.drawable.french_icon));
                break;
            case "ar":
                langBtn.setImageDrawable(getResources().getDrawable(R.drawable.arabic_icon));
                break;
            default:
                break;
        }

        if (SplashActivity.FIRST_TIME) {
            setLocale(currentLanguage);
            SplashActivity.FIRST_TIME = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (homeFragment.isVisible()) {
            if (currentLanguage.equals("ar")) {
                builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle(R.string.close_app)
                    .setMessage(R.string.close_sure)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .setIcon(R.drawable.ic_warning)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.profile_btn, R.id.lang_btn, R.id.leadrboard_btn, R.id.sound_btn, R.id.points_btn, R.id.container, R.id.settings_btn, R.id.close_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_btn:
                break;
            case R.id.lang_btn:
                switch (currentLanguage) {
                    case "en":
                        setLocale("fr");
                        langBtn.setImageDrawable(getResources().getDrawable(R.drawable.french_icon));
                        currentLanguage = "fr";
                        break;
                    case "fr":
                        setLocale("ar");
                        langBtn.setImageDrawable(getResources().getDrawable(R.drawable.arabic_icon));
                        currentLanguage = "ar";
                        break;
                    case "ar":
                        setLocale("en");
                        langBtn.setImageDrawable(getResources().getDrawable(R.drawable.english_icon));
                        currentLanguage = "en";
                        break;
                }
                break;
            case R.id.leadrboard_btn:
                openFragment(new LeaderboardFragment());
                break;
            case R.id.sound_btn:
                //mute audio
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);

                ///////////////////
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                } else {
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                }

                ///////////////////
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!audioManager.isStreamMute(stream)) {
                        savedStreamMuted = true;
                        audioManager.adjustStreamVolume(stream, AudioManager.ADJUST_MUTE, 0);
                    }
                } else {
                    audioManager.setStreamMute(stream, true);
                }*/
                break;
            case R.id.points_btn:
                break;
            case R.id.container:
                break;
            case R.id.settings_btn:
                break;
            case R.id.close_btn:
                if (currentLanguage.equals("ar")) {
                    builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle(R.string.logout)
                        .setMessage(R.string.logout_sure)
                        .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .setIcon(R.drawable.ic_warning)
                        .show();
                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }

    public void setLocale(String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        SharedPreferences.Editor editor = getSharedPreferences("lang_pref", MODE_PRIVATE).edit();
        editor.putString("current_lang", localeName);
        editor.apply();

        Intent refreshIntent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(refreshIntent);
        finish();
    }

    public void disableAllTouches() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableAllTouches() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
