package com.example.binarybandits.ui.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;

import java.util.ArrayList;

public class LeaderboardArrayAdapter extends ArrayAdapter {
        public LeaderboardArrayAdapter(Context context, ArrayList<Player> players) {
            super(context, 0, players);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup
                parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_leaderboard_items,
                        parent, false);
            } else {
                view = convertView;
            }
            // add items to listview
            Player player = (Player) getItem(position);
            TextView playerName = view.findViewById(R.id.player_name_text);
            TextView score = view.findViewById((R.id.score_Player));
            TextView rank = view.findViewById(R.id.rank_number);
            playerName.setText(player.getUsername());
            score.setText(Integer.toString(player.getTotalScore()));
            rank.setText("#" + Integer.toString(position+4));
            return view;
        }
    }

