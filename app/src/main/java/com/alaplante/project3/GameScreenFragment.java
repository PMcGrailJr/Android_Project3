package com.alaplante.project3;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class GameScreenFragment extends Fragment {

    private long defaultQuizTime, remainingQuizTime;
    private TextView quizTimerText;
    private String timeString, toastText;
    private int flipCardCount = 16;
    private CountDownTimer quizTimer;
    private boolean gameFinished = false;
    private LinearLayout gameScreenContainer;
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

        for (int i = 1; i <= flipCardCount; i++) {
            ImageView flipCard = view.findViewById(flipCards[i]);
            flipCard.setOnClickListener(new View.OnClickListener() {
               public void onClick(View v){
                   Toast.makeText(getActivity(), "" + v.getResources().getResourceEntryName(v.getId()), Toast.LENGTH_SHORT).show();
               }
            });
        }



        setQuizTimer("45 seconds");

        // start the quiz timer and load first question
        startQuizTimer();

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
        //((MainActivity) getActivity()).setScore(calculateScore());
        ((MainActivity) getActivity()).loadScoreScreen();
    }



}
