package com.hardygtw.travelmemories.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.adapters.FullScreenImageAdapter;

public class FullScreenImageActivity extends Activity {

    private PagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        Intent i = getIntent();
        int position = i.getExtras().getInt("PHOTO_POSITION");

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new FullScreenImageAdapter(this, SQLDatabaseSingleton.getInstance(this).getTravelGalleryPhotos());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
