package com.example.binarybandits.ui.leaderboard;

import android.content.Context;
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
 * Represents the search fragment of the leaderboard page for the list of players
 */
public class LeaderboardSearchArrayAdapter extends ArrayAdapter {
    /**
     * Creates the view for the leaderboard search fragment player list
     * @param context - the context
     * @param players - the list of players
     */
    public LeaderboardSearchArrayAdapter(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }

    /**
     * Gets the view for the leaderboard search fragment player list
     * @param position - the position
     * @param convertView - the view to convert
     * @param parent - the parent view group
     * @return the view for the leaderboard search fragment player list
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup
            parent) {
        View view;
        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_leaderboard_search_items,
                    parent, false);
        } else {
            view = convertView;
        }

        // add items to listview
        Player player = (Player) getItem(position);
        TextView playerName = view.findViewById(R.id.player_name_text);
        TextView score = view.findViewById((R.id.score_Player));
        ImageView image1 = view.findViewById(R.id.leaderboard_image_list);

        // set the text and image for each item
        if (player != null) {
            playerName.setText(player.getUsername());
            score.setText(Integer.toString(player.getTotalScore()));
            String url_user = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + player.getUsername(); // sets the unique profile image for each user
            Picasso.get().load(url_user).into(image1); // loads the image into the imageview to make it circle

        }
        // if the player is null, hide the cardview
        else{
            androidx.cardview.widget.CardView cardView = view.findViewById(R.id.profileCardView);
            cardView.setVisibility(View.GONE);
        }

        return view; // return the view for the leaderboard search fragment player list
    }
}
