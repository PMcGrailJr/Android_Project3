package com.alaplante.project3;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

public class Card {

    private String imageName;
    private boolean matched; //determines if card has been matched already
    private String back = "image0.png";//image name for back of card for when it's flipped back over
    private ImageView imageView; //this is basically the location in the grid.
    private int ID;
    private boolean active;
    FragmentActivity activity;
    AssetManager assets;

    public Card(ImageView inputView, String inputImageName, int inputID, FragmentActivity inputactivity) {
        imageView = inputView;
        imageName = inputImageName;
        activity = inputactivity;
        ID = inputID;
        active = true;
        assets = activity.getAssets();
        //AssetManager assets = getActivity(GameScreenFragment).getAssets();
        //DisplayFront();
        //try { Thread.sleep(3000); } catch(Exception e) {}
        DisplayBack();
        //may display front initially

    }
        public void DisplayFront(){
            try(InputStream stream =
                    assets.open(imageName+".png")){
                Drawable card = Drawable.createFromStream(stream, imageName);
                imageView.setImageDrawable(card);
            }
            catch(IOException exception){
                Log.e(TAG,"Error loading image", exception);
            } finally {
                //try { Thread.sleep(3000); } catch(Exception e) {}
               //GameScreenFragment.SIGNAL();
            }
        }

        public void DisplayBack () {
            try (InputStream stream =
                         assets.open("cardBack.png")) {
                Drawable card = Drawable.createFromStream(stream, "image0");
                imageView.setImageDrawable(card);
            } catch (IOException exception) {
                Log.e(TAG, "Error loading image", exception);
            }
        }

        public void activate(){
            active=true;
        }

        public void deactivate(){
            active = false;
            try (InputStream stream =
                         assets.open(imageName+".png")){
                Drawable card = Drawable.createFromStream(stream, imageName);
                imageView.setImageDrawable(card);
            } catch (IOException exception) {
                Log.e(TAG, "Error loading image", exception);
            }
        }

        public String getImageName () {
            return imageName;
        }
        public int getID(){
            return ID;
        }
        public boolean isActive(){
            return active;
        }


}
