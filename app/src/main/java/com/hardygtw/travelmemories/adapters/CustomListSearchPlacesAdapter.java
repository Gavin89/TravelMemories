package com.hardygtw.travelmemories.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.model.GooglePlace;


public class CustomListSearchPlacesAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<GooglePlace> items;

    public CustomListSearchPlacesAdapter(Activity activity, List<GooglePlace> items) {
        this.activity = activity;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_search_places, null);

        TextView  placeName = (TextView) convertView.findViewById(R.id.search_place_name);
        TextView  placeCategories = (TextView) convertView.findViewById(R.id.search_place_categories);
        TextView placeCityCountry = (TextView) convertView.findViewById(R.id.search_place_city_country);


        GooglePlace place = (GooglePlace) items.get(position);

      placeName.setText(place.getName());
      placeCategories.setText(place.getCategories());
      placeCityCountry.setText(place.getFormatted_address());

        return convertView;
    }


}
