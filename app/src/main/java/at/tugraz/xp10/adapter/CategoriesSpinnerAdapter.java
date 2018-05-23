package at.tugraz.xp10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.tugraz.xp10.R;
import at.tugraz.xp10.model.Category;

public class CategoriesSpinnerAdapter extends ArrayAdapter <Category>{

    public CategoriesSpinnerAdapter(Context context, ArrayList<Category> categories){
        super (context, 0, categories);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Category category = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_item, parent, false);

        }

        TextView categoryName = (TextView) convertView.findViewById(R.id.category_name);

        categoryName.setText(category.getName());

        return convertView;

    }

}
