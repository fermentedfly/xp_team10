package at.tugraz.xp10.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import at.tugraz.xp10.firebase.Categories;
import at.tugraz.xp10.firebase.CategoriesValueEventListener;
import at.tugraz.xp10.R;
import at.tugraz.xp10.adapter.CategoriesAdapter;
import at.tugraz.xp10.model.Category;

public class CategoriesFragment extends Fragment {

    private ArrayList<Category> mCategoriesList;
    private CategoriesAdapter mAdapter;
    private View mView;
    private Categories mCategoriesFBHandle;

    public CategoriesFragment() {
        mCategoriesList = new ArrayList<>();
        mCategoriesFBHandle = new Categories();
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_categories, container, false);

        final FloatingActionButton addCategoryButton = mView.findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory(inflater);
            }
        });

        ListView listView = mView.findViewById(R.id.category_list_view);
        mAdapter = new CategoriesAdapter(getContext(), mCategoriesList);
        listView.setAdapter(mAdapter);

        getCategoryList();

        return mView;
    }

    public void getCategoryList() {
        mCategoriesFBHandle.getCategories(new CategoriesValueEventListener() {
            @Override
            public void onNewData(HashMap<String, Category> Categories) {
                mCategoriesList.clear();
                for (Category c : Categories.values())
                {
                    mCategoriesList.add(c);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addCategory(LayoutInflater inflater) {
        View promptView = inflater.inflate(R.layout.category_add_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mView.getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.category_name);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newCategory = editText.getText().toString();
                        addCategoryToDB(newCategory);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void addCategoryToDB(String newCategory) {
        mCategoriesFBHandle.putCategory(new Category(newCategory));
    }
}
