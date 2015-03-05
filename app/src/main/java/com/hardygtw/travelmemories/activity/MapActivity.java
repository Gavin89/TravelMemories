package com.hardygtw.travelmemories.activity;

import android.support.v4.app.Fragment;

import uk.ac.aston.cs3040.rssreaderwithmaps.fragment.RssMapFragment;
import uk.ac.aston.cs3040.rssreaderwithmaps.model.GeoRssLocation;
import uk.ac.aston.cs3040.rssreaderwithmaps.model.RssItem;

/**
 * Created by beaumoaj on 03/02/15.
 */
public class MapActivity extends SingleFragmentActivity {
    public static final String ITEM = "aston.cs3040.item";

    @Override
    protected Fragment createFragment() {
        RssItem item = (RssItem) getIntent().getSerializableExtra(ITEM);
        if (item != null) {
            return RssMapFragment.newInstance(item);
        } else {
            return new RssMapFragment();
        }
    }

}