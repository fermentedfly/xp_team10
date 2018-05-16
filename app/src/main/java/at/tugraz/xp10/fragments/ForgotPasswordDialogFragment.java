package at.tugraz.xp10.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.google.firebase.auth.FirebaseAuth;

import at.tugraz.xp10.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForgotPasswordDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForgotPasswordDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordDialogFragment extends DialogFragment {
    private static final String TAG = "ForgotPasswordDialog";

    private static final String ARG_EMAIL = "email";

    private String mEmail;
    private EditText mEmailEditText;

    private OnFragmentInteractionListener mListener;

    public ForgotPasswordDialogFragment() {
        // Required empty public constructor
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
        // Get the layout inflater
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

                        FirebaseAuth.getInstance().sendPasswordResetEmail(mEmailEditText.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Email send.");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mEmailEditText.setError(e.getMessage());
                                Log.d(TAG, "onFailure: " + e.getMessage());
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmailEditText = (EditText) view.findViewById(R.id.email_address);

        mEmailEditText.setText(mEmail);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
