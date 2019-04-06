package com.alaplante.project3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ScoreScreenFragment extends Fragment {

    private TextView hiScoreText, scoreText;
    private boolean newHiScore;
    private int score;

    public ScoreScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_score_screen, container, false);

        //hiScoreText = view.findViewById(R.id.hiScoreText);
        scoreText = view.findViewById(R.id.scoreText);
        //score = ((MainActivity) getActivity()).getScore();
        //scoreText.setText("" + score);
        //if (((MainActivity) getActivity()).checkNewHiScore(score) == true) hiScoreText.setVisibility(View.VISIBLE);

        final Button mainMenuButton = view.findViewById(R.id.mainMenuButton);

        mainMenuButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ((MainActivity) getActivity()).loadMainMenu();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
