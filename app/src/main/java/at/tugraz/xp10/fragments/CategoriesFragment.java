package at.tugraz.xp10.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import at.tugraz.xp10.R;
import at.tugraz.xp10.adapter.CategoriesAdapter;
import at.tugraz.xp10.firebase.Categories;
import at.tugraz.xp10.firebase.CategoriesValueEventListener;
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
                addCategory(inflater, v);
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
                for (Category c : Categories.values()) {
                    mCategoriesList.add(c);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addCategory(LayoutInflater inflater, final View v) {

        final View dialogView = inflater.inflate(R.layout.category_add_dialog, null);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom))
                .setView(dialogView)
                .setTitle("New Category")
                .setPositiveButton(R.string.submit, null) //Set to null. We override the onclick
                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                // POSITIVE
                Button button = ((android.support.v7.app.AlertDialog) dialog).getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String newCategory = ((EditText) (dialogView.findViewById(R.id.category_name))).getText().toString();
                        addCategoryToDB(newCategory);
                        dialog.dismiss();
                    }
                });

                // Negative
                Button buttonNeg = ((android.support.v7.app.AlertDialog) dialog).getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);
                buttonNeg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void addCategoryToDB(String newCategory) {
        mCategoriesFBHandle.put(new Category(newCategory));
    }
}
