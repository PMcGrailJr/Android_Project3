package com.alaplante.project3;

import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import static android.content.ContentValues.TAG;


public class GameScreenFragment extends Fragment {

    private long defaultQuizTime, remainingQuizTime;
    private int numberOfGuesses = 0;
    private int numberOfMatches = 0;
    private TextView quizTimerText;
    private String timeString, toastText;
    private int flipCardCount = 16;
    private CountDownTimer quizTimer;
    private CountDownTimer displayTimer;
    private CountDownTimer flipTimer;
    private Card currentFlippedImage; //if card is already flipped, this stores the image name
    private boolean gameFinished = false;
    private int currentSelection;
    private LinearLayout gameScreenContainer;
    private boolean cardFlipped = false; //determines if first or second card
    private boolean secondCardFlipped = false;
    private boolean extremeMode;
    final int[] flipCards = {
            0,
            R.id.flipCard1,
            R.id.flipCard2,
            R.id.flipCard3,
            R.id.flipCard4,
            R.id.flipCard5,
            R.id.flipCard6,
            R.id.flipCard7,
            R.id.flipCard8,
            R.id.flipCard9,
            R.id.flipCard10,
            R.id.flipCard11,
            R.id.flipCard12,
            R.id.flipCard13,
            R.id.flipCard14,
            R.id.flipCard15,
            R.id.flipCard16
    };
    private Card[] cards = new Card[16];


