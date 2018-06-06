package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.tugraz.xp10.adapter.ShoppingListItemListAdapter;
import at.tugraz.xp10.firebase.Categories;
import at.tugraz.xp10.firebase.CategoriesValueEventListener;
import at.tugraz.xp10.firebase.ShoppingListItems;
import at.tugraz.xp10.firebase.ShoppingListItemsValueEventListener;
import at.tugraz.xp10.model.Category;
import at.tugraz.xp10.model.ShoppingListItem;
import at.tugraz.xp10.R;


public class ListViewFragment extends Fragment {

    private ShoppingListItems mShoppingListItemsFBHandle;
    private Categories mCategoriesFBHandle;

    private static final String ARG_SHOPPING_LIST_ID = "shoppingListId";
    private static final String s_Title = "Title";

    private String mShoppingListId = "";
    private String m_Title = "";

    private ArrayList<ShoppingListItem> mItemList = new ArrayList<>();
    ShoppingListItemListAdapter mAdapter;
    ArrayAdapter<String> mAdapterCategory;

    public Boolean mEditMode;
    private Boolean mAddMode;
    private Boolean mException;
    public View mEditableView;
    public ShoppingListItem mOriginShoppingListItem;
    public ShoppingListItem mTmpShoppingListItem;

    private ArrayList <String> mCategoryIdList = new ArrayList<>();
    private ArrayList <String> mCategoryNameList = new ArrayList<>();
    private Map<String, String> mCategoryIdNameMap = new HashMap<>();


    private Button mCancelButton;
    private Button mSaveButton;

