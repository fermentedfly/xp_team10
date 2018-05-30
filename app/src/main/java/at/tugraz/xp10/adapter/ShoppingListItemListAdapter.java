package at.tugraz.xp10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import at.tugraz.xp10.R;
import at.tugraz.xp10.fragments.ListViewFragment;
import at.tugraz.xp10.model.ShoppingListItem;

public class ShoppingListItemListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ShoppingListItem> mDataSource;
    private ListViewFragment mListViewFragment;

    public ShoppingListItemListAdapter(Context context, ArrayList<ShoppingListItem> items, ListViewFragment listViewFragment) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListViewFragment = listViewFragment;
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }


    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        // Get view for row item

        if (rowView == null)
            rowView = mInflater.inflate(R.layout.shopping_list_item, parent, false);

        if(!mListViewFragment.mEditMode) {
            final CheckBox purchasedView = (CheckBox) rowView.findViewById(R.id.shopping_list_item_purchased);
            final TextView nameTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_name);
            final TextView categoryTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_category);
            final TextView quantityTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_quantity);
            final ImageButton deleteBtn = (ImageButton) rowView.findViewById(R.id.item_delete);

            final ShoppingListItem item = (ShoppingListItem) getItem(position);

            final Spinner spinner = (Spinner) rowView.findViewById(R.id.shopping_list_item_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.planets_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);


            purchasedView.setChecked(item.getIsPurchased());
            nameTextView.setText(item.getName());
            categoryTextView.setText(item.getCategory());
            quantityTextView.setText(String.format("%.0f", item.getQuantity()));

            spinner.setSelection(adapter.getPosition(item.getUnit()));

            final View finalRowView = rowView;
            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListViewFragment.deleteItem(item.getTempId());
                    finalRowView.findViewById(R.id.item_delete).setVisibility(View.INVISIBLE);
                    finalRowView.setBackgroundColor(mListViewFragment.getResources().getColor(R.color.colorAccent));
                }
            });


            purchasedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mListViewFragment.mEditMode) {
                        mListViewFragment.mEditableView = finalRowView;
                        mListViewFragment.updateItemToDB(item);
                    }
                }
            });

            nameTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return longClick(view, finalRowView, nameTextView, categoryTextView, quantityTextView, spinner, deleteBtn, item);
                }
            });
            quantityTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return longClick(view, finalRowView, nameTextView, categoryTextView, quantityTextView, spinner, deleteBtn, item);
                }
            });
            categoryTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return longClick(view, finalRowView, nameTextView, categoryTextView, quantityTextView, spinner, deleteBtn, item);
                }
            });
            spinner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return longClick(view, finalRowView, nameTextView, categoryTextView, quantityTextView, spinner, deleteBtn, item);
                }
            });

        }
        return rowView;
    }

    private boolean longClick(View view, View finalRowView, TextView nameTextView, TextView categoryTextView, TextView quantityTextView, Spinner spinner, ImageButton deleteBtn, ShoppingListItem item) {

        if(mListViewFragment.mEditMode) return true;

        mListViewFragment.mEditMode = true;
        mListViewFragment.mEditableView = finalRowView;
        mListViewFragment.mOriginShoppingListItem = item;


        nameTextView.setFocusableInTouchMode(true);
        categoryTextView.setFocusableInTouchMode(true);
        quantityTextView.setFocusableInTouchMode(true);
        spinner.setFocusableInTouchMode(true);

        finalRowView.setBackgroundColor(mListViewFragment.getResources().getColor(R.color.colorEditGray));
        deleteBtn.setBackgroundColor(mListViewFragment.getResources().getColor(R.color.colorEditGray));
        deleteBtn.setVisibility(View.VISIBLE);

        mListViewFragment.mTmpShoppingListItem = item;

        mListViewFragment.getView().findViewById(R.id.lvCancelButton).setVisibility(View.VISIBLE);
        mListViewFragment.getView().findViewById(R.id.lvSaveButton).setVisibility(View.VISIBLE);
        mListViewFragment.getView().findViewById(R.id.addItemButton).setVisibility(View.INVISIBLE);
        return true;

    }

    public void setButtonsVisibility(View view, int visibility) {

        ImageButton deleteBtn = view.findViewById(R.id.item_delete);
        deleteBtn.setVisibility(visibility);
        view.findViewById(R.id.shopping_list_item_name).setFocusableInTouchMode(false);
        view.findViewById(R.id.shopping_list_item_category).setFocusableInTouchMode(false);
        view.findViewById(R.id.shopping_list_item_quantity).setFocusableInTouchMode(false);
        view.findViewById(R.id.shopping_list_item_spinner).setFocusableInTouchMode(false);

    }
}
