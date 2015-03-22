package com.hardygtw.travelmemories.fragments.Gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.adapters.GridViewImageAdapter;
import com.hardygtw.travelmemories.model.Photo;

import java.io.File;

public class GalleryFragment extends Fragment {


    private GridView gridView;
    private GridViewImageAdapter customGridAdapter;

    public GalleryFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        //gridView = (GridView) findViewById(R.id.gridView);
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        customGridAdapter = new GridViewImageAdapter(getActivity(), R.layout.grid_view_item, SQLDatabaseSingleton.getInstance(getActivity()).getTravelGalleryPhotos());
        gridView.setAdapter(customGridAdapter);
        gridView.setClickable(true);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                final Photo photo =  customGridAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setItems(R.array.long_hold_item_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Uri selectedImage = Uri.fromFile(new File(photo.getPath().replace("file:///","")));
                        Log.d("SELECT IMAGE URI", selectedImage.getPath());

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                        sharingIntent.setType("image/jpeg");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, selectedImage);
                        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                    }
                });

                builder.create();

                builder.show();

                return true;
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
        menuInflater.inflate(R.menu.menu_gallery, menu);


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