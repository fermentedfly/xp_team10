package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.ShoppingList;
import at.tugraz.xp10.model.User;
import at.tugraz.xp10.util.Constants;


public class ListSettingFragment extends Fragment  {
    private static final String ARG_LIST_ID = "ID";
    private static final String TAG = "ListSetting";

    private String mListID;
    private FirebaseAuth mAuth;

    private DatabaseReference mUserRef;
    private DatabaseReference mShoppingListRef;

    private List<Chip> mContactList;

    private OnFragmentInteractionListener mListener;

    private Button mSaveButton;
    private Button mCancelButton;

    private EditText mTitle;
    private EditText mDescription;
    private ChipsInput mUsers;
    private HashMap<String, User> mUserList;
    private User mCurrentUser;
    private User owner;
    private String ownerID;

    public ListSettingFragment() {
        // Required empty public constructor
    }

    public static ListSettingFragment newInstance(String list_id) {
        ListSettingFragment fragment = new ListSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIST_ID, list_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListID = getArguments().getString(ARG_LIST_ID);
        }
        else {
            mListID = null;
        }
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("/users");
        mShoppingListRef = mDatabase.getReference("/shoppinglists");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_setting, container, false);

        mTitle = view.findViewById(R.id.list_setting_title);
        mDescription = view.findViewById(R.id.list_setting_description);

        mSaveButton = view.findViewById(R.id.list_setting_save);
        mSaveButton.setEnabled(false);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToDatabase();
            }
        });

        mCancelButton = view.findViewById(R.id.list_setting_cancel);
        mCancelButton.setEnabled(false);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        loadFriends();

        return view;
    }

    private void load_list_by_id()
    {
        mShoppingListRef.child(mListID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShoppingList list = dataSnapshot.getValue(ShoppingList.class);
                for(Map.Entry<String, String> entry: list.getMembers().entrySet())
                {
                    if(entry.getValue().equals(Constants.OWNER))
                    {
                        owner = mUserList.get(entry.getKey());
                        ownerID = entry.getKey();
                    }
                }
                fill_ui_fields(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fill_ui_fields(ShoppingList list)
    {
        for(Map.Entry<String, User>entry : mUserList.entrySet())
        {
            if(mCurrentUser.getFriends().containsKey(entry.getKey())) {
                addUserToChip(entry.getValue(), entry.getKey());
            }
        }

        Fragment current = getFragmentManager().findFragmentByTag("ListSetting");
        View v = current.getView();
        if(v != null) {
            ChipView ownerChipView = v.findViewById(R.id.owner_chip_view);
            ownerChipView.setLabel(owner.getName());
            ownerChipView.setHasAvatarIcon(true);

            mUsers = new ChipsInput(getContext());
            mUsers.setChipDeletable(true);
            mUsers.setMaxRows(10);
            mUsers.setMaxHeight(ViewUtil.dpToPx(400)+8);

            LinearLayout ll = v.findViewById(R.id.list_setting_chips_input);
            ll.addView(mUsers);

            mUsers.setFilterableList(mContactList);

            if (list != null) {
                mTitle.setText(list.getTitle());
                mDescription.setText(list.getDescription());
                for (Chip chip : mContactList) {
                    if (list.getMembers().keySet().contains(chip.getId())) {
                        mUsers.addChip(chip);
                    }
                }
            }
        }

        mSaveButton.setEnabled(true);
        mCancelButton.setEnabled(true);
    }

    private void loadFriends() {
        mUserList = new HashMap<>();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mContactList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    mUserList.put(ds.getKey(), user);

                    if(ds.getKey().equals(mAuth.getCurrentUser().getUid()))
                    {
                        mCurrentUser = user;
                    }
                }

                if (mListID != null) {
                    load_list_by_id();
                }
                else {
                    owner = mCurrentUser;
                    ownerID = mAuth.getCurrentUser().getUid();
                    fill_ui_fields(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void saveDataToDatabase() {

        if(TextUtils.isEmpty(mTitle.getText().toString())) {
            mTitle.setError(getString(R.string.error_field_required));
            return;
        }

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setTitle(mTitle.getText().toString());
        if(!mDescription.getText().toString().isEmpty())
            shoppingList.setDescription(mDescription.getText().toString());

        Map<String, String> users = new HashMap<String, String>();
        List<Chip> selectedUsers = (List<Chip>) mUsers.getSelectedChipList();
        
        for(Chip selUser : selectedUsers) {
            users.put(selUser.getId().toString(), Constants.MEMBER);
        }

        users.put(ownerID,Constants.OWNER);

        if (mAuth.getCurrentUser().getUid() != ownerID)
        {
            users.put(mAuth.getCurrentUser().getUid(), Constants.MEMBER);
        }

        shoppingList.setMembers(users);

        if (mListID == null) {
            mListID = mShoppingListRef.push().getKey();
        }

        mShoppingListRef.child(mListID).setValue(shoppingList);

        for (String uid : shoppingList.getMembers().keySet()) {
            Map<String, Object> newList = new HashMap<>();
            newList.put(mListID, true);
            mUserRef.child(uid).child("shoppinglists").updateChildren(newList);
        }
        closeFragment();
    }

    private void addUserToChip(User user, String key) {
        if (!key.equals(mAuth.getCurrentUser().getUid()) && user != owner) {
            mContactList.add(new Chip(key, user.getFirstName() + " " + user.getLastName(), user.geteMail()));
        }
    }


    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStackImmediate();
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
        void onFragmentInteraction(Uri uri);
    }
}

