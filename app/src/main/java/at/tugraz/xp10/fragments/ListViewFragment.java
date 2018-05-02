package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import at.tugraz.xp10.adapter.ShoppingListItemListAdapter;
import at.tugraz.xp10.model.ShoppingListItem;
import at.tugraz.xp10.R;


public class ListViewFragment extends Fragment {
    private DatabaseReference mDB;
    private DatabaseReference mShoppingListItems;

    private static final String ARG_SHOPPING_LIST_ID = "shoppingListId";
    private static final String s_Title = "Title";

    private String mShoppingListId = "";
    private String m_Title = "";

    private OnFragmentInteractionListener mListener;

    private ArrayList<ShoppingListItem> mItemList = new ArrayList<>();
    ShoppingListItemListAdapter mAdapter;


    public ListViewFragment() {
        // Required empty public constructor
    }


    public static ListViewFragment newInstance(String shoppingListId, String title) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOPPING_LIST_ID, shoppingListId);
        args.putString(s_Title, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShoppingListId = getArguments().getString(ARG_SHOPPING_LIST_ID);
            m_Title = getArguments().getString(s_Title);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        FloatingActionButton addItemBtn = v.findViewById(R.id.addItemButton);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToDB();
            }
        });
        Button goShoppingBtn = v.findViewById(R.id.goShoppingButton);
        goShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        SetTitle();

        mDB = FirebaseDatabase.getInstance().getReference();
        mShoppingListItems = mDB.child("items").child(mShoppingListId);


        mShoppingListItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ListView mListView = v.findViewById(R.id.item_list_view);

        mAdapter = new ShoppingListItemListAdapter(getContext(), mItemList);
        mListView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = ListSettingFragment.newInstance(mShoppingListId);
                fragmentTransaction.replace(R.id.content_frame, fragment, "ListSetting").addToBackStack(null);
                fragmentTransaction.commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void SetTitle()
    {
        if(m_Title != null && !m_Title.isEmpty())
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if(actionBar != null)
            {
                actionBar.setTitle(m_Title);
            }
        }
    }


    private void addItemToDB()
    {
        try {
            String name = ((EditText) getView().findViewById(R.id.item_name)).getText().toString();
            String category = ((EditText) getView().findViewById(R.id.item_category)).getText().toString();
            Double unitprice = Double.parseDouble(((EditText) getView().findViewById(R.id.item_price)).getText().toString());
            Double quanitiy = Double.parseDouble(((EditText) getView().findViewById(R.id.item_quantity)).getText().toString());

            ShoppingListItem item = new ShoppingListItem(name, quanitiy, unitprice, category, false);

            mShoppingListItems.push().setValue(item);

            ((EditText) getView().findViewById(R.id.item_name)).setText("");
            ((EditText) getView().findViewById(R.id.item_category)).setText("");
            ((EditText) getView().findViewById(R.id.item_price)).setText("");
            ((EditText) getView().findViewById(R.id.item_quantity)).setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Wrong number format!", Toast.LENGTH_LONG).show();
        }
    }


    private void fetchData(DataSnapshot dataSnapshot)
    {
        mItemList.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            ShoppingListItem item = ds.getValue(ShoppingListItem.class);
            mItemList.add(item);
        }

        mAdapter.notifyDataSetChanged();

    }





}
