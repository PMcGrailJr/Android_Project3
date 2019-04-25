package com.alaplante.project3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.content.res.AssetManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class CustomizeImagesFragment extends Fragment {
    private int imageCount = 8;
    private String currentResource;

    final int[] changeImageButtons = {
            R.id.changeImageButton1,
            R.id.changeImageButton2,
            R.id.changeImageButton3,
            R.id.changeImageButton4,
            R.id.changeImageButton5,
            R.id.changeImageButton6,
            R.id.changeImageButton7,
            R.id.changeImageButton8,
    };
    final int[] changeImageViews={
            R.id.changeImageView1,
            R.id.changeImageView2,
            R.id.changeImageView3,
            R.id.changeImageView4,
            R.id.changeImageView5,
            R.id.changeImageView6,
            R.id.changeImageView7,
            R.id.changeImageView8,

    };
    //View view;
    public static final int PICK_IMAGE = 1;
    String[] imageNames = {"blackCat1", "hans1", "orangeCat", "puppy1", "puppy2", "whiskey1", "whiskey2", "whiskey3"};
    public List<String> images;
    public List<String> imageValues;
    private SharedPreferences appImages;

    public CustomizeImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.customize_images_screen, container, false);
        loadImages(view);

        // Inflate the layout for this fragment
        return view;
    }

    public void loadImages(View view) {

        // get app images from shared preferences
        appImages = getActivity().getSharedPreferences("appimages", MODE_PRIVATE);

        // store the image in an ArrayList
        images = new ArrayList<>(appImages.getAll().keySet());
        Collections.sort(images);

        // declare tempImage variable, and imageValues array list
        String tempImage;
        imageValues = new ArrayList<>();

        // get actual image values to call from game screen
        for (int i = 0; i < images.size(); i++) {
            tempImage = appImages.getString(images.get(i), "");
            imageValues.add(tempImage);
        }

        // initialize asset manager for default images
        AssetManager assets =  getActivity().getAssets();

        // loop through setting each image
        for(int i=0; i<imageCount;i++){
            ImageView customImage = view.findViewById(changeImageViews[i]);
            final Button changeButton = view.findViewById(changeImageButtons[i]);

            // get image extension to determine if default or custom, handle differently
            String imageExtension = imageValues.get(i).substring(imageValues.get(i).length() - 4);

            // if image is default
            if (imageExtension.equals(".png")) {
                // try to load default images
                try(InputStream stream = assets.open(imageValues.get(i))){
                    Drawable card = Drawable.createFromStream(stream, imageValues.get(i));
                    customImage.setImageDrawable(card);
                }
                catch(IOException exception){
                    Log.e(TAG,"Error loading image", exception);

                }

            // if image is custom
            } else {

                // try loading custom images
                try {
                    Uri uri;
                    uri = Uri.parse(imageValues.get(i));
                    //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Bitmap yourSelectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    customImage.setImageBitmap(yourSelectedImage);
                } catch(IOException exception) {
                    Log.e(TAG,"Error loading image", exception);
                }
            }


            // on click listener for each change button, creates intent to allow user to select custom image
            changeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String clickedResource = getResources().getResourceEntryName(v.getId());
                    currentResource = clickedResource.substring(clickedResource.length() - 1);
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    String[] mimeTypes = {"image/png"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            });
        }
    }


    @Override  // manages image after bing picked by user
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // get reference to associated image
        String changeImageText = "changeImageView" + currentResource;
        int resId = getResources().getIdentifier(changeImageText, "id", "com.alaplante.project3");
        ImageView clickedImage = getActivity().findViewById(resId);

        super.onActivityResult(requestCode, resultCode, data);

        // after picking succesfully
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {

            // try to load image URI and set image on screen, store image in shared preferences
            try {
                // get uri, convert to string to store in saved preferences. get image replacement ref for stored preferences
                Uri selectedImage = data.getData();
                String stringURI = selectedImage.toString();
                String prefImage = "image" + currentResource;

                // create bitmap of selected image
                Bitmap yourSelectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                try {
                    // try setting bitmap of custom image to associated change image view
                    clickedImage.setImageBitmap(yourSelectedImage);
                } catch (Exception exception) {
                    Log.e(TAG,"Error setting image", exception);
                }

                // call function to add custom image to preferences
                addCustomImage(stringURI, prefImage);

            } catch(IOException exception) {
                Log.e(TAG,"Error loading image", exception);
            }
        }
    }

    // add new hi score to file
    public void addCustomImage(String path, String tag) {
        // get SharedPreference.Editor to store new hi score
        SharedPreferences.Editor preferencesEditor = appImages.edit();
        preferencesEditor.putString(tag, path); // store current search
        preferencesEditor.apply(); // store the updated preferences

    }

}
