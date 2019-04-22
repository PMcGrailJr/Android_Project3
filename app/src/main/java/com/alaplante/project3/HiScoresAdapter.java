package com.alaplante.project3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HiScoresAdapter extends RecyclerView.Adapter<HiScoresAdapter.ViewHolder> {

    // List<String> used to obtain RecyclerView items' data
    private final List<Integer> scores; // search tags

    // constructor
    public HiScoresAdapter(List<Integer> tags) {
        this.scores = tags;
    }


    // nested subclass of RecyclerView.ViewHolder used to implement
    // the view-holder pattern in the context of a RecyclerView--the logic
    // of recycling views that have scrolled offscreen is handled for you
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        // configures a RecyclerView item's ViewHolder
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    // sets up new list item and its ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // inflate the list_item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);

        // create a ViewHolder for current item
        return (new ViewHolder(view));
    }

    // sets the text of the list item to display the search tag
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(scores.get(position)));
    }

    // returns the number of items that adapter binds
    @Override
    public int getItemCount() {
        return scores.size();
    }

    public int getMaxValue() { return Collections.max(scores);}

}
