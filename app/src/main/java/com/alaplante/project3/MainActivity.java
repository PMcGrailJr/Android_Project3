package com.alaplante.project3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private int hiScore = 0, latestScore = 0;
    public static final String QUIZ_LENGTH = "pref_quizLength";
    public static final String EXTREME_MODE = "pref_extremeMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //use ft to load main menu
        loadMainMenu();

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
