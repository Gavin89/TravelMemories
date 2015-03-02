package com.hardygtw.travelmemories.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.adapters.FullScreenImageAdapter;

public class FullScreenImageActivity extends Activity {

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new FullScreenImageAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }
}
