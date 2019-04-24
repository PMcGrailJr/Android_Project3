package com.alaplante.project3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class CustomizeImagesFragment extends Fragment {
    private int imageCount = 8;
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
        AssetManager assets =  getActivity().getAssets();
        for(int i=0; i<imageCount;i++){
            ImageView customImage = view.findViewById(changeImageViews[i]);
            final Button changeButton = view.findViewById(changeImageButtons[i]);
            try(InputStream stream =
                        assets.open(imageNames[i]+".png")){
                Drawable card = Drawable.createFromStream(stream, imageNames[i]);
                customImage.setImageDrawable(card);
            }
            catch(IOException exception){
                Log.e(TAG,"Error loading image", exception);
            }

            changeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //This is where the intent happens I guess
                   /* Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 0);*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }


                /*protected void onActivityResult(int requestCode, int resultCode, Intent data){
                    CustomizeImagesFragment.super.onActivityResult(requestCode, resultCode, data);
                    Log.e(TAG, data.toString());
                }*/
            });
        }
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //Log.e(TAG, data.toString());
            //Log.e(TAG, "this is the data"+ data.toString());
            Log.e(TAG, "this is the data"+ data.getData());
            /*AssetManager assets =  getActivity().getAssets();
            ImageView customImage = view.findViewById(changeImageViews[1]);
            try(InputStream stream =
                        assets.open(data.getData().toString())){
                Drawable card = Drawable.createFromStream(stream, imageNames[1]);
                customImage.setImageDrawable(card);
            }
            catch(IOException exception){
                Log.e(TAG,"Error loading image", exception);
            }*/
        }
    }
}
