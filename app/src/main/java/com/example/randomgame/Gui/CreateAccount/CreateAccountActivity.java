package com.example.randomgame.Gui.CreateAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.randomgame.Gui.HomeActivity;
import com.example.randomgame.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.randomgame.Utils.CommonMethods.showToast;

public class CreateAccountActivity extends AppCompatActivity implements ICreateAccountView {

    private static final String TAG = "CreateAccountActivity";

    @BindView(R.id.name_ET)
    EditText nameET;
    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.password_ET)
    EditText passwordET;
    @BindView(R.id.confirm_password_ET)
    EditText confirmPasswordET;
    @BindView(R.id.phone_ET)
    EditText phoneET;
    @BindView(R.id.country_ET)
    EditText countryET;
    @BindView(R.id.create_acc_btn)
    Button createAccBtn;
    @BindView(R.id.create_acc_progress)
    SpinKitView createAccProgress;

    FirebaseUser currentUser;
    CreateAccountPresenter presenter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();



        presenter = new CreateAccountPresenter(this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        currentUser = mAuth.getCurrentUser();
    }

    @OnClick({R.id.create_acc_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_acc_btn:

                String name, email, pw, confirmPw, phone, country;
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                pw = passwordET.getText().toString();
                confirmPw = confirmPasswordET.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pw) && !TextUtils.isEmpty(confirmPw)
                        && pw.equals(confirmPw)) {
                    presenter.createAccEmailPassword(email, pw, mAuth, this);
                }
                break;
        }
    }







    private void printKeyHash() {
//        PackageInfo info =
    }

    @Override
    public void createAccSuccess() {
        Log.d(TAG, "createUserWithEmail:success");
//        Toast.makeText(CreateAccountActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
        showToast(this, "Account Created.");
        FirebaseUser user = mAuth.getCurrentUser();
        createAccProgress.setVisibility(View.GONE);
        startActivity(new Intent(CreateAccountActivity.this, HomeActivity.class));
    }

    @Override
    public void createAccFailed() {
//        Log.w(TAG, "createUserWithEmail:failure", task.getException());
        createAccProgress.setVisibility(View.GONE);
//        Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        showToast(this, "Authentication failed.");
    }

    @Override
    public void loading() {
        createAccProgress.setVisibility(View.VISIBLE);
    }
}
