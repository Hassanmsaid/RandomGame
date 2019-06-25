package com.example.randomgame.Gui.CreateAccount;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountPresenter {

    ICreateAccountView view;
    Context context;

    public CreateAccountPresenter(ICreateAccountView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void createAccEmailPassword(String email, String pw, FirebaseAuth mAuth, Context context) {
        view.loading();
        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            view.createAccSuccess();
                        } else {
                            view.createAccFailed();
                        }
                    }
                });
    }
}
