package com.hardygtw.travelmemories.fragments.Places;

/**
 * Created by gavin on 17/02/2015.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.model.PlaceVisited;
import com.hardygtw.travelmemories.model.Trip;

public class ViewPlaceDetailsFragment extends Fragment {

    ImageView imgFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.view_place_details, container, false);

        int place_id = getArguments().getInt("PLACE_VISIT_ID");

        TextView placeName = (TextView) rootView.findViewById(R.id.place_name);
        TextView dateVisited = (TextView) rootView.findViewById(R.id.dateVisited);
        TextView location = (TextView) rootView.findViewById(R.id.location);
        TextView notes = (TextView) rootView.findViewById(R.id.place_notes);
        TextView companions = (TextView) rootView.findViewById(R.id.companion_name);


        PlaceVisited place = SQLDatabaseSingleton.getInstance(getActivity()).getPlaceDetails(place_id);

        placeName.setText(place.getPlaceName());
        dateVisited.setText(place.getDateVisited());
        location.setText(place.getAddress());
        notes.setText(place.getTravellerNotes());
        companions.setText(place.getTravelCompanions());
        return rootView;
    }

    public void open() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        imgFavorite.setImageBitmap(bp);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_places, menu);
    }
}

