package com.hardygtw.travelmemories.fragments.Places;


import android.app.ActionBar;
import android.content.Context;
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


public class EditPlaceFragment extends Fragment {

    private ActionBar actionBar;
    private FragmentTabHost mTabHost;

    public EditPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab_list,container, false);
        String title = getArguments().getString("EDIT_PLACE");
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();
        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_details)),
                EditPlaceDetailsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_gallery)),
                EditPlaceGalleryFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_companions)),
                EditPlaceCompanionsFragment.class, null);
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
        settings.setVisible(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_add_place, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_cancel:
                Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("NEW_PLACE", "Places Visited");

                fragment = new PlaceListFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"NEW_PLACE_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
