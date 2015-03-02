package com.hardygtw.travelmemories.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.views.TouchImageView;


public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.full_screen_image, container, false);

        TouchImageView imageView = (TouchImageView) viewLayout.findViewById(R.id.imageView);
        ImageButton closeImageButton = (ImageButton) viewLayout.findViewById(R.id.closeImageButton);
        ImageButton editPhotoImageButton = (ImageButton) viewLayout.findViewById(R.id.editPhotoImageButton);

        imageView.setImageResource(R.drawable.no_image);

        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        editPhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MainActivity.class);
                activity.startActivity(i);
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