    public GameScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "New Game", Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_game_screen, container, false);
        quizTimerText = view.findViewById(R.id.quizTimerText);
        gameScreenContainer = view.findViewById(R.id.gameScreenContainer);
        final AssetManager assets = getActivity().getAssets();
        //String[] imageNames = {"bear", "butterfly", "coyote", "dog", "dolphin", "donkey", "hippo", "ram"};
        //String[] imageAssignments =  {"bear", "butterfly", "coyote", "dog", "dolphin", "donkey", "hippo", "ram", "bear", "butterfly", "coyote", "dog", "dolphin", "donkey", "hippo", "ram"};
        //String[] imageAssignments = new String[2*imageNames.length];
        String[] imageNames = {"blackCat1", "hans1", "orangeCat", "puppy1", "puppy2", "whiskey1", "whiskey2", "whiskey3"};
        String[] imageAssignments = {"blackCat1", "hans1", "orangeCat", "puppy1", "puppy2", "whiskey1", "whiskey2", "whiskey3", "blackCat1", "hans1", "orangeCat", "puppy1", "puppy2", "whiskey1", "whiskey2", "whiskey3"};
        randomize(imageNames, imageAssignments);
        /*for(int i = 0; i<flipCardCount; i++){
            Log.e(TAG, imageAssignments[i]);
        }*/
        for (int i = 1; i <= flipCardCount; i++) {
            ImageView flipCard = view.findViewById(flipCards[i]);
            flipCard.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v){
                   flipCard(v);
               }
            });
            //String[] imageNames = {"image1", "image2", "image3", "image4", "image5", "image6", "image7", "image8"};
            //String[] imageAssignments =  {"image1", "image2", "image3", "image4", "image5", "image6", "image7", "image8","image1", "image2", "image3", "image4", "image5", "image6", "image7", "image8"};
            //String[] imageAssignments = new String[2*imageNames.length];
            //randomize(imageNames, imageAssignments);
            cards[i-1]= new Card(flipCard, imageAssignments[i-1], flipCards[i], getActivity());
        }

        // load selected game time, set timer
        String selectedGameTime = ((MainActivity) getActivity()).getQuizLength();
        setQuizTimer(selectedGameTime);



        // load extreme mode boolean, make display timer decision
        extremeMode = ((MainActivity) getActivity()).getExtremeMode();
        if(!extremeMode) {
            startDisplayTimer();
        } else {
            startQuizTimer();
        }

        // Inflate the layout for this fragment
        return view;
    }

    // sets quiz timer based on preferences
    public void setQuizTimer(String selectedTime){

        if (selectedTime.equals("15 seconds")) {
            defaultQuizTime = 15;

        } else if (selectedTime.equals("30 seconds")) {
            defaultQuizTime = 30;

        } else if (selectedTime.equals("45 seconds")) {
            defaultQuizTime = 45;

        }

        remainingQuizTime = defaultQuizTime * 1000;

    }

    public void startDisplayTimer() {

        for (int i = 0; i < flipCardCount; i++) {
            cards[i].DisplayFront();
        }

        displayTimer = new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                for (int i = 0; i < flipCardCount; i++) {
                    cards[i].DisplayBack();
                }

                startQuizTimer();

            }

        };

        displayTimer.start();

    }

    public void startFlipTimer(final int clickedCard) {

        if (cards[clickedCard].isActive()) cards[clickedCard].DisplayFront();

        flipTimer = new CountDownTimer(250, 250) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                checkMatching(clickedCard);

            }

        };

        flipTimer.start();
    }

    // starts quiz timer
    public void startQuizTimer() {

        quizTimer = new CountDownTimer(remainingQuizTime, 1000){

            public void onTick(long millisUntilFinished) {

                long minutesRemaining = (millisUntilFinished / 1000) / 60;
                long secondsRemaining = (millisUntilFinished / 1000) % 60;
                if (secondsRemaining < 10) timeString = minutesRemaining + ":0" + secondsRemaining;
                else timeString = minutesRemaining + ":" + secondsRemaining;
                remainingQuizTime = millisUntilFinished;
                quizTimerText.setText(timeString);
            }

            public void onFinish(){
                finishQuiz();
            }

        };

        quizTimer.start();

    }

    // reports hi score back to main activity
    public void finishQuiz() {
        gameFinished = true;
        quizTimer.cancel();
        if(!extremeMode) displayTimer.cancel();
        ((MainActivity) getActivity()).setScore(calculateScore());
        ((MainActivity) getActivity()).addHiScore(calculateScore(), getCurrentDate());
        ((MainActivity) getActivity()).loadScoreScreen();
    }


    public void flipCard(View v) {
        int clickedCard = -99;
        for (int j = 0; j < flipCardCount; j++) {
            if (v.getId() == cards[j].getID()) {
                currentSelection = j;
                clickedCard = j;
            }
        }
        startFlipTimer(clickedCard);
    }

    public void checkMatching(int clickedCard) {

        if (cardFlipped)  {
            secondCardFlipped = true;
            numberOfGuesses++;
            if (cards[clickedCard].getImageName() == currentFlippedImage.getImageName()) {
                numberOfMatches ++;
                MediaPlayer good = MediaPlayer.create(this.getActivity(), R.raw.good);
                good.start();
                //Toast.makeText(getActivity(), "Match!", Toast.LENGTH_SHORT).show();
                currentFlippedImage.deactivate();
                cards[clickedCard].deactivate();
            } else {
                //Toast.makeText(getActivity(), "Not a Match", Toast.LENGTH_SHORT).show();
                MediaPlayer bad = MediaPlayer.create(this.getActivity(), R.raw.bad);
                bad.start();
                currentFlippedImage.DisplayBack();
                cards[clickedCard].DisplayBack();
            }
            cardFlipped = false;
            secondCardFlipped = false;
        } else {
            cardFlipped = true;
            currentFlippedImage = cards[clickedCard];
        }

        if (numberOfMatches == 8) finishQuiz();

    }

    public int calculateScore(){
        double initscore;
        //(number of matches/number of guesses)+(remaining time/total time)
        if (numberOfGuesses > 0) {
            initscore = ((numberOfMatches/numberOfGuesses)+(remainingQuizTime/defaultQuizTime))*100;
        } else {
            initscore = 0;
        }
        int finalScore = (int)initscore;
        return finalScore;
    }

    public void randomize(String[] images, String[] cardImages){
        int[] randArray = new int[cardImages.length];
        Random rand = new Random();
        //fill array with zeros
        for(int i=0; i<randArray.length; i++){
            randArray[i]= 0;
        }
        boolean done = false;
        int numOccurances = 0;
        int temp = -99;
        //fill each number of the array
        for(int i=0; i<randArray.length; i++){
            done=false;
            while(!done) {
                temp = rand.nextInt(8);
                numOccurances = 0;
                for (int j = 0; j < i; j++) {
                    if (temp == randArray[j]) {
                        numOccurances++;
                    }
                }
                if(numOccurances <  2){
                    done=true;
                }
            }
            Log.e(TAG, ""+ i+ " "+temp);
            randArray[i] = temp;
            cardImages[i]=images[temp];
        }
    }

    private String getCurrentDate() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        return todayAsString;
    }

}

