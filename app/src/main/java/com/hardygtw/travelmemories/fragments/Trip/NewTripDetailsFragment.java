package com.hardygtw.travelmemories.fragments.Trip;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.hardygtw.travelmemories.R;

/**
 * Created by gavin on 12/02/2015.
 */
public class NewTripDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.new_trip_details, container, false);

        return v;
    }
}

