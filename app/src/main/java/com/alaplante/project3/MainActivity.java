package com.alaplante.project3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int hiScore = 0, latestScore = 0;
    public static final String QUIZ_LENGTH = "pref_quizLength";
    public static final String EXTREME_MODE = "pref_extremeMode";
    public List<String> scores;
    public List<Integer> scoreValues;
    public List<String> images;
    public List<String> imageValues;
    public static HiScoresAdapter adapter;
    private SharedPreferences hiScores;
    private SharedPreferences appImages;
    private final String[] defaultImages = {"blackCat1", "hans1", "orangeCat", "puppy1", "puppy2", "whiskey1", "whiskey2", "whiskey3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get app images from shared preferences
        appImages = getSharedPreferences("appimages", MODE_PRIVATE);

        // store the image in an ArrayList
        images = new ArrayList<>(appImages.getAll().keySet());

        // if the shared preference hasn't been initialize, add all default images
        if (images.size() < 8) {
            SharedPreferences.Editor preferencesEditor = appImages.edit();
            for (int i = 0; i < defaultImages.length; i++) {
                preferencesEditor.putString("image" + (i+1), defaultImages[i] + ".png");
            }
            preferencesEditor.apply();
        }

        String tempImage;
        imageValues = new ArrayList<>();

        // get actual image values to call from game screen
        for (int i = 0; i < images.size(); i++) {
            tempImage = appImages.getString(images.get(i), "");
            imageValues.add(tempImage);
        }

        // get hi-scores from shared preferences
        hiScores = getSharedPreferences("hiscores", MODE_PRIVATE);

        // stored the saved scores in an ArrayList then sort them
        scores = new ArrayList<>(hiScores.getAll().keySet());
        Collections.sort(scores, String.CASE_INSENSITIVE_ORDER);

        int tempScore;
        scoreValues = new ArrayList<>();

        for (int i = 0; i < scores.size(); i++) {

            tempScore = hiScores.getInt(scores.get(i), 0);
            scoreValues.add(tempScore);

        }

        Collections.sort(scoreValues);
        Collections.reverse(scoreValues);

        // create RecyclerView.Adapter to bind tags to the RecyclerView
        adapter = new HiScoresAdapter(scoreValues);

        //use ft to load main menu
        loadMainMenu();

    }

    // add new hi score to file
    public void addHiScore(int score, String tag) {
        // get SharedPreference.Editor to store new hi score
        SharedPreferences.Editor preferencesEditor = hiScores.edit();
        preferencesEditor.putInt(tag, score); // store current search
        preferencesEditor.apply(); // store the updated preferences

        if (!scoreValues.contains(score)){
            scoreValues.add(score);
            Collections.sort(scoreValues);
            Collections.reverse(scoreValues);
            adapter.notifyDataSetChanged();

        }
    }

    // show menu if app is running on a phone or a portrait-oriented tablet
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get the device's current orientation
        int orientation = getResources().getConfiguration().orientation;

        // display the app's menu only in portrait orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            // inflate the menu
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        } else {
            return false;
        }

    }

    // displays the SettingsActivity when running on a phone
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadMainMenu(){
        MainMenuFragment newMainMenuFragment = new MainMenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, newMainMenuFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void loadGameScreen(){
        GameScreenFragment newGameScreenFragment = new GameScreenFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, newGameScreenFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void loadScoreScreen(){
        ScoreScreenFragment newScoreScreenFragment = new ScoreScreenFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, newScoreScreenFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void loadHiScoresScreen(){
        HiScoresFragment newHiScoresScreenFragment = new HiScoresFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, newHiScoresScreenFragment);
        ft.addToBackStack(null);
        ft.commit();

    }
    public void loadCustomizeImagesScreen(){
        CustomizeImagesFragment newCustomizeImagesScreenFragment= new CustomizeImagesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, newCustomizeImagesScreenFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void loadHowToScreen(){
        HowToFragment newHowToFragment = new HowToFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, newHowToFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void setScore(int newScore){
        latestScore = newScore;
    }

    public int getScore() {
        return latestScore;
    }

    public int getHiScore() {
        return hiScore;
    }

    public boolean checkNewHiScore(int newScore) {
        if (newScore > hiScore) {
            hiScore = newScore;
            return true;
        } else {
            return false;
        }
    }

    public String getQuizLength() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String quizLength = sharedPreferences.getString(QUIZ_LENGTH, null);
        return quizLength;
    }

    public Boolean getExtremeMode() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean extremeMode = sharedPreferences.getBoolean(EXTREME_MODE, true);
        return extremeMode;
    }
    

}
