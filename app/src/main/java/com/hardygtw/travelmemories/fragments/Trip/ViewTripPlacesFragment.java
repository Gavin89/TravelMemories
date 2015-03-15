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
        import com.hardygtw.travelmemories.fragments.Places.NewPlaceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTripPlacesFragment extends Fragment implements View.OnClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int trip_id;

    public ViewTripPlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trip_places, container, false);
        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

//        trip_id = getArguments().getInt("TRIP_ID");


      //  Bundle bundle = new Bundle(1);
      //  bundle.putInt("TRIP_ID", trip_id);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TripPlacesAdapter(getTestData(), getParentFragment().getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private String[] getTestData() {
        String[] strings = new String[10];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = "Place " + i;
        }
        return strings;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_trip_place:
                android.support.v4.app.Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle(1);
              //  bundle.putInt("TRIP_ID", trip_id);
                bundle.putString("ADD PLACE", "Add Place");

                fragment = new NewPlaceFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment, "NEW_PLACE_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }

    }
}



