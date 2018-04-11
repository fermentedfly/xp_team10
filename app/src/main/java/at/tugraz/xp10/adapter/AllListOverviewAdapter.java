package at.tugraz.xp10.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AllListOverviewAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mList;

    public AllListOverviewAdapter(Context c, String[] dummy) {
        mContext = c;
        mList = dummy;
    }

    public int getCount() {
        return mList.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setBackgroundColor(0xff);
            textView.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
            textView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(mList[position]);
        return textView;
    }
}
