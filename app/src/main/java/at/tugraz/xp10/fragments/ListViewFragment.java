package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import at.tugraz.xp10.adapter.ShoppingListItemListAdapter;
import at.tugraz.xp10.model.ShoppingListItem;
import at.tugraz.xp10.R;


public class ListViewFragment extends Fragment {
    private DatabaseReference mDB;
    private DatabaseReference mShoppingListItems;

    private static final String ARG_SHOPPING_LIST_ID = "shoppingListId";
    private static final String s_Title = "Title";

    private String mShoppingListId = "";
    private String m_Title = "";

    private OnFragmentInteractionListener mListener;

    private ArrayList<ShoppingListItem> mItemList = new ArrayList<>();
    ShoppingListItemListAdapter mAdapter;

    public Boolean mEditMode;
    private Boolean mAddMode;
    private Boolean mException;
    public View mEditableView;
    public ShoppingListItem mOriginShoppingListItem;
    public ShoppingListItem mTmpShoppingListItem;

    public ListViewFragment() {
        // Required empty public constructor
    }


    public static ListViewFragment newInstance(String shoppingListId, String title) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOPPING_LIST_ID, shoppingListId);
        args.putString(s_Title, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShoppingListId = getArguments().getString(ARG_SHOPPING_LIST_ID);
            m_Title = getArguments().getString(s_Title);
        }
        mEditMode = false;
        mAddMode = false;
        mException = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);


        final RelativeLayout addItemLayout = v.findViewById(R.id.shopping_list_item);
        addItemLayout.setVisibility(View.GONE);

        final Button cancelButton = v.findViewById(R.id.lvCancelButton);
        final Button saveButton = v.findViewById(R.id.lvSaveButton);

        Spinner spinner = (Spinner) v.findViewById(R.id.item_unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        FloatingActionButton addItemBtn = v.findViewById(R.id.addItemButton);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddMode = true;
                displayAddLayout(v, addItemLayout);
                cancelButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);

            }
        });

        SetTitle();

        mDB = FirebaseDatabase.getInstance().getReference();
        mShoppingListItems = mDB.child("items").child(mShoppingListId);


        mShoppingListItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ListView mListView = v.findViewById(R.id.item_list_view);
        mAdapter = new ShoppingListItemListAdapter(getContext(), mItemList, this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if(mEditMode) return true;

                mOriginShoppingListItem = ((ShoppingListItem) mAdapter.getItem(pos));
                mEditMode = true;
                mEditableView = view;

                view.findViewById(R.id.shopping_list_item_name).setFocusableInTouchMode(true);
                view.findViewById(R.id.shopping_list_item_category).setFocusableInTouchMode(true);
                view.findViewById(R.id.shopping_list_item_quantity).setFocusableInTouchMode(true);
                view.findViewById(R.id.shopping_list_item_spinner).setFocusableInTouchMode(true);

                view.findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorEditGray));
                mEditableView.findViewById(R.id.item_delete).setBackgroundColor(getResources().getColor(R.color.colorEditGray));

                mAdapter.setButtonsVisibility(view, View.VISIBLE);

                editItem((ShoppingListItem) mAdapter.getItem(pos));

                cancelButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                return true;
            }

        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAddMode){
                    mAddMode = false;
                    setItemFieldsEmpty();
                    getView().findViewById(R.id.shopping_list_item).setVisibility(View.GONE);
                }
                else if(mEditMode){
                    mEditMode = false;
                    mAdapter.setButtonsVisibility(mEditableView, View.INVISIBLE);
                    mEditableView.findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mEditableView.findViewById(R.id.item_delete).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    setFieldsReadOnly();
                }
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(addItemLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                cancelButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);

                getView().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
              }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAddMode){
                    addItemToDB();
                    if(!mException) {
                        mAddMode = false;
                        addItemLayout.setVisibility(View.GONE);
                    }
                } else if(mEditMode) {

                    updateItemToDB(mTmpShoppingListItem);
                    if(!mException) {
                        mEditMode = false;
                        mEditableView.findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        mEditableView.findViewById(R.id.item_delete).setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        mAdapter.setButtonsVisibility(mEditableView, View.INVISIBLE);
                    }
                }

                if(!mException) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(addItemLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    cancelButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.GONE);


                }
                mException = false;
            }
        });

        return v;
    }


    private void setFieldsReadOnly() {
        CheckBox purchasedView = (CheckBox) mEditableView.findViewById(R.id.shopping_list_item_purchased);
        TextView nameTextView = (TextView) mEditableView.findViewById(R.id.shopping_list_item_name);
        TextView categoryTextView = (TextView) mEditableView.findViewById(R.id.shopping_list_item_category);
        TextView quantityTextView = (TextView) mEditableView.findViewById(R.id.shopping_list_item_quantity);
        ImageButton deleteBtn = (ImageButton) mEditableView.findViewById(R.id.item_delete);
        Spinner spinner = (Spinner) mEditableView.findViewById(R.id.shopping_list_item_spinner);

        purchasedView.setChecked(mOriginShoppingListItem.getIsPurchased());
        nameTextView.setText(mOriginShoppingListItem.getName());
        categoryTextView.setText(mOriginShoppingListItem.getCategory());
        quantityTextView.setText(String.format("%.0f", mOriginShoppingListItem.getQuantity()));


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
        spinner.setSelection(myAdap.getPosition(spinner.getSelectedItem().toString()));

       // purchasedView.setEnabled(true);
        nameTextView.setFocusableInTouchMode(false);
        categoryTextView.setFocusableInTouchMode(false);
        quantityTextView.setFocusableInTouchMode(false);
        spinner.setFocusableInTouchMode(false);
    }

    private void displayAddLayout(View v, RelativeLayout addLayout) {
        addLayout.setVisibility(View.VISIBLE);
        v.findViewById(R.id.addItemButton).setVisibility(View.INVISIBLE);
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

    private void SetTitle()
    {
        if(m_Title != null && !m_Title.isEmpty())
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar != null)
            {
                actionBar.setTitle(m_Title);
            }
        }
    }


    private void addItemToDB()
    {
        try {
            String name = ((EditText) getView().findViewById(R.id.item_name)).getText().toString();
            String category = ((EditText) getView().findViewById(R.id.item_category)).getText().toString();
            String unit = ((Spinner) getView().findViewById(R.id.item_unit_spinner)).getSelectedItem().toString();
            Double quanitiy = Double.parseDouble(((EditText) getView().findViewById(R.id.item_quantity)).getText().toString());

            String listKey = mShoppingListItems.push().getKey();
            ShoppingListItem item = new ShoppingListItem(name, quanitiy, unit, category, false, listKey);
            mShoppingListItems.child(listKey).setValue(item);

            setItemFieldsEmpty();

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Wrong number format!", Toast.LENGTH_LONG).show();
            mException = true;
        }
    }

    private void setItemFieldsEmpty() {
        ((EditText) getView().findViewById(R.id.item_name)).setText("");
        ((EditText) getView().findViewById(R.id.item_category)).setText("");
        ((EditText) getView().findViewById(R.id.item_quantity)).setText("");
        getView().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
     }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        mItemList.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            ShoppingListItem item = ds.getValue(ShoppingListItem.class);
            mItemList.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

    public void deleteItem(String id)
    {
        getView().findViewById(R.id.lvCancelButton).setVisibility(View.GONE);
        getView().findViewById(R.id.lvSaveButton).setVisibility(View.GONE);
        getView().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mShoppingListItems.child(id).removeValue();

        mEditMode = false;
    }

    public void editItem(ShoppingListItem item){

        getView().findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorEditGray));
        mTmpShoppingListItem = item;
        Button editSaveBtn = getView().findViewById(R.id.lvSaveButton);
        editSaveBtn.setVisibility(View.VISIBLE);
        getView().findViewById(R.id.addItemButton).setVisibility(View.INVISIBLE);

    }

    public void updateItemToDB(ShoppingListItem item)
    {
        try {
            String name = ((EditText) mEditableView.findViewById(R.id.shopping_list_item_name)).getText().toString();
            String category = ((EditText) mEditableView.findViewById(R.id.shopping_list_item_category)).getText().toString();
            Double quanitiy = Double.parseDouble(((EditText) mEditableView.findViewById(R.id.shopping_list_item_quantity)).getText().toString());
            Boolean isPurchased = ((CheckBox) mEditableView.findViewById(R.id.shopping_list_item_purchased)).isChecked();
            String unit = ((Spinner) mEditableView.findViewById(R.id.shopping_list_item_spinner)).getSelectedItem().toString();

            String listKey = item.getTempId();
            ShoppingListItem new_item = new ShoppingListItem(name, quanitiy, unit, category, isPurchased, listKey);
            mShoppingListItems.child(listKey).setValue(new_item);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Wrong number format!", Toast.LENGTH_LONG).show();
            mException = true;
        }
    }
}
