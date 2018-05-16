package at.tugraz.xp10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.User;

public class UserSettingsFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private String mCurrentUserID;
    private User mCurrentUser;
    private View mView;
    private Switch mEmailNotifications;


    public UserSettingsFragment() {
    }

    public static UserSettingsFragment newInstance() {
        UserSettingsFragment fragment = new UserSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mCurrentUserID = mAuth.getCurrentUser().getUid();
        mUserRef = mDatabase.getReference("/users/").child(mCurrentUserID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user_setting, container, false);

        mEmailNotifications = mView.findViewById(R.id.email_notifications);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentUser = dataSnapshot.getValue(User.class);
                mEmailNotifications.setChecked(mCurrentUser.getEmailNotifications());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mEmailNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             mUserRef.child("emailNotifications").setValue(isChecked);
           }
       });

        return mView;
    }


    }
