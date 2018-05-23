package at.tugraz.xp10;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import at.tugraz.xp10.fragments.ForgotPasswordDialogFragment;
import at.tugraz.xp10.fragments.RegisterFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ForgotPasswordDialogFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mProgressViewPlaceholder;
    private View mLoginFormView;



    public LoginActivity() {
        mAuth = FirebaseAuth.getInstance();
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

        FirebaseUser currentUser = null;
        if(mAuth != null)
            currentUser = mAuth.getCurrentUser();

        if(currentUser != null && (currentUser.isEmailVerified() || currentUser.getEmail().equals(getString(R.string.admin_xp10_com))))
            gotoMainPage();


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    protected Boolean attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            signInWithUserAndPassword(email, password);
            return true;
        }
    }

    public void signInWithUserAndPassword(String email, String password) {
        showProgress(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        showProgress(false);

                        if (user.isEmailVerified() || user.getEmail().equals(getString(R.string.admin_xp10_com)))
                            gotoMainPage();
                        else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.\n" + "Email address is not verified",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Authentication failed.\n" + e.getMessage(),
                                Toast.LENGTH_LONG).show();
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
        return mAuth.getCurrentUser() != null;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        // nothing yet
    }


    public EditText getmEmailView() {
        return mEmailView;
    }

    public void setmEmailView(EditText mEmailView) {
        this.mEmailView = mEmailView;
    }

    public EditText getmPasswordView() {
        return mPasswordView;
    }

    public void setmPasswordView(EditText mPasswordView) {
        this.mPasswordView = mPasswordView;
    }
}

