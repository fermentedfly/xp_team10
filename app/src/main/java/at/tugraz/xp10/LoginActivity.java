package at.tugraz.xp10;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.LoginValueEventListener;
import at.tugraz.xp10.fragments.ForgotPasswordDialogFragment;
import at.tugraz.xp10.fragments.RegisterFragment;
import at.tugraz.xp10.model.User;

public class LoginActivity extends AppCompatActivity {

//    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mProgressViewPlaceholder;
    private View mLoginFormView;
    private View mFocusView;
    private Login mLogin;

    public LoginActivity() {
        mLogin = new Login();
//        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        Button mLoginButtom = findViewById(R.id.login_button);
        mLoginButtom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mForgotPasswordButton = findViewById(R.id.forgot_password_button);
        mForgotPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForgotPassword();
            }
        });

        Button mRegisterButton = findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressViewPlaceholder = findViewById(R.id.login_progress);
        mProgressView = findViewById(R.id.login_progress2);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mLogin.getCurrentUser() != null && (mLogin.getCurrentUser().isEmailVerified() || mLogin.getCurrentUser().getEmail().equals(getString(R.string.admin_xp10_com))))
            gotoMainPage();


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    protected Boolean attemptLogin() {

        // Reset errors.
        resetErrors();

        // Store values at the time of the login attempt.
        String email = getmEmail();
        String password = getmPassword();

        boolean cancel = false;
        mFocusView = null;

        // Check for a valid password, if the user entered one.
        if ((password == null) || password.isEmpty())
        {
            setmEditTextError(mPasswordView, getString(R.string.error_field_required));
            setFocus(mPasswordView);
            cancel = true;
        }else if( !isPasswordValid(password)) {
            setmEditTextError(mPasswordView, getString(R.string.error_invalid_password));
            setFocus(mPasswordView);
            cancel = true;
        }

        if((email == null) || email.isEmpty())
        {
            setmEditTextError(mEmailView, getString(R.string.error_field_required));
            setFocus(mEmailView);
            cancel = true;
        } else if (!isEmailValid(email)) {
            setmEditTextError(mEmailView, getString(R.string.error_invalid_email));
            setFocus(mEmailView);
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            signInWithUserAndPassword(email, password);
            return true;
        }
    }

    protected void requestFocus() {
        mFocusView.requestFocus();
    }

    protected void resetErrors() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
    }

    public void signInWithUserAndPassword(String email, String password) {
        showProgress(true);
        mLogin.signInWithUserAndPassword(email, password, new LoginValueEventListener() {

            @Override
            public void onSuccess() {

                Log.d(TAG, "signInWithEmail:success");
                showProgress(false);

                if (mLogin.getCurrentUser().isEmailVerified() || mLogin.getCurrentUser().getEmail().equals(getString(R.string.admin_xp10_com)))
                    gotoMainPage();
                else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.\n" + "Email address is not verified",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(LoginActivity.this, "Authentication failed.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
            }

        });
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private void goToForgotPassword() {
        String email = mEmailView.getText().toString();

        FragmentManager fragmentManager = getSupportFragmentManager();
        ForgotPasswordDialogFragment newFragment = ForgotPasswordDialogFragment.newInstance(email);

        newFragment.show(fragmentManager, "dialog");

    }
    private void goToRegister() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RegisterFragment newFragment = RegisterFragment.newInstance();

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();

    }

    private void gotoMainPage() {
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        LoginActivity.this.startActivity(myIntent);
    }

    public boolean isUserLoggedIn() {
        return mLogin.isUserLoggedIn();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        startShowProgress(!show, shortAnimTime, mLoginFormView);
        startShowProgress(show, shortAnimTime, mProgressView);
        startShowProgress(show, shortAnimTime, mProgressViewPlaceholder);
    }

    private void startShowProgress(final boolean show, int shortAnimTime, final View viewProgress) {
        viewProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        viewProgress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public String getmEmail() {
        return mEmailView.getText().toString();
    }

    public String getmPassword() {
        return mPasswordView.getText().toString();
    }

    public void setmEditTextError(EditText e, String error) {
        e.setError(error);
    }

    public void setFocus(View v)
    {
        mFocusView = v;
    }
}

