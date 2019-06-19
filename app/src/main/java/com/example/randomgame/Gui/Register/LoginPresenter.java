package com.example.randomgame.Gui.Register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginPresenter {
    IloginView view;
    Context context;

    public LoginPresenter(IloginView view, Context context) {
        this.view = view;
        this.context = context;
    }

    void login(String email, String pw, final FirebaseAuth mAuth, Context context) {
        view.loading();
        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            view.loginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            view.loginFailed();
                        }
                    }
                });
    }
}
