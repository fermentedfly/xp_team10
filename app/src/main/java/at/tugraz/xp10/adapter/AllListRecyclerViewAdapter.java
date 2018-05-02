package at.tugraz.xp10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.ShoppingList;
import at.tugraz.xp10.util.ListEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class AllListRecyclerViewAdapter extends RecyclerView.Adapter<AllListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ListEntry> mValues;

    public AllListRecyclerViewAdapter(ArrayList<ListEntry> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_all_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).shoppingList.getTitle());
        holder.mContentView.setText(mValues.get(position).shoppingList.getDescription());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public ListEntry mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

