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

import java.util.ArrayList;
import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.User;

final class UserChip extends ChipView {

    private String uid;

    public UserChip(Context context) {
        super(context);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
    private LinearLayout mChipContainer;

    public ManageFriendsFragment() {
        // Required empty public constructor
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_manage_friends, container, false);

        mChipContainer = mView.findViewById(R.id.chipview_friends);

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
            add_user_to_friends(email);
        }
    }

    private void add_user_to_friends(String email)
    {
        for(String uid: mContactList.keySet())
        {
            User user = mContactList.get(uid);
            if((user.geteMail().equals(email)) && (!mCurrentUser.getFriends().containsKey(uid)))
            {
                mCurrentUser.addFriend(uid);
                add_friend_to_view(uid, user);
                save_to_database();
                return;
            }
        }
        mEmailAddress.setError(getString(R.string.invalid_email));
    }

    private void save_to_database()
    {
        mUserRef.child(mAuth.getCurrentUser().getUid()).setValue(mCurrentUser);
    }

    private void add_friend_to_view(String uid, User user)
    {
        UserChip chip = new UserChip(mView.getContext());
        chip.setLabel(user.getFirstName());
        chip.setUid(uid);
        chip.setPadding(2,2,2,2);
        chip.setDeletable(true);
        chip.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserChip current_chip = (UserChip)(v.getParent().getParent().getParent());
                mCurrentUser.removeFriend(current_chip.getUid());
                mChipContainer.removeView(current_chip);
                save_to_database();
            }
        });

        mChipContainer.addView(chip);

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

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid()))
                    {
                        mCurrentUser = user;
                    }
                }

                for(String uid: mCurrentUser.getFriends().keySet())
                {
                    if(mContactList.containsKey(uid)) {
                        add_friend_to_view(uid, mContactList.get(uid));
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
