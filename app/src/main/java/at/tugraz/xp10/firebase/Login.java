package at.tugraz.xp10.firebase;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import at.tugraz.xp10.model.User;

public class Login {

        private Users mUserDBRef;
        private FirebaseAuth mDBAuth;

        public Login() {
            mUserDBRef = new Users();
            mDBAuth = FirebaseAuth.getInstance();
        }

        public void setForgotPasswordMail(String email, final LoginValueEventListener listener) {
            Task<Void> task = mDBAuth.sendPasswordResetEmail(email);
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onSuccess();
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFailure(e);
                }
            });
        }

        public void createUserWithEmailAndPassword(final String email, String password, final String firstname, final String lastname, final LoginValueEventListener listener) {

            Task<AuthResult> task = mDBAuth.createUserWithEmailAndPassword(email, password);
            task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    FirebaseUser currentUser = mDBAuth.getCurrentUser();
                    if (currentUser != null) {
                        currentUser.sendEmailVerification();
                        User user = new User(email, firstname, lastname);
                        mUserDBRef.setUser(currentUser.getUid(), user);
                        listener.onSuccess();
                    }
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFailure(e);
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
