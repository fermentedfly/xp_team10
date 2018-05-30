package at.tugraz.xp10.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.firebase.ShoppingListValueEventListener;
import at.tugraz.xp10.firebase.ShoppingLists;
import at.tugraz.xp10.firebase.Users;
import at.tugraz.xp10.firebase.UserListValueEventListener;
import at.tugraz.xp10.model.ShoppingList;
import at.tugraz.xp10.model.User;
import at.tugraz.xp10.util.Constants;


public class ListSettingFragment extends Fragment {
    private static final String ARG_LIST_ID = "ID";

    private String mListID;
    private Users mUsersFBHandle;
    private ShoppingLists mShoppingListsFBHandle;

    private ArrayList<Chip> mContactList;

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
        mUsersFBHandle = new Users();
        mShoppingListsFBHandle = new ShoppingLists();
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
        } else {
            mListID = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private void loadFriends() {
        mUserList = new HashMap<>();
        mUsersFBHandle.getUsers(new UserListValueEventListener() {
            @Override
            public void onNewData(HashMap<String, User> data) {
                mContactList = new ArrayList<>();

                for (HashMap.Entry<String, User> d : data.entrySet()) {
                    mUserList.put(d.getKey(), d.getValue());
                    if (d.getKey().equalsIgnoreCase(mUsersFBHandle.getCurrentUserID())) {
                        mCurrentUser = d.getValue();
                    }
                }
                if (mListID != null) {
                    load_list_by_id();
                } else {
                    owner = mCurrentUser;
                    ownerID = mUsersFBHandle.getCurrentUserID();
                    fill_ui_fields(null);
                }
            }
        });
    }

    private void load_list_by_id() {
        mShoppingListsFBHandle.getShoppingList(mListID, new ShoppingListValueEventListener() {
            @Override
            public void onNewData(ShoppingList data) {
                for (HashMap.Entry<String, String> entry : data.getMembers().entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(Constants.OWNER)) {
                        owner = mUserList.get(entry.getKey());
                        ownerID = entry.getKey();
                    }
                }
                fill_ui_fields(data);
            }
        });
    }

    private void fill_ui_fields(ShoppingList list) {
        for (HashMap.Entry<String, User> entry : mUserList.entrySet()) {
            if ((mCurrentUser.getFriends().containsKey(entry.getKey())) && (
                    mCurrentUser.getFriends().get(entry.getKey()).equalsIgnoreCase(Constants.FRIENDS_REQUEST_CONFIRMED))) {
                addUserToChip(entry.getValue(), entry.getKey());
            }
        }

        Fragment current = getFragmentManager().findFragmentByTag("ListSetting");
        View v = current.getView();
        if (v != null) {
            ChipView ownerChipView = v.findViewById(R.id.owner_chip_view);
            ownerChipView.setLabel(owner.getName());
            ownerChipView.setHasAvatarIcon(true);

            mUsers = new ChipsInput(getContext());
            mUsers.setChipDeletable(true);
            mUsers.setMaxRows(10);
            mUsers.setMaxHeight(ViewUtil.dpToPx(400) + 8);

            mUsers.getEditText().setTextColor(Color.WHITE);
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


    private void saveDataToDatabase() {

        if (TextUtils.isEmpty(mTitle.getText().toString())) {
            mTitle.setError(getString(R.string.error_field_required));
            return;
        }

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setTitle(mTitle.getText().toString());
        if (!mDescription.getText().toString().isEmpty())
            shoppingList.setDescription(mDescription.getText().toString());

        HashMap<String, String> users = new HashMap<>();
        ArrayList<Chip> selectedUsers = (ArrayList<Chip>) mUsers.getSelectedChipList();

        for (Chip selUser : selectedUsers) {
            users.put(selUser.getId().toString(), Constants.MEMBER);
        }

        users.put(ownerID, Constants.OWNER);

        if (!mUsersFBHandle.getCurrentUserID().equals(ownerID)) {
            users.put(mUsersFBHandle.getCurrentUserID(), Constants.MEMBER);
        }

        shoppingList.setMembers(users);
        mShoppingListsFBHandle.updateShoppingList(mListID, shoppingList);

        for (String uid : shoppingList.getMembers().keySet()) {
            mUsersFBHandle.addShoppingListToUser(uid, mListID);
        }
        closeFragment();
    }

    private void addUserToChip(User user, String key) {
        if (!key.equalsIgnoreCase(mUsersFBHandle.getCurrentUserID()) && user != owner) {
            mContactList.add(new Chip(key, user.getFirstName() + " " + user.getLastName(), user.geteMail()));
        }
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}

