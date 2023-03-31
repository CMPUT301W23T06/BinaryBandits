package com.example.binarybandits.ui.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.otherProfileActivity;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.DBConnector;
import com.example.binarybandits.player.PlayerListCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the search fragment of the leaderboard page
 */
public class LeaderboardSearchFragment extends Fragment {

    /**
     * Required empty public constructor for leaderboard search fragment
     */
    public LeaderboardSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Creates the view for the leaderboard search fragment
     * @param inflater - the layout inflater
     * @param container - the view group container
     * @param savedInstanceState -  the saved instance state
     * @return the view for the leaderboard search fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View search = inflater.inflate(R.layout.fragment_leaderboard_search, container, false);

        // Connect to database and get set of players
        DBConnector dbConnector = new DBConnector();
        PlayerDB playerDB = new PlayerDB(dbConnector);
        Set<Player> players = new HashSet<>(); // change ArrayList to Set to avoid duplicate results

        EditText searchInput = search.findViewById(R.id.search_bar);
        ListView searchResults = search.findViewById(R.id.search_results);


        // Test listening to text changes, and updating the listview when the text changes
        searchInput.addTextChangedListener(new TextWatcher() {
            /**
             * This method is called before the text is changed
             * @param s - the character sequence
             * @param start - the start index
             * @param count - the number of characters to be replaced
             * @param after - the number of characters to replace
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed, but needed for TextWatcher to function
            }

            /**
             * This method is called when the text is changed, and searches for the player on every change
             * @param s - the character sequence
             * @param start - the start index
             * @param before - the number of characters to be replaced
             * @param count - the number of characters to replace
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                players.clear(); // clear Set before adding new players
                String searchText = s.toString();

                // If the search text is not empty, search for players containing the input
                if(!searchText.isEmpty()) {
                    playerDB.getPlayersByQuery(playerDB.searchPlayer(searchText), new PlayerListCallback() {
                        @Override
                        public void onPlayerListCallback(ArrayList<Player> playerList) {
                            if(playerList != null) {
                                ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), playerList); // convert Set to ArrayList
                                searchResults.setAdapter(playerArrayAdapter); // set the adapter for the listview to display the players

                                // When the user clicks on a player, go to their profile
                                searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    /**
                                     * This method is called when the user clicks on a player item in the listview, and goes to the player's profile
                                     * @param adapterView - the adapter view
                                     * @param view - the view
                                     * @param i - the position
                                     * @param l - the id
                                     */
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(LeaderboardSearchFragment.this.getActivity(), otherProfileActivity.class); // go to other player's profile
                                        Bundle extras = new Bundle(); // pass the player's username to the other profile activity
                                        extras.putString("name", playerList.get(i).getUsername());
                                        extras.putString("list", "search");
                                        intent.putExtras(extras);
                                        startActivity(intent); // start the activity

                                    }
                                });
                            }
                        }
                    });
                }
                // If the search text is empty, clear the listview
                else {
                    ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), new ArrayList<>()); // convert Set to ArrayList
                    searchResults.setAdapter(playerArrayAdapter);
                }

                //END OF NEW LEADERBOARD SEARCH (REMOVE THIS COMMENT LATER)
            }

            /**
             * This method is called after the text is changed
             * @param s - the editable
             */
            @Override
            public void afterTextChanged(Editable s) {
                // not needed, but needed for TextWatcher to function
            }
        });

        // When the user presses the search button on the keyboard, search for the player
        Button backToLeaderboard = search.findViewById(R.id.back_to_leaderboard);
        backToLeaderboard.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is called when the user clicks the back to leaderboard button
             * @param v - the view
             */
            @Override
            public void onClick(View v) {
                backToLeaderboard(v);
            }
        });

        return search; // return the view for the leaderboard search fragment

    }

    /**
     * This method is called when the user clicks the back to leaderboard button, and goes back to the leaderboard
     * @param view - the view
     */
    public void backToLeaderboard(View view) {
        // on click back to leaderboard
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LeaderboardFragment()).commit();
    }



}