    public ListViewFragment() {
        mShoppingListItemsFBHandle = new ShoppingListItems();
        mCategoriesFBHandle = new Categories();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        mCancelButton = v.findViewById(R.id.lvCancelButton);
        mSaveButton = v.findViewById(R.id.lvSaveButton);
        final RelativeLayout addItemLayout = v.findViewById(R.id.shopping_list_item);

        SetTitle();
        addItemLayout.setVisibility(View.GONE);

        Spinner unitSpinner = (Spinner) v.findViewById(R.id.item_unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        Spinner spinnerCat = (Spinner) v.findViewById(R.id.item_category);
        mAdapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, mCategoryNameList);
        mAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(mAdapterCategory);



        FloatingActionButton addItemBtn = v.findViewById(R.id.addItemButton);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddMode = true;
                displayAddLayout(v, addItemLayout);
                setButtonVisibility(View.VISIBLE);
            }
        });

        mShoppingListItemsFBHandle.getShoppingListItems(mShoppingListId,
                new ShoppingListItemsValueEventListener() {
            @Override
            public void onNewData(HashMap<String, ShoppingListItem> Items) {
                mItemList.clear();

                for (Map.Entry<String, ShoppingListItem> d : Items.entrySet())
                {
                    mItemList.add(d.getValue());
                }

                mAdapter.notifyDataSetChanged();

            }
        });

        mCategoriesFBHandle.getCategories(new CategoriesValueEventListener() {
            @Override
            public void onNewData(HashMap<String, Category> Categories) {
                mCategoryIdList.clear();
                mCategoryNameList.clear();

                for (Map.Entry<String, Category> entry : Categories.entrySet())
                {
                    mCategoryIdList.add(entry.getKey());
                    mCategoryNameList.add(entry.getValue().getName());
                    mCategoryIdNameMap.put(entry.getKey(), entry.getValue().getName());
                }

                mAdapterCategory.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });

        final ListView mListView = v.findViewById(R.id.item_list_view);
        mAdapter = new ShoppingListItemListAdapter(getContext(), mItemList, this, mCategoryIdNameMap,
                mCategoryNameList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if(mEditMode) return true;

                mOriginShoppingListItem = ((ShoppingListItem) mAdapter.getItem(pos));
                mEditMode = true;
                mEditableView = view;

                view.findViewById(R.id.shopping_list_item_name).setFocusableInTouchMode(true);
                view.findViewById(R.id.shopping_list_item_quantity).setFocusableInTouchMode(true);

                view.findViewById(R.id.shopping_list_item_purchased).setEnabled(true);
                view.findViewById(R.id.shopping_list_item_name).setEnabled(true);
                view.findViewById(R.id.shopping_list_item_category).setEnabled(true);
                view.findViewById(R.id.shopping_list_item_quantity).setEnabled(true);
                view.findViewById(R.id.shopping_list_item_spinner).setEnabled(true);

                view.findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorEditGray));
                mEditableView.findViewById(R.id.item_delete).setBackgroundColor(getResources().getColor(R.color.colorEditGray));

                mAdapter.setButtonsVisibility(view, View.VISIBLE);

                editItem((ShoppingListItem) mAdapter.getItem(pos));
                setButtonVisibility(View.VISIBLE);

                return true;
            }

        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
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
                setButtonVisibility(View.GONE);

                getView().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
              }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
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
                    setButtonVisibility(View.GONE);
                }
                mException = false;
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = ListSettingFragment.newInstance(mShoppingListId);
                fragmentTransaction.replace(R.id.content_frame, fragment, "ListSetting").addToBackStack(null);
                fragmentTransaction.commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setButtonVisibility(int visible) {
        mCancelButton.setVisibility(visible);
        mSaveButton.setVisibility(visible);
    }

    private void setFieldsReadOnly() {
        CheckBox purchasedView = (CheckBox) mEditableView.findViewById(R.id.shopping_list_item_purchased);
        TextView nameTextView = (TextView) mEditableView.findViewById(R.id.shopping_list_item_name);
        Spinner categorySpinner = (Spinner) mEditableView.findViewById(R.id.shopping_list_item_category);
        TextView quantityTextView = (TextView) mEditableView.findViewById(R.id.shopping_list_item_quantity);
        ImageButton deleteBtn = (ImageButton) mEditableView.findViewById(R.id.item_delete);
        Spinner spinner = (Spinner) mEditableView.findViewById(R.id.shopping_list_item_spinner);

        purchasedView.setChecked(mOriginShoppingListItem.getIsPurchased());
        nameTextView.setText(mOriginShoppingListItem.getName());
        String categoryId = mOriginShoppingListItem.getCategory();
        int categoryIndex = mCategoryIdList.indexOf(categoryId);
        categorySpinner.setSelection(categoryIndex);
        quantityTextView.setText(String.format("%.0f", mOriginShoppingListItem.getQuantity()));


        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
        spinner.setSelection(myAdap.getPosition(spinner.getSelectedItem().toString()));

        purchasedView.setEnabled(true);
        nameTextView.setFocusableInTouchMode(false);
        categorySpinner.setEnabled(false);
        quantityTextView.setFocusableInTouchMode(false);
        spinner.setEnabled(false);
    }

    private void displayAddLayout(View v, RelativeLayout addLayout) {
        addLayout.setVisibility(View.VISIBLE);
        v.findViewById(R.id.addItemButton).setVisibility(View.INVISIBLE);
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
            long categoryPosition = ((Spinner) getView().findViewById(R.id.item_category)).getSelectedItemPosition();
            String categoryId = mCategoryIdList.get((int)categoryPosition);

            String unit = ((Spinner) getView().findViewById(R.id.item_unit_spinner)).getSelectedItem().toString();
            Double quanitiy = Double.parseDouble(((EditText) getView().findViewById(R.id.item_quantity)).getText().toString());

            mShoppingListItemsFBHandle.addItemToShoppingList(mShoppingListId, name, quanitiy, unit, categoryId, false);

            setItemFieldsEmpty();

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Wrong number format!", Toast.LENGTH_LONG).show();
            mException = true;
        }
    }

    private void setItemFieldsEmpty() {
        ((EditText) getView().findViewById(R.id.item_name)).setText("");
        ((EditText) getView().findViewById(R.id.item_quantity)).setText("");
        getView().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
     }

    public void deleteItem(String id)
    {
        getView().findViewById(R.id.lvCancelButton).setVisibility(View.GONE);
        getView().findViewById(R.id.lvSaveButton).setVisibility(View.GONE);
        getView().findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.shopping_list_item).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mShoppingListItemsFBHandle.deleteItemFromShoppingList(mShoppingListId, id);

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
            int categoryIndex = ((Spinner) mEditableView.findViewById(R.id.shopping_list_item_category)).getSelectedItemPosition();
            String category = mCategoryIdList.get(categoryIndex);
            Double quantity = Double.parseDouble(((EditText) mEditableView.findViewById(R.id.shopping_list_item_quantity)).getText().toString());
            Boolean isPurchased = ((CheckBox) mEditableView.findViewById(R.id.shopping_list_item_purchased)).isChecked();
            String unit = ((Spinner) mEditableView.findViewById(R.id.shopping_list_item_spinner)).getSelectedItem().toString();


            mShoppingListItemsFBHandle.updateItemInShoppingList(mShoppingListId, item.getTempId(), name, quantity, unit, category, isPurchased);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Wrong number format!", Toast.LENGTH_LONG).show();
            mException = true;
        }
    }
}
