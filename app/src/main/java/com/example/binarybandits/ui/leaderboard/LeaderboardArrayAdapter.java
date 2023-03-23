package com.example.binarybandits.ui.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * array adapter for leaderboard list
 * updates view
 */
public class LeaderboardArrayAdapter extends ArrayAdapter {
        private boolean scoreLeaderboard;
        public LeaderboardArrayAdapter(Context context, ArrayList<Player> players, boolean scoreLeaderboard) {
            super(context, 0, players);
            this.scoreLeaderboard = scoreLeaderboard;
        }

    /**
     * get view for leaderboards items xml
     * add item to list with fields
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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
            ImageView image1 = view.findViewById(R.id.leaderboard_image_list);
            playerName.setText(player.getUsername());
            if (scoreLeaderboard) {
                score.setText(Integer.toString(player.getTotalScore()));
            }
            else {
                score.setText(Integer.toString(player.getHighestScore()));
            }
            rank.setText("#" + Integer.toString(position+4));
            String url1 = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + player.getUsername();
            Picasso.get().load(url1).into(image1);
            return view;
        }
    }

