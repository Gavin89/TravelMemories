package com.hardygtw.travelmemories.fragments.Trip;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.model.Trip;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTripFragment extends Fragment {

    private ActionBar actionBar;
    private FragmentTabHost mTabHost;
    private int trip_id;

    public ViewTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment let me watch the video
        View rootView = inflater.inflate(R.layout.tab_list,container, false);

        actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);

        trip_id = getArguments().getInt("TRIP_ID");
        Trip trip = SQLDatabaseSingleton.getInstance(getActivity()).getTripDetails(trip_id);
        String title = trip.getTripName();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);

        Bundle bundle = new Bundle(1);
        bundle.putInt("TRIP_ID", trip_id);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.trip_details)),
                ViewTripDetailsFragment.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.trip_gallery)),
                ViewTripGalleryFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.view_places)),
                ViewTripPlacesFragment.class, null);

        if (!title.equals("")) {
            actionBar.setTitle(title);
        }

        return rootView;
    }


    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_view_trip, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_trip:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this place?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SQLDatabaseSingleton.getInstance(getActivity()).deleteTrip(trip_id);
                                ((MainActivity)getActivity()).goBackFragment();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.edit_trip:
                android.support.v4.app.Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle(2);
                bundle.putInt("TRIP_ID", trip_id);
                bundle.putString("EDIT_TRIP", "Edit Trip");

                fragment = new EditTripFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"EDIT_TRIP_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.share_place:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
