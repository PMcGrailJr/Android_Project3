package com.alaplante.project3;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HiScoresFragment extends Fragment {

    private List<String> scores;
    private SharedPreferences hiScores;

    public HiScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.hi_scores_screen, container, false);

        // get reference to the RecyclerView to configure it
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // use a LinearLayoutManager to display items in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(MainActivity.adapter);

        // specify a custom ItemDecorator to draw lines between list items
        recyclerView.addItemDecoration(new ItemDivider(getActivity()));




        // Inflate the layout for this fragment
        return view;
    }




}
