package at.tugraz.xp10.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import at.tugraz.xp10.R;
import at.tugraz.xp10.firebase.Login;
import at.tugraz.xp10.firebase.LoginValueEventListener;


public class ForgotPasswordDialogFragment extends DialogFragment {
    private static final String TAG = "ForgotPasswordDialog";
    private static final String ARG_EMAIL = "email";

    private String mEmail;
    private EditText mEmailEditText;
    private Login mLogin;

    public ForgotPasswordDialogFragment() {
        mLogin = new Login();
    }

    public static ForgotPasswordDialogFragment newInstance(String email) {
        ForgotPasswordDialogFragment fragment = new ForgotPasswordDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();


        final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setView(inflater.inflate(R.layout.fragment_forgot_password, null))
                .setTitle("Forgot Password")
                .setPositiveButton(R.string.submit, null) //Set to null. We override the onclick
                .setNegativeButton(R.string.cancel, null)
                .create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                mEmailEditText = (EditText) getDialog().findViewById(R.id.email_address);
                mEmailEditText.setText(mEmail);

                // POSITIVE
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (mEmailEditText.getText().toString().isEmpty()) {
                            mEmailEditText.setError(getString(R.string.error_field_required));
                            return;
                        }
                        mLogin.setForgotPasswordMail(mEmailEditText.getText().toString(),
                                new LoginValueEventListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getContext(), "Mail send successfully!", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                mEmailEditText.setError(e.getMessage());
                                Toast.makeText(getContext(), "Error. Please try again!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                // POSITIVE
                Button buttonNeg = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonNeg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        ForgotPasswordDialogFragment.this.getDialog().cancel();
                    }
                });
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmailEditText = (EditText) view.findViewById(R.id.email_address);

        mEmailEditText.setText(mEmail);
    }
}
