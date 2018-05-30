package at.tugraz.xp10.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.tugraz.xp10.model.User;

public class Login {

        private DatabaseReference mDBRef;
        private FirebaseAuth mDBAuth;

        public Login() {
            mDBRef = FirebaseDatabase.getInstance().getReference();
            mDBAuth = FirebaseAuth.getInstance();
        }

        public void setForgotPasswordMail(final EditText emailEditText, final Context context) {

            mDBAuth.sendPasswordResetEmail(emailEditText.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Mail send successfully!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    emailEditText.setError(e.getMessage());
                    Toast.makeText(context, "Error. Please try again!", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void createUserWithEmailAndPassword(final String email, String password, final String firstname, final String lastname, final Activity activity, final FragmentManager fragmentManager) {

            mDBAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser currentUser = mDBAuth.getCurrentUser();
                            if (currentUser != null) {
                                currentUser.sendEmailVerification();

                                User user = new User(email, firstname, lastname);

                                mDBRef.child("users").child(currentUser.getUid()).setValue(user);

                                Toast.makeText(activity, "Registration successful.\nPlease confirm email address", Toast.LENGTH_LONG).show();
                                fragmentManager.popBackStack();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Registration failed.\n" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });


        }

    public boolean isUserLoggedIn() {
        return mDBAuth.getCurrentUser() != null;
    }

    public void signInWithUserAndPassword(String email, String password, final LoginValueEventListener listener) {

            mDBAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                      listener.onFailure(e);
                    }
                });

    }

    public FirebaseUser getCurrentUser(){
            return mDBAuth.getCurrentUser();
    }

}
