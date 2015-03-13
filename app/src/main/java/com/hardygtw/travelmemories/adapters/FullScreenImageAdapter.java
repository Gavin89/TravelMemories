package com.hardygtw.travelmemories.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.model.Photo;
import com.hardygtw.travelmemories.views.TouchImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;


public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Photo> photos;

    // constructor
    public FullScreenImageAdapter(Activity activity, ArrayList<Photo> photos) {
        this.activity = activity;
        this.photos = photos;

    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.activity_full_screen_image, container, false);

        TouchImageView imageView = (TouchImageView) viewLayout.findViewById(R.id.imageView);
        ImageButton closeImageButton = (ImageButton) viewLayout.findViewById(R.id.closeImageButton);

        Photo item = photos.get(position);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        Bitmap loadedImage = ImageLoader.getInstance().loadImageSync("file:///" + item.getPath(), options);
        imageView.setImageBitmap(loadedImage);

        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
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
