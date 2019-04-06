package com.alaplante.project3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuFragment extends Fragment {

    private TextView hiScoreText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        final Button newGameButton = view.findViewById(R.id.newGameButton);
        hiScoreText = view.findViewById(R.id.mainMenuHiScoreText);
       // hiScoreText.setText("" + ((MainActivity) getActivity()).getHiScore());

        newGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Toast.makeText(getActivity(), "New Game", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).loadGameScreen();

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
