package at.tugraz.xp10.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.xp10.R;
import at.tugraz.xp10.adapter.CategoriesAdapter;
import at.tugraz.xp10.model.Category;

public class CategoriesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Category> mCategoriesList;
    private CategoriesAdapter mAdapter;
    private DatabaseReference mDB;
    private DatabaseReference mCategories;
    private LayoutInflater mInflater;
    private View mView;

    public CategoriesFragment() {
        mCategoriesList = new ArrayList<>();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mView = inflater.inflate(R.layout.fragment_categories, container, false);

        final FloatingActionButton addCategoryButton = mView.findViewById(R.id.addCategoryButton);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });

        ListView mListView = mView.findViewById(R.id.category_list_view);

        mAdapter = new CategoriesAdapter(getContext(), mCategoriesList);
        mListView.setAdapter(mAdapter);

        mDB = FirebaseDatabase.getInstance().getReference();
        mCategories = mDB.child("categories");


        mCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mView;
    }

    private void addCategory() {
        View promptView = mInflater.inflate(R.layout.category_add_dialog, null);
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
        mCategories.push().setValue(new Category(newCategory));
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        mCategoriesList.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Category item = ds.getValue(Category.class);
            mCategoriesList.add(item);
        }

        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
