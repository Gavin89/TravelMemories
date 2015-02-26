package com.hardygtw.travelmemories.fragments.Places;


import android.app.ActionBar;
import android.content.Context;
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
import android.widget.TextView;

import com.hardygtw.travelmemories.MainActivity;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.fragments.Trip.EditTripFragment;


public class ViewPlaceFragment extends Fragment {

    private ActionBar actionBar;
    private FragmentTabHost mTabHost;

    public ViewPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab_list,container, false);
        String title = "Paris";
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_details)),
                ViewPlaceDetailsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_gallery)),
                ViewPlaceGalleryFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_companions)),
                ViewPlaceCompanionsFragment.class, null);
        if (!title.equals("")) {
            actionBar.setTitle(title);
        }
        return rootView;
    }

    private View getTabIndicator(Context context, int title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem settings = menu.findItem(R.id.action_settings);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_view_place, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_place:
                android.support.v4.app.Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("EDIT_PLACE", "Edit Place");

                fragment = new EditPlaceFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"EDIT_PLACE_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.share_place:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }
        return super.onOptionsItemSelected(item);
    }

}
