package at.tugraz.xp10.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.tugraz.xp10.R;
import at.tugraz.xp10.fragments.AllListFragment.DetailListener;
import at.tugraz.xp10.util.ListEntry;

import java.util.ArrayList;

public class AllListRecyclerViewAdapter extends RecyclerView.Adapter<AllListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ListEntry> mValues;
    private final DetailListener mListener;

    public AllListRecyclerViewAdapter(ArrayList<ListEntry> items, DetailListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_all_list_item, parent, false);
        return new ViewHolder(view, mListener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public ListEntry mItem;
        private DetailListener mListener;

        public ViewHolder(View view, DetailListener listener) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mListener = listener;
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            mListener.onDetail(mItem);
        }
    }
}

