package at.tugraz.xp10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.Category;

public class CategoriesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Category> mDataSource;

    public CategoriesAdapter(Context context, ArrayList<Category> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View rowView = mInflater.inflate(R.layout.category_list_item, parent, false);

        TextView nameTextView =
                (TextView) rowView.findViewById(R.id.category_list_item_name);

        Category category = (Category) getItem(position);

        nameTextView.setText(category.getName());

        return rowView;
    }
}
