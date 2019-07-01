package com.example.randomgame.Gui.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomgame.Gui.CreateAccount.CreateAccountActivity;
import com.example.randomgame.Gui.HomeActivity;
import com.example.randomgame.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements IloginView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.login_email_ET)
    EditText loginEmailET;
    @BindView(R.id.login_password_ET)
    EditText loginPasswordET;
    @BindView(R.id.create_acc_TV)
    TextView createAccTV;
    @BindView(R.id.login_btn)
    Button loginBtn;

    private FirebaseAuth mAuth;
    private LoginPresenter presenter;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new LoginPresenter(this, this);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    @OnClick({R.id.create_acc_TV, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_acc_TV:
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
                break;

            case R.id.login_btn:
                String email, pw;
                email = loginEmailET.getText().toString();
                pw = loginPasswordET.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pw)) {
                    presenter.login(email, pw, mAuth, this);
                } else {
                    Toast.makeText(this, "Check email & password then try again", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void loginSuccess() {
        Log.d(TAG, "signInWithEmail:success");
        Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
        currentUser = mAuth.getCurrentUser();
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    @Override
    public void loginFailed() {
//        Log.w(TAG, "signInWithEmail:failure", task.getException());
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loading() {
        progressBar.setVisibility(View.VISIBLE);
    }
}


