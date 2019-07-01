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

public class CreateAccountActivity extends AppCompatActivity implements ICreateAccountView {

    private static final String TAG = "CreateAccountActivity";
    private final static int RC_SIGN_IN = 123;
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
    @BindView(R.id.create_acc_google_btn)
    ImageView createAccGoogleBtn;
    @BindView(R.id.create_acc_progress)
    SpinKitView createAccProgress;

    GoogleSignInClient mgoogleSignInClient;
    GoogleApiClient googleApiClient;
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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

        presenter = new CreateAccountPresenter(this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        currentUser = mAuth.getCurrentUser();
    }

    @OnClick({R.id.create_acc_btn, R.id.create_acc_google_btn})
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

            case R.id.create_acc_google_btn:
                signInGoogle();
                break;
        }
    }

    public void createAccEmailPassword(String email, String pw) {
        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreateAccountActivity.this, "Account Created.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            createAccProgress.setVisibility(View.GONE);
                            startActivity(new Intent(CreateAccountActivity.this, HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            createAccProgress.setVisibility(View.GONE);
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInGoogle() {
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(CreateAccountActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
//                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(CreateAccountActivity.this, HomeActivity.class));
                            Toast.makeText(CreateAccountActivity.this, currentUser.getDisplayName() + " " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void printKeyHash() {
//        PackageInfo info =
    }

    @Override
    public void createAccSuccess() {
        Log.d(TAG, "createUserWithEmail:success");
        Toast.makeText(CreateAccountActivity.this, "Account Created.",
                Toast.LENGTH_SHORT).show();
        FirebaseUser user = mAuth.getCurrentUser();
        createAccProgress.setVisibility(View.GONE);
        startActivity(new Intent(CreateAccountActivity.this, HomeActivity.class));
    }

    @Override
    public void createAccFailed() {
//        Log.w(TAG, "createUserWithEmail:failure", task.getException());
        createAccProgress.setVisibility(View.GONE);
        Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loading() {
        createAccProgress.setVisibility(View.VISIBLE);
    }
}
