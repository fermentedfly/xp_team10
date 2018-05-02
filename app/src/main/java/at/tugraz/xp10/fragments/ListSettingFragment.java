package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListSettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSettingFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";

    private static String TAG = "ListSetting";

    // TODO: Rename and change types of parameters
    private String mId;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference mShoppingListRef;

    private List<Chip> mContactList;

    private OnFragmentInteractionListener mListener;

    private Button mSaveButton;
    private Button mCancelButton;

    private EditText mTitle;
    private EditText mDescription;
    private ChipsInput mUsers;

    public ListSettingFragment() {
        // Required empty public constructor
    }

    public static ListSettingFragment newInstance(String id) { //TODO: use list object (List list)
        ListSettingFragment fragment = new ListSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
        }
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("/users");
        mShoppingListRef = mDatabase.getReference("/shoppinglists");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mContactList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    addUserToChip(user, ds.getKey());
                }
                updateFragment();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return view;
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

        users.put(mAuth.getCurrentUser().getUid(),Constants.OWNER);

        shoppingList.setMembers(users);

        String listKey = mShoppingListRef.push().getKey();

        mShoppingListRef.child(listKey).setValue(shoppingList);

        for (String uid : shoppingList.getMembers().keySet()) {
            Map<String, Object> newList = new HashMap<>();
            newList.put(listKey, true);
            mUserRef.child(uid).child("shoppinglists").updateChildren(newList);
        }
        closeFragment();
    }

    private void updateFragment() {
        Fragment current = getFragmentManager().findFragmentByTag("ListSetting");
        View v = current.getView();
        if(v != null) {
            mUsers = new ChipsInput(getContext());
            mUsers.setMaxRows(10);
            mUsers.setMaxHeight(ViewUtil.dpToPx(400)+8);

            LinearLayout ll = v.findViewById(R.id.list_setting_chips_input);
            ll.addView(mUsers);

            mUsers.setFilterableList(mContactList);
        }
        mSaveButton.setEnabled(true);
        mCancelButton.setEnabled(true);
    }

    private void addUserToChip(User user, String key) {
        if (!key.equals(mAuth.getCurrentUser().getUid())) {
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

