package com.hardygtw.travelmemories.fragments.Places;


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
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.model.PlaceVisited;
import com.hardygtw.travelmemories.model.Trip;


public class ViewPlaceFragment extends Fragment {

    private ActionBar actionBar;
    private FragmentTabHost mTabHost;
    private int place_id;
    private ShowcaseView sv;
    private PlaceVisited place;

    public ViewPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tab_list,container, false);
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.removeAllTabs();

        place_id = getArguments().getInt("PLACE_ID");
        place = SQLDatabaseSingleton.getInstance(getActivity()).getPlaceDetails(place_id);
        String title = place.getPlaceName();

        ((MainActivity)getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabFrameLayout);

        Bundle bundle = new Bundle(1);
        bundle.putInt("PLACE_ID", place_id);

        mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_details)),
                ViewPlaceDetailsFragment.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.place_gallery)),
                ViewPlaceGalleryFragment.class, null);
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
            case R.id.remove_place:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this place?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                 SQLDatabaseSingleton.getInstance(getActivity()).deletePlace(place_id);
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
            case R.id.edit_place:
                android.support.v4.app.Fragment fragment = null;
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("EDIT_PLACE", "Edit Place");
                bundle.putInt("PLACE_ID", place_id);

                fragment = new EditPlaceFragment();
                fragment.setArguments(bundle);

                ft.replace(R.id.frame_container, fragment,"EDIT_PLACE_FRAGMENT");
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.share_place:
                Intent sendIntent = new Intent();

                sendIntent.setAction(Intent.ACTION_SEND);

                String summary = "Place name: " + place.getPlaceName() + "\nPlace Address: " + place.getAddress() +  "\nDate Visited: " + place.getDateVisited()
                        + "\nCompanions: " + place.getTravelCompanions() + "\nNotes: " + place.getTravellerNotes();
                sendIntent.putExtra(Intent.EXTRA_TEXT, summary);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                break;
            case R.id.help_icon:
                sv =  new ShowcaseView.Builder(getActivity())
                        .setTarget(new ActionItemTarget(getActivity(), R.id.remove_place))
                        .setContentTitle("Delete Place")
                        .setContentText("This allows you to remove the current place")
                        .setStyle(R.style.CustomShowcaseTheme)
                        .hideOnTouchOutside()
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sv.hide();
                                new ShowcaseView.Builder(getActivity())
                                        .setTarget(new ActionItemTarget(getActivity(), R.id.edit_place))
                                        .setContentTitle("Edit Place")
                                        .setContentText("You can edit the current place")
                                        .hideOnTouchOutside()
                                        .setStyle(R.style.CustomShowcaseTheme2)
                                        .build();
                            }
                        })
                        .build();
        }
        return super.onOptionsItemSelected(item);
    }

}
