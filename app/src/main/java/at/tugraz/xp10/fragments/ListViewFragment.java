package at.tugraz.xp10.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.tugraz.xp10.Item;
import at.tugraz.xp10.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment implements View.OnClickListener {
    private List<Item> items;
    private DatabaseReference db;
    //private FirebaseHelper dbHelper;
    private ArrayAdapter<Item> itemsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String s_Title = "Title";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String m_Title = "";

    private OnFragmentInteractionListener mListener;

    public ListViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListViewFragment newInstance(String param1, String param2, String title) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(s_Title, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            m_Title = getArguments().getString(s_Title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        FloatingActionButton addItemBtn = v.findViewById(R.id.addItemButton);
        addItemBtn.setOnClickListener(this);
        Button goShoppingBtn = v.findViewById(R.id.goShoppingButton);
        goShoppingBtn.setOnClickListener(this);

        SetTitle();

        items = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference();
        //itemsAdapter = new ArrayAdapter<Item>(this, R.id.listGridLayout, retrieve());
        retrieve();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public void addItem(Item item)
    {
        boolean isPurchased = item.isPurchased;
        String name = item.name;
        String category = item.category;
        Long price = item.price;
        Long quantity = item.quantity;

        GridLayout gridView = getView().findViewById(R.id.listGridLayout);
        CheckBox cb = new CheckBox(gridView.getContext());
        GridLayout.LayoutParams param= new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,0.5f));
        param.width = 0;
        cb.setLayoutParams(param);
        cb.setChecked(isPurchased);
        gridView.addView(cb);

        EditText tv = new EditText(gridView.getContext());
        tv.setText(name);
        param = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1.5f));
        param.width = 0;
        tv.setLayoutParams(param);
        gridView.addView(tv);

        tv = new EditText(gridView.getContext());
        tv.setText(category);
        param = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
        param.width = 0;
        tv.setLayoutParams(param);
        gridView.addView(tv);

        tv = new EditText(gridView.getContext());
        tv.setText(price.toString());
        tv.setInputType(InputType.TYPE_CLASS_NUMBER);
        param = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
        param.width = 0;
        tv.setLayoutParams(param);
        gridView.addView(tv);

        tv = new EditText(gridView.getContext());
        tv.setText(quantity.toString());
        tv.setInputType(InputType.TYPE_CLASS_NUMBER);
        param = new GridLayout.LayoutParams(GridLayout.spec(
                GridLayout.UNDEFINED,GridLayout.FILL),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
        param.width = 0;
        tv.setLayoutParams(param);
        gridView.addView(tv);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.addItemButton:
                GridLayout layout = getView().findViewById(R.id.listGridLayout);
                CheckBox cb = new CheckBox(layout.getContext());
                GridLayout.LayoutParams param= new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,0.5f));
                param.width = 0;
                cb.setLayoutParams(param);
                layout.addView(cb);

                EditText tv = new EditText(layout.getContext());
                tv.setText("Ketchup");
                param = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1.5f));
                param.width = 0;
                tv.setLayoutParams(param);
                layout.addView(tv);

                tv = new EditText(layout.getContext());
                tv.setText("Essen");
                param = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
                param.width = 0;
                tv.setLayoutParams(param);
                layout.addView(tv);

                tv = new EditText(layout.getContext());
                tv.setText("5€");
                tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                param = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
                param.width = 0;
                tv.setLayoutParams(param);
                layout.addView(tv);

                tv = new EditText(layout.getContext());
                tv.setText("2");
                tv.setInputType(InputType.TYPE_CLASS_NUMBER);
                param = new GridLayout.LayoutParams(GridLayout.spec(
                        GridLayout.UNDEFINED,GridLayout.FILL),
                        GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL,1f));
                param.width = 0;
                tv.setLayoutParams(param);
                layout.addView(tv);
                break;

            case R.id.goShoppingButton:
                saveListData();
                saveListDataDB();
                // go to next fragment
                break;
            default:
                break;
        }
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

    public void saveListData()
    {
        for (int i = 0; i < 10; i++)
        {
            Item asd = new Item( "Ketchup2", "Essen", new Long(5 + i), new Long(i));
            items.add(asd);
        }
    }

    public void saveListDataDB()
    {
        try
        {
            for (Item item : items)
            {
                db.child("items").push().setValue(item);
            }

        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }
    }

    public List<Item> retrieve()
    {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return items;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {
        items.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Item item = ds.getValue(Item.class);
            items.add(item);
            
            addItem(item);
        }
    }
}
