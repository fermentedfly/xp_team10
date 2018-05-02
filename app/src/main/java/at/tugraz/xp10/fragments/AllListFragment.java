package at.tugraz.xp10.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.tugraz.xp10.MainActivity;
import at.tugraz.xp10.R;
import at.tugraz.xp10.adapter.AllListRecyclerViewAdapter;
import at.tugraz.xp10.model.ShoppingList;
import at.tugraz.xp10.model.User;
import at.tugraz.xp10.util.ListEntry;

public class AllListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "AllListFragment";
    private int mColumnCount = 2;
    private ArrayList<ListEntry> mShoppingLists = new ArrayList<>();
    private HashMap<String, ShoppingList> mShoppingMap = new HashMap<>();
    private AllListRecyclerViewAdapter mAdapter;

    private DetailListener mListener = new DetailListener() {
        @Override
        public void onDetail(ListEntry entry) {
            Log.d(TAG, "Item clicked: " + entry.id);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = ListViewFragment.newInstance("foo", "bar", "foobar");
            fragmentTransaction.replace(R.id.content_frame, fragment, "ListSetting").addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    public AllListFragment() {
    }

    public static AllListFragment newInstance(int columnCount) {
        AllListFragment fragment = new AllListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_all_list_list, container, false);

        View viewById = view.findViewById(R.id.all_list_recyclerview);
        // Set the adapter
        if (viewById instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) viewById;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new AllListRecyclerViewAdapter(mShoppingLists, mListener);
            recyclerView.setAdapter(mAdapter);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Lists");

        FloatingActionButton fab = view.findViewById(R.id.button_add_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //TODO: has to be replaced by title from database
                Fragment fragment = ListSettingFragment.newInstance("Title");
                fragmentTransaction.replace(R.id.content_frame, fragment, "ListSetting").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        ((MainActivity) view.getContext()).currentUser.setUserListener(new User.UserListener() {
            @Override
            public void onLoaded(User user) {
                Log.d(TAG, "onLoaded: " + user.toString());
                buildOverallView(view);
            }
        });

        buildOverallView(view);

        return view;
    }

    private void buildOverallView(View view) {
        User user = ((MainActivity) view.getContext()).currentUser;
        mShoppingLists.clear();
        mShoppingMap.clear();
        mAdapter.notifyDataSetChanged();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        if (!user.getShoppinglists().isEmpty()) {
            for (final String shoppingListId : user.getShoppinglists().keySet()) {

                Query query = database.child("shoppinglists").child(shoppingListId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mShoppingMap.put(shoppingListId, dataSnapshot.getValue(ShoppingList.class));
                        mShoppingLists.clear();

                        for (Map.Entry<String, ShoppingList> entry : mShoppingMap.entrySet()) {
                            mShoppingLists.add(new ListEntry(entry.getKey(), entry.getValue()));
                        }

                        Log.d(TAG, "got Shoppinglist " + dataSnapshot.getValue(ShoppingList.class).toString());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    public interface DetailListener {
        void onDetail(ListEntry entry);
    }
}
