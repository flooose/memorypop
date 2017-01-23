package de.jjff.flooose.memorypop.services;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import de.jjff.flooose.memorypop.R;
import de.jjff.flooose.memorypop.SigninOrUpActivity;

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;


public class LoginService {

    private String email;

    @Inject
    public LoginService() {
    }

    public void login(final OnCompleteListener mActivity, String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity);
    }
}
