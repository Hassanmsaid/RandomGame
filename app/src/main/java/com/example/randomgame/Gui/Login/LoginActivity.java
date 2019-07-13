package com.example.randomgame.Gui.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomgame.Gui.CreateAccount.CreateAccountActivity;
import com.example.randomgame.Gui.HomeActivity;
import com.example.randomgame.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.randomgame.Utils.CommonMethods.showToast;

public class LoginActivity extends AppCompatActivity implements IloginView {

    private static final String TAG = "LoginActivity";
    private final static int RC_SIGN_IN = 123;
    Locale myLocale;

    @BindView(R.id.login_email_ET)
    EditText loginEmailET;
    @BindView(R.id.login_password_ET)
    EditText loginPasswordET;
    @BindView(R.id.create_acc_TV)
    TextView createAccTV;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.login_google_btn)
    ImageView loginGoogleBtn;
//    @BindView(R.id.login_facebook_btn)
//    ImageView loginFacebookBtn;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private LoginPresenter presenter;
    private FirebaseUser currentUser;
    GoogleSignInClient mgoogleSignInClient;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = getSharedPreferences("lang_pref", MODE_PRIVATE);
        String currentLanguage = preferences.getString("current_lang", "en");
        setLocale(currentLanguage);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        printHashKey(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

        presenter = new LoginPresenter(this, this);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progress);

        //Facebook
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_facebook_btn);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                showToast(LoginActivity.this, "success");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                showToast(LoginActivity.this, "canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                showToast(LoginActivity.this, error.toString());
            }
        });
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void signInGoogle() {
        loading();
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
//                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                showToast(this, e.toString());
                Log.w(TAG, "Google sign in failed", e);
            }
        }

        //Facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
//                            loginSuccess();
                            currentUser = mAuth.getCurrentUser();
//                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                            Toast.makeText(LoginActivity.this, currentUser.getDisplayName() + " " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                            showToast(LoginActivity.this, "Welcome back " + currentUser.getDisplayName() + " " + currentUser.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("lang_pref", MODE_PRIVATE);
        String currentLanguage = preferences.getString("current_lang", "en");
        setLocale(currentLanguage);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    @OnClick({R.id.create_acc_TV, R.id.login_btn, R.id.login_google_btn})
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
//                    Toast.makeText(this, "Check email & password then try again", Toast.LENGTH_SHORT).show();
                    showToast(this, "Check email & password then try again");
                }
                break;
            case R.id.login_google_btn:
                signInGoogle();
                break;
//            case R.id.login_facebook_btn:
//                // TODO add facebook signin
//                break;
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
//        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        showToast(this, "Authentication failed.");
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setLocale(String localeName) {
        myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


}


