package com.hardygtw.travelmemories.fragments.Gallery;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.adapters.GalleryAdapter;

public class GalleryFragment extends Fragment {


    private GridView gridView;
    private GalleryAdapter customGridAdapter;

    public GalleryFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        //gridView = (GridView) findViewById(R.id.gridView);
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        customGridAdapter = new GalleryAdapter(getActivity(), R.layout.grid_view_item, SQLDatabaseSingleton.getInstance(getActivity()).getTravelGalleryPhotos());
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), position + "#Selected",
                        Toast.LENGTH_SHORT).show();
            }

        });
        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){

        super.onCreateOptionsMenu(menu, menuInflater);
        //menuInflater.inflate(R.menu.trip_places_visited, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
    }




}