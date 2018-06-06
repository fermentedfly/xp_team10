package at.tugraz.xp10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import at.tugraz.xp10.R;
import at.tugraz.xp10.firebase.DatabaseValueEventListener;
import at.tugraz.xp10.firebase.Users;
import at.tugraz.xp10.model.ModelBase;
import at.tugraz.xp10.model.User;

public class UserSettingsFragment extends Fragment {

    private Users mUsersFBHandle;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_setting, container, false);
        mEmailNotifications = mView.findViewById(R.id.email_notifications);
        mUsersFBHandle.getCurrentUser(new DatabaseValueEventListener() {
            @Override
            public <T extends ModelBase> void onNewData(T data) {
                mCurrentUser = (User)data;
                mEmailNotifications.setChecked(mCurrentUser.getEmailNotifications());
            }
        });

        mEmailNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               mCurrentUser.setEmailNotifications(isChecked);
               mUsersFBHandle.setCurrentUser(mCurrentUser);
           }
       });

        return mView;
    }


    }
