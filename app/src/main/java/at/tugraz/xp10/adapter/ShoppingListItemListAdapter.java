package at.tugraz.xp10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.shopping_list_item, parent, false);

        CheckBox purchasedView = (CheckBox) rowView.findViewById(R.id.shopping_list_item_purchased);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_name);
        TextView categoryTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_category);
        TextView priceTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_price);
        TextView quantityTextView = (TextView) rowView.findViewById(R.id.shopping_list_item_quantity);
        ImageButton editBtn = (ImageButton) rowView.findViewById(R.id.item_edit);
        ImageButton deleteBtn = (ImageButton) rowView.findViewById(R.id.item_delete);

        final ShoppingListItem item = (ShoppingListItem) getItem(position);

        purchasedView.setChecked(item.getIsPurchased());
        purchasedView.setEnabled(false);
        nameTextView.setText(item.getName());
        categoryTextView.setText(item.getCategory());
        priceTextView.setText(String.format("%.2f", item.getUnitprice()));
        quantityTextView.setText(String.format("%.0f", item.getQuantity()));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListViewFragment.editItem(item);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListViewFragment.deleteItem(item.getTempId());
            }
        });

        return rowView;
    }

    public void setButtonsVisibility(View v, int visibility) {

        ImageButton editBtn = v.findViewById(R.id.item_edit);
        ImageButton deleteBtn = v.findViewById(R.id.item_delete);

        editBtn.setVisibility(visibility);
        deleteBtn.setVisibility(visibility);
    }
}
