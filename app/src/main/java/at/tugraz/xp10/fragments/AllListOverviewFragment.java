package at.tugraz.xp10.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import at.tugraz.xp10.R;
import at.tugraz.xp10.adapter.AllListOverviewAdapter;

public class AllListOverviewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_list_overview, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.grid_overview);
        gridview.setAdapter(new AllListOverviewAdapter(getActivity()));
        return view;
    }

    public static AllListOverviewFragment newInstance() {
        AllListOverviewFragment fragment = new AllListOverviewFragment();
        return fragment;
    }
}
