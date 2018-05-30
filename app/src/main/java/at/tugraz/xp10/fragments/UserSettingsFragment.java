package at.tugraz.xp10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.firebase.Users;
import at.tugraz.xp10.firebase.UsersValueEventListener;
import at.tugraz.xp10.model.User;

public class UserSettingsFragment extends Fragment {

    private Users mUsersFBHandle;
    private String mCurrentUserID;
    private User mCurrentUser;
    private View mView;
    private Switch mEmailNotifications;


    public UserSettingsFragment() {
        mUsersFBHandle = new Users();
    }

    public static UserSettingsFragment newInstance() {
        UserSettingsFragment fragment = new UserSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentUserID = mUsersFBHandle.getCurrentUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_setting, container, false);
        mEmailNotifications = mView.findViewById(R.id.email_notifications);
        mUsersFBHandle.getUser(mCurrentUserID, new UsersValueEventListener() {
            @Override
            public void onNewData(HashMap<String, User> data) {
                mCurrentUser = data.get(mCurrentUserID);
                mEmailNotifications.setChecked(mCurrentUser.getEmailNotifications());
            }
        });

        mEmailNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               mCurrentUser.setEmailNotifications(isChecked);
               mUsersFBHandle.setUser(mCurrentUserID, mCurrentUser);
           }
       });

        return mView;
    }


    }
