package com.hardygtw.travelmemories.fragments.Gallery;


import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.fragments.Places.ViewPlaceGalleryFragment;
import com.hardygtw.travelmemories.fragments.Trip.NewTripFragment;
import com.hardygtw.travelmemories.fragments.Trip.ViewTripGalleryFragment;
import com.hardygtw.travelmemories.model.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoFragment extends Fragment {

    private ImageView photoView;
    private TextView photoTag;
    private Uri uri;
    private Photo photo;
    private ActionBar actionBar;
    private int trip_id = 0;
    private int place_id = 0;

    public PhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo,container, false);


        photo = (Photo) getArguments().getSerializable("PHOTO");
        actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        photoView =  (ImageView) rootView.findViewById(R.id.photo_view);
        photoTag = (TextView) rootView.findViewById(R.id.tag);

        if (getArguments().get("TRIP_ID") != null) {
            trip_id = getArguments().getInt("TRIP_ID");
        }

        if (getArguments().get("PLACE_ID") != null) {
            place_id = getArguments().getInt("PLACE_ID");
        }

        ImageLoader imgLoader = ImageLoader.getInstance();
        imgLoader.displayImage(photo.getPath(), photoView);
        return rootView;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_accept:

                SQLDatabaseSingleton.getInstance(getActivity()).createPhoto(photo.getPath(), place_id, trip_id, photoTag.getText().toString());

                FragmentManager fm = getFragmentManager();

                if (trip_id > 0) {
                   fm.popBackStack();
                } else if (place_id > 0) {
                   fm.popBackStack();
                } else {
                    Fragment fragment = null;
                    FragmentTransaction ft = fm.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("NEW_PHOTO", "New Photo");

                    fragment = new GalleryFragment();
                    fragment.setArguments(bundle);

                    ((MainActivity)getActivity()).clearBackStack();

                    ft.replace(R.id.frame_container, fragment,"GALLERY_FRAGMENT");
                    ft.commitAllowingStateLoss();
                }

                break;
            case R.id.add_cancel:
                ((MainActivity)getActivity()).goBackFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_add_photo, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
