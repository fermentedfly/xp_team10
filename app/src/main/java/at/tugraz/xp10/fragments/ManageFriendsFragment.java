package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pchmn.materialchips.ChipView;

import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.User;

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
    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private HashMap<String, User> mContactList;
    private User mCurrentUser;
    private EditText mEmailAddress;
    private Button mAddButton;
    private View mView;
    private LinearLayout mFriendsView;
    private LinearLayout mPendingView;
    private String mCurrentUserID;

    public ManageFriendsFragment() {
    }

    public static ManageFriendsFragment newInstance() {
        ManageFriendsFragment fragment = new ManageFriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("/users");
        mCurrentUserID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                        user.addFriendRequest(mCurrentUserID);
                        add_user_to_friend_view(uid, user, "Issued");
                        save_to_database(mCurrentUserID, mCurrentUser);
                        save_to_database(uid, user);
                    }
                    return;
                }
            }
            mEmailAddress.setError(getString(R.string.invalid_email));
        }
    }

    private void save_to_database(String uid, User user)
    {
        mUserRef.child(uid).setValue(user);
    }

    private void add_user_to_friend_view(String uid, User user, String state)
    {
        UserChip chip = new UserChip(mView.getContext());
        chip.setLabel(user.getName());
        chip.setUid(uid);
        chip.setUser(user);
        if(state.equals("Confirmed")) {
            chip.setLabelColor(getResources().getColor(R.color.colorWhite));
            chip.setChipBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        chip.setPadding(2,2,2,2);
        chip.setDeletable(true);
        chip.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChip current_chip = (UserChip)(v.getParent().getParent().getParent());
                mCurrentUser.removeFriend(current_chip.getUid());
                current_chip.getUser().removeFriend(mCurrentUserID);
                mFriendsView.removeView(current_chip);
                save_to_database(mCurrentUserID, mCurrentUser);
                save_to_database(current_chip.getUid(), current_chip.getUser());
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
                current_chip.getUser().removeFriend(mCurrentUserID);
                mPendingView.removeView(current_chip);
                save_to_database(mCurrentUserID, mCurrentUser);
                save_to_database(current_chip.getUid(), current_chip.getUser());
            }
        });
        chip.setOnChipClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChip current_chip = (UserChip)(v.getParent().getParent());
                mCurrentUser.confirmFriend(current_chip.getUid());
                current_chip.getUser().confirmFriend(mCurrentUserID);
                save_to_database(mCurrentUserID, mCurrentUser);
                save_to_database(current_chip.getUid(), current_chip.getUser());
                mPendingView.removeView(current_chip);
                add_user_to_friend_view(current_chip.getUid(), current_chip.getUser(), "Confirmed");
            }
        });

        mPendingView.addView(chip);
    }

    private void load_list_of_users()
    {
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mContactList = new HashMap<String, User>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    mContactList.put(ds.getKey(), user);

                    if(ds.getKey().equals(mCurrentUserID))
                    {
                        mCurrentUser = user;
                    }
                }

                for(String uid: mCurrentUser.getFriends().keySet())
                {
                    if(mContactList.containsKey(uid)) {
                        String state = mCurrentUser.getFriends().get(uid);
                        if(state.equals("Pending"))
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        void onFragmentInteraction(Uri uri);
    }
}
