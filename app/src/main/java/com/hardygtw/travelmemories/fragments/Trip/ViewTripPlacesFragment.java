package com.hardygtw.travelmemories.fragments.Trip;


        import android.os.Bundle;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.widget.GridLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.support.v4.app.Fragment;
        import com.hardygtw.travelmemories.R;
        import com.hardygtw.travelmemories.adapters.PlacesAdapter;
        import com.hardygtw.travelmemories.adapters.TripPlacesAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTripPlacesFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ViewTripPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_places_visited, container, false);
        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TripPlacesAdapter(getTestData(),this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;

    }

    private String[] getTestData() {
        String[] strings = new String[10];
        for(int i =0; i < strings.length; i++) {
            strings[i] = "Place " + i;
        }
        return strings;
    }

}



