package com.hardygtw.travelmemories.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.hardygtw.travelmemories.R;


/**
 * Created by beaumoaj on 03/02/15.
 */
public class MapActivity extends FragmentActivity{

        public static FragmentManager fragmentManager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
            fragmentManager = getSupportFragmentManager();
        }
    }