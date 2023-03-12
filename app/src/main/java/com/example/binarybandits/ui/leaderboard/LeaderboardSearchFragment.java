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
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.DBConnector;
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

public class LeaderboardSearchFragment extends Fragment {

    public LeaderboardSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View search = inflater.inflate(R.layout.fragment_leaderboard_search, container, false);


        DBConnector dbConnector = new DBConnector();
        PlayerDB playerDB = new PlayerDB(dbConnector);
        Set<Player> players = new HashSet<>(); // change ArrayList to Set to avoid duplicate results

        EditText searchInput = search.findViewById(R.id.search_bar);
        ListView searchResults = search.findViewById(R.id.search_results);


        // Test listening to text changes, and updating the listview when the text changes
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                players.clear(); // clear Set before adding new players
                String searchText = s.toString();

                // If the search text is not empty, search for the player
                if (!searchText.isEmpty()) {
                    playerDB.getPlayer(searchText, new PlayerCallback() {
                        // When the player is found, add it to the listview
                        @Override
                        public void onPlayerCallback(Player searchedPlayer) {
                            players.add(searchedPlayer); // use Set.add() instead of ArrayList.add()
                            ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), new ArrayList<>(players)); // convert Set to ArrayList
                            searchResults.setAdapter(playerArrayAdapter);

                        }
                    });
                }
                // If the search text is empty, clear the listview
                else {
                    players.clear();
                    //ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), new ArrayList<>(players)); // convert Set to ArrayList
                    //searchResults.setAdapter(playerArrayAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });

        // When the user presses the search button on the keyboard, search for the player
        Button backToLeaderboard = search.findViewById(R.id.back_to_leaderboard);
        backToLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToLeaderboard(v);
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LeaderboardSearchFragment.this.getActivity(), otherProfileActivity.class);
                startActivity(intent);

            }
        });

        return search;

    }

    // This method is called when the back to leaderboard button is clicked
    public void backToLeaderboard(View view) {
        //on click back to leaderboard
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LeaderboardFragment()).commit();

    }



}
