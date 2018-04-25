package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.tugraz.xp10.LoginActivity;
import at.tugraz.xp10.R;
import at.tugraz.xp10.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private OnFragmentInteractionListener mListener;
    private EditText mInputFirstName;
    private EditText mInputLastName;
    private EditText mInputEmail;
    private EditText mInputPassword;
    private EditText mInputConfirmPassword;
    private Button mButtonCancel;
    private Button mButtonRegister;
    private DatabaseReference mDatabase;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {

                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(mInputEmail.getText().toString(), mInputPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        currentUser.sendEmailVerification();

                                        User user = new User(mInputEmail.getText().toString(),
                                                mInputFirstName.getText().toString(),
                                                mInputLastName.getText().toString());

                                        mDatabase.child("users").child(currentUser.getUid()).setValue(user);

                                        Toast.makeText(getActivity(), "Registration successful.\nPlease confirm email address",
                                                Toast.LENGTH_LONG).show();
                                        getFragmentManager().popBackStack();

//                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                        DatabaseReference myRef = database.getReference();
//
//                                        myRef.child("Users").push().setValue(new User("hallo@test.com", "1"));
//


                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Registration failed.\n" + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInputFirstName = view.findViewById(R.id.register_firstname);
        mInputLastName = view.findViewById(R.id.register_lastname);
        mInputEmail = view.findViewById(R.id.register_email);
        mInputPassword = view.findViewById(R.id.register_password);
        mInputConfirmPassword = view.findViewById(R.id.register_confirm_password);
        mButtonCancel = view.findViewById(R.id.register_cancel_button);
        mButtonRegister = view.findViewById(R.id.register_register_button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private Boolean validate() {
        Boolean error = false;
        String firstName = mInputFirstName.getText().toString();
        String lastName = mInputLastName.getText().toString();
        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();
        String confirmPassword = mInputConfirmPassword.getText().toString();

        // reset all errors
        mInputFirstName.setError(null);
        mInputLastName.setError(null);
        mInputEmail.setError(null);
        mInputPassword.setError(null);
        mInputConfirmPassword.setError(null);

        if (firstName.isEmpty()) {
            mInputFirstName.setError(getString(R.string.error_field_required));
            error = true;
        }

        if (lastName.isEmpty()) {
            mInputLastName.setError(getString(R.string.error_field_required));
            error = true;
        }

        if (email.isEmpty()) {
            mInputEmail.setError(getString(R.string.error_field_required));
            error = true;
        }

        if (!email.contains("@")) {
            // TODO better validation
            mInputEmail.setError(getString(R.string.error_invalid_email));
            error = true;
        }

        if (password.isEmpty()) {
            mInputPassword.setError(getString(R.string.error_field_required));
            error = true;
        }

        if (!confirmPassword.equals(password)) {
            mInputConfirmPassword.setError(getString(R.string.error_password_not_equal));
            error = true;
        }


        return !error;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
