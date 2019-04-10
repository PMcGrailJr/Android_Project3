package com.alaplante.project3;

import android.content.res.AssetManager;
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
    private Card currentFlippedImage; //if card is already flipped, this stores the image name
    private boolean gameFinished = false;
    private int currentSelection;
    private LinearLayout gameScreenContainer;
    private boolean cardFlipped = false; //determines if first or second card
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
        String[] imageNames = {"bear", "butterfly", "coyote", "dog", "dolphin", "donkey", "hippo", "ram"};
        String[] imageAssignments =  {"bear", "butterfly", "coyote", "dog", "dolphin", "donkey", "hippo", "ram", "bear", "butterfly", "coyote", "dog", "dolphin", "donkey", "hippo", "ram"};
        //String[] imageAssignments = new String[2*imageNames.length];
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

        setQuizTimer("45 seconds");

        // start the quiz timer and load first question
        startDisplayTimer();

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
        quizTimer = null;
        displayTimer = null;
        //((MainActivity) getActivity()).setScore(calculateScore());
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

        if (cards[clickedCard].isActive()) cards[clickedCard].DisplayFront();
        checkMatching(clickedCard);

        //try { TimeUnit.SECONDS.sleep(1); } catch(Exception e) {}

        //Toast.makeText(getActivity(), "" + v.getResources().getResourceEntryName(v.getId()), Toast.LENGTH_SHORT).show();
    }

    public void checkMatching(int clickedCard) {

        if (cardFlipped)  {
            numberOfGuesses++;
            if (cards[clickedCard].getImageName() == currentFlippedImage.getImageName()) {
                numberOfMatches ++;
                Toast.makeText(getActivity(), "Match!", Toast.LENGTH_SHORT).show();
                currentFlippedImage.deactivate();
                cards[clickedCard].deactivate();
            } else {
                Toast.makeText(getActivity(), "Not a Match", Toast.LENGTH_SHORT).show();
                currentFlippedImage.DisplayBack();
                cards[clickedCard].DisplayBack();
            }
            cardFlipped = false;
        } else {
            cardFlipped = true;
            currentFlippedImage = cards[clickedCard];
        }

        if (numberOfMatches == 8) finishQuiz();

    }

    public int ScoreGame(){
        double initscore;
        //(number of matches/number of guesses)+(remaining time/total time)
        initscore = ((numberOfMatches/numberOfGuesses)+(remainingQuizTime/defaultQuizTime))*100;
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

}

