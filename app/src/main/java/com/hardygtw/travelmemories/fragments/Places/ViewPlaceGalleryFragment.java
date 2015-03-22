package com.hardygtw.travelmemories.fragments.Places;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.hardygtw.travelmemories.R;
import com.hardygtw.travelmemories.SQLDatabaseSingleton;
import com.hardygtw.travelmemories.activity.MainActivity;
import com.hardygtw.travelmemories.adapters.GridViewImageAdapter;
import com.hardygtw.travelmemories.fragments.Gallery.PhotoFragment;
import com.hardygtw.travelmemories.model.ImageItem;
import com.hardygtw.travelmemories.model.Photo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gavin on 12/02/2015.
 */
public class ViewPlaceGalleryFragment extends Fragment{

    private GridView gridView;
    private GridViewImageAdapter customGridAdapter;
    private Button place_take_photo;
    private Button place_add_photo;
    private int trip_id = 0;
    private int place_id = 0;

    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_IMAGE_GALLERY = 3;

    String mCurrentPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.view_place_gallery, container, false);
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        place_id = getParentFragment().getArguments().getInt("PLACE_ID");
        customGridAdapter = new GridViewImageAdapter(getActivity(), R.layout.grid_view_item, SQLDatabaseSingleton.getInstance(getActivity()).getPlacePhotos(place_id));
        gridView.setAdapter(customGridAdapter);

        gridView.setClickable(true);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                final Photo photo =  customGridAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setItems(R.array.long_hold_item_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Uri selectedImage = Uri.fromFile(new File(photo.getPath()));
                        Log.d("SELECT IMAGE URI", selectedImage.getPath());

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                        sharingIntent.setType("image/jpeg");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, selectedImage);
                        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                    }
                });

                builder.create();

                builder.show();

                return true;
            }
        });

        place_take_photo=(Button) rootView.findViewById(R.id.capture_place_photo);
        place_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        place_add_photo=(Button) rootView.findViewById(R.id.add_place_photo);
        place_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchGalleryIntent();
            }
        });

        if (getParentFragment().getArguments().get("TRIP_ID") != null) {
            trip_id = getParentFragment().getArguments().getInt("TRIP_ID");
        }


        if (getParentFragment().getArguments().get("PLACE_ID") != null) {
            place_id = getParentFragment().getArguments().getInt("PLACE_ID");
        }

        return rootView;

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchGalleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // Ensure that there's a camera activity to handle the intent
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                i.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                getActivity().startActivityForResult(i, REQUEST_IMAGE_GALLERY);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Photo photo = new Photo();
            photo.setPath("file:///" +mCurrentPhotoPath);

            android.support.v4.app.Fragment fragment = null;
            FragmentManager fm = getParentFragment().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("TAKE_PHOTO", "Take Photo");
            bundle.putSerializable("PHOTO", photo);
            bundle.putInt("PLACE_ID", place_id);
            fragment = new PhotoFragment();
            fragment.setArguments(bundle);

            ft.replace(R.id.frame_container, fragment,"PHOTO_FRAGMENT");
            ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == getActivity().RESULT_OK) {
            Photo photo = new Photo();
            String realPath = "file://"+getRealPathFromURI(data.getData(), getActivity());
            photo.setPath(realPath);
            android.support.v4.app.Fragment fragment = null;
            FragmentManager fm = getParentFragment().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("TAKE_PHOTO", "Take Photo");
            bundle.putSerializable("PHOTO", photo);
            bundle.putInt("PLACE_ID", place_id);

            fragment = new PhotoFragment();
            fragment.setArguments(bundle);

            ft.replace(R.id.frame_container, fragment,"PHOTO_FRAGMENT");

            ft.commitAllowingStateLoss();
        }
    }

    public  String getRealPathFromURI(Uri contentUri, Activity activity) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}

