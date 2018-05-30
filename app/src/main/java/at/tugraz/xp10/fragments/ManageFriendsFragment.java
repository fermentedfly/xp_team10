package at.tugraz.xp10.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pchmn.materialchips.ChipView;

import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.firebase.UserListValueEventListener;
import at.tugraz.xp10.firebase.Users;
import at.tugraz.xp10.model.User;

import static at.tugraz.xp10.util.Constants.FRIENDS_REQUEST_CONFIRMED;
import static at.tugraz.xp10.util.Constants.FRIEND_REQUEST_ISSUED;
import static at.tugraz.xp10.util.Constants.FRIEND_REQUEST_PENDING;

final class UserChip extends ChipView {

    private String uid;
    private User user;

    public UserChip(Context context) {
        super(context);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


public class ManageFriendsFragment extends Fragment {
    private final Users mUsersDB;
    private HashMap<String, User> mContactList;
    private User mCurrentUser;
    private EditText mEmailAddress;
    private Button mAddButton;
    private View mView;
    private LinearLayout mFriendsView;
    private LinearLayout mPendingView;

    public ManageFriendsFragment() {
        mUsersDB = new Users();
    }

    public static ManageFriendsFragment newInstance() {
        ManageFriendsFragment fragment = new ManageFriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_manage_friends, container, false);

        mFriendsView = mView.findViewById(R.id.chipview_friends);
        mPendingView = mView.findViewById(R.id.chipview_pending);

        mEmailAddress = mView.findViewById(R.id.textedit_friend_email);
        mEmailAddress.setEnabled(false);

        mAddButton = mView.findViewById(R.id.button_add_friend);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_user();
            }
        });

        load_list_of_users();

        return mView;
    }

    private void add_user()
    {
        String email = mEmailAddress.getText().toString();
        if(!email.isEmpty())
        {
            for(String uid: mContactList.keySet())
            {
                User user = mContactList.get(uid);
                if(user.geteMail().equals(email))
                {
                    if(!mCurrentUser.getFriends().containsKey(uid)) {
                        mCurrentUser.addFriend(uid);
                        user.addFriendRequest(mUsersDB.getCurrentUserID());
                        add_user_to_friend_view(uid, user, FRIEND_REQUEST_ISSUED);
                        mUsersDB.setUser(mUsersDB.getCurrentUserID(), mCurrentUser);
                        mUsersDB.setUser(uid, user);
                        mEmailAddress.setText("");
                    }
                    else
                    {
                        mEmailAddress.setError("User is already your friend");
                    }
                    return;
                }
            }
            mEmailAddress.setError(getString(R.string.invalid_email));
        }
    }

    private void add_user_to_friend_view(String uid, User user, String state)
    {
        UserChip chip = new UserChip(mView.getContext());
        chip.setLabel(user.getName());
        chip.setUid(uid);
        chip.setUser(user);
        if(state.equals(FRIENDS_REQUEST_CONFIRMED)) {
            chip.setLabelColor(getResources().getColor(R.color.colorPrimary));
            chip.setChipBackgroundColor(getResources().getColor(R.color.colorAccent));
            chip.setDeleteIconColor(getResources().getColor(R.color.colorPrimary));
        }
        chip.setPadding(2,2,2,2);
        chip.setDeletable(true);
        chip.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChip current_chip = (UserChip)(v.getParent().getParent().getParent());
                mCurrentUser.removeFriend(current_chip.getUid());
                current_chip.getUser().removeFriend(mUsersDB.getCurrentUserID());
                mFriendsView.removeView(current_chip);
                mUsersDB.setUser(mUsersDB.getCurrentUserID(), mCurrentUser);
                mUsersDB.setUser(current_chip.getUid(), current_chip.getUser());
            }
        });

        mFriendsView.addView(chip);
    }

    private void add_user_to_pending_view(String uid, User user)
    {
        UserChip chip = new UserChip(mView.getContext());
        chip.setLabel(user.getName());
        chip.setUid(uid);
        chip.setUser(user);
        chip.setPadding(2,2,2,2);
        chip.setDeletable(true);
        chip.setClickable(true);
        chip.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChip current_chip = (UserChip)(v.getParent().getParent().getParent());
                mCurrentUser.removeFriend(current_chip.getUid());
                current_chip.getUser().removeFriend(mUsersDB.getCurrentUserID());
                mPendingView.removeView(current_chip);
                mUsersDB.setUser(mUsersDB.getCurrentUserID(), mCurrentUser);
                mUsersDB.setUser(current_chip.getUid(), current_chip.getUser());
            }
        });
        chip.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChip current_chip = (UserChip)(v.getParent().getParent());
                mCurrentUser.confirmFriend(current_chip.getUid());
                current_chip.getUser().confirmFriend(mUsersDB.getCurrentUserID());
                mUsersDB.setUser(mUsersDB.getCurrentUserID(), mCurrentUser);
                mUsersDB.setUser(current_chip.getUid(), current_chip.getUser());
                mPendingView.removeView(current_chip);
                add_user_to_friend_view(current_chip.getUid(), current_chip.getUser(), FRIENDS_REQUEST_CONFIRMED);
            }
        });

        mPendingView.addView(chip);
    }

    private void load_list_of_users()
    {
        mUsersDB.getUsers(new UserListValueEventListener() {
            @Override
            public void onNewData(HashMap<String, User> data) {
                mFriendsView.removeAllViews();
                mPendingView.removeAllViews();
                mContactList = data;
                mCurrentUser = data.get(mUsersDB.getCurrentUserID());

                for(String uid: mCurrentUser.getFriends().keySet())
                {
                    if(mContactList.containsKey(uid)) {
                        String state = mCurrentUser.getFriends().get(uid);
                        if(state.equals(FRIEND_REQUEST_PENDING))
                        {
                            add_user_to_pending_view(uid, mContactList.get(uid));
                        }
                        else
                        {
                            add_user_to_friend_view(uid, mContactList.get(uid), state);
                        }
                    }
                }
                mEmailAddress.setEnabled(true);
                mAddButton.setEnabled(true);
            }
        });
    }
}
