package de.jjff.flooose.memorypop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import de.jjff.flooose.memorypop.daggerstuff.DaggerLoginComponent;
import de.jjff.flooose.memorypop.daggerstuff.LoginComponent;
import de.jjff.flooose.memorypop.daggerstuff.LoginModule;
import de.jjff.flooose.memorypop.services.LoginService;

import static de.jjff.flooose.memorypop.MemoryPopApplication.LOG_TAG;

public class SigninOrUpActivity extends AppCompatActivity implements OnCompleteListener<AuthResult> {

    LoginComponent component;
    @Inject
    LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_or_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //component = DaggerLoginComponent.builder().loginModule(new LoginModule(this)).build();
        component = DaggerLoginComponent.create();
    }

    public void signIn(View view) {
        EditText signInView = (EditText) findViewById(R.id.email_address);
        EditText passwordView = (EditText) findViewById(R.id.password);

        component.loginService().login(this, signInView.getText().toString(), passwordView.getText().toString());
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

        // If sign in fails, display a message to the user. If sign in succeeds
        // the auth state listener will be notified and logic to handle the
        // signed in user can be handled in the listener.
        if (!task.isSuccessful()) {
            Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
            Toast.makeText(SigninOrUpActivity.this, R.string.auth_failed,
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(SigninOrUpActivity.this, R.string.auth_succeeded,
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        // ...
    }

}
