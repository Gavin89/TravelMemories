package com.hardygtw.travelmemories.fragments.Places;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.adapters.GridViewImageAdapter;
import com.hardygtw.travelmemories.model.ImageItem;

import java.util.ArrayList;

/**
 * Created by gavin on 12/02/2015.
 */
public class ViewPlaceGalleryFragment extends Fragment{

    private GridView gridView;
    private GridViewImageAdapter customGridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.view_place_gallery, container, false);
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        customGridAdapter = new GridViewImageAdapter(getActivity(), R.layout.grid_view_item, SQLDatabaseSingleton.getInstance(getActivity()).getTravelGalleryPhotos());
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

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        // retrieve String drawable array
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }

        return imageItems;

    }

}
