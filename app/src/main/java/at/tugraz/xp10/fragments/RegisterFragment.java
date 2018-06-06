package at.tugraz.xp10.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.tugraz.xp10.R;
import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.LoginValueEventListener;

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private EditText mInputFirstName;
    private EditText mInputLastName;
    private EditText mInputEmail;
    private EditText mInputPassword;
    private EditText mInputConfirmPassword;
    private Button mButtonCancel;
    private Button mButtonRegister;

    private Login mLogin;

    public RegisterFragment() {
        mLogin = new Login();
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
                    mLogin.createUserWithEmailAndPassword(
                            mInputEmail.getText().toString(),
                            mInputPassword.getText().toString(),
                            mInputFirstName.getText().toString(),
                            mInputLastName.getText().toString(),
                            new LoginValueEventListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getActivity(), "Registration successful.\nPlease confirm email address", Toast.LENGTH_LONG).show();
                                    getFragmentManager().popBackStack();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getActivity(), "Registration failed.\n" + e.getMessage(), Toast.LENGTH_LONG).show();
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
    }

    private Boolean validate() {
        Boolean error = false;
        String firstName = mInputFirstName.getText().toString();
        String lastName = mInputLastName.getText().toString();
        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();
        String confirmPassword = mInputConfirmPassword.getText().toString();

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
}
