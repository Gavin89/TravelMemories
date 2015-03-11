package com.hardygtw.travelmemories.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.adapters.NavDrawerListAdapter;
import com.hardygtw.travelmemories.fragments.Gallery.GalleryFragment;
//import com.hardygtw.travelmemories.fragments.Nearby.GooglePlacesFragment;
import com.hardygtw.travelmemories.fragments.Nearby.GooglePlacesFragment;
import com.hardygtw.travelmemories.fragments.Nearby.NearbyFragment;
import com.hardygtw.travelmemories.fragments.Nearby.NearbyPlacesFragment;
import com.hardygtw.travelmemories.fragments.Places.PlaceListFragment;
import com.hardygtw.travelmemories.fragments.Trip.TripListFragment;
import com.hardygtw.travelmemories.model.NavDrawerItem;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    // camera
    protected static final int CAPTURE_IMAGE_CALLBACK = 0;
    protected static final int SELECT_PICTURE_CALLBACK = 1;

    public void changeIconColours() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup topLayout = (ViewGroup) findViewById(R.id.frame_container);
        topLayout.requestTransparentRegion(topLayout);

        changeIconColours();

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        // Trips
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Nearby
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Places Visited
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Gallery
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        //Take Photo
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowCustomEnabled(false);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name,
                R.string.app_name
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                goBackFragment();
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {

        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TripListFragment();
                break;
            case 1:
                fragment = new GooglePlacesFragment();
                break;
            case 2:
                fragment = new PlaceListFragment();
                break;
            case 3:
                fragment = new GalleryFragment();
                break;
            case 4:
                String[] addPhoto=new String[]{ "Camera" , "Gallery" };
                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(this);

                dialogBuilder.setTitle("Select Photo");

                dialogBuilder.setItems(addPhoto, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (id == 0) {
                            // Call camera Intent
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                        }
                        if (id == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                        }
                    }
                });

                dialogBuilder.setNeutralButton("Cancel",new android.content.DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }});
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    @Override
    public void onBackPressed() {
        if (!goBackFragment()) {
            super.onBackPressed();
        }
    }

    public boolean goBackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return false;
        } else {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayShowCustomEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getActionBar().removeAllTabs();
            getActionBar().setTitle(mTitle);
            fm.popBackStack();
            removeCurrentFragment();
            return true;
        }
    }

    public void removeCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        /**
         * Order these in which the furthest element in the UI is first.
         * e.g View Places Map is after View Trip so try to remove it first, then break out the loop
         * Break out of the loop when the current displayed element is found so it is removed from screen
         */
        Fragment[] currentFrags = new Fragment[8];

        currentFrags[0] = getSupportFragmentManager().findFragmentByTag("MAP_FRAGMENT");
        currentFrags[1] = getSupportFragmentManager().findFragmentByTag("EDIT_TRIP_FRAGMENT");
        currentFrags[2] = getSupportFragmentManager().findFragmentByTag("VIEW_PLACES_FRAGMENT");
        currentFrags[3] = getSupportFragmentManager().findFragmentByTag("VIEW_PLACE_FRAGMENT");
        currentFrags[4] = getSupportFragmentManager().findFragmentByTag("VIEW_TRIP_FRAGMENT");
        currentFrags[5] = getSupportFragmentManager().findFragmentByTag("NEW_TRIP_FRAGMENT");
        currentFrags[6] = getSupportFragmentManager().findFragmentByTag("NEW_PLACE_FRAGMENT");
        currentFrags[7] = getSupportFragmentManager().findFragmentByTag("EDIT_PLACE_FRAGMENT");

        boolean finished = false;

        for (int i = 0; i < currentFrags.length && finished == false; i++) {
            if (currentFrags[i] != null) {
                transaction.remove(currentFrags[i]);
                finished = true;
            }
        }

        transaction.commit();

    }
    public void newImageIntent(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_CALLBACK);
    }

    public void existingImageIntent(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SELECT_PICTURE_CALLBACK);
    }
}