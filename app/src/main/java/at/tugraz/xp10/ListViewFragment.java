package at.tugraz.xp10;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static ListViewFragment newInstance(String param1, String param2) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_list_view, container, false);

        FloatingActionButton b = v.findViewById(R.id.addItemButton);
        b.setOnClickListener(this);

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

    public void onAddItem(View view)
    {
        Log.i("info", "onAddItem");
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
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.addItemButton:
                Log.d("fuck", "im inside");
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
}
