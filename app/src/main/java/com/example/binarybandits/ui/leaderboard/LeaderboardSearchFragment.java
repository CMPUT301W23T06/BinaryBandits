package com.example.binarybandits.ui.leaderboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardSearchFragment extends Fragment {

    //private EditText searchInput;
    //private ListView searchResults;

    public LeaderboardSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View search = inflater.inflate(R.layout.fragment_leaderboard_search, container, false);

        /*DBConnector dbConnector = new DBConnector();
        PlayerDB playerDB = new PlayerDB(dbConnector);
        ArrayList<Player> players = new ArrayList<>();
        //ArrayList<String> usernames = new ArrayList<>();
        //ArrayList<Integer> scores = new ArrayList<>();

        searchInput = search.findViewById(R.id.search_bar);
        String searchText = searchInput.getText().toString();

        if ((searchText).length() != 0) {
            playerDB.getPlayer(searchText, new PlayerCallback() {
                @Override
                public void onPlayerCallback(Player searchedPlayer) {
                    players.add(searchedPlayer);

                }
            });

            // display all players in ListView
            searchResults = search.findViewById(R.id.search_results);

            ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), players);
            searchResults.setAdapter(playerArrayAdapter);


        }
        return search;
    }
}*/


        /*DBConnector dbConnector = new DBConnector();
        PlayerDB playerDB = new PlayerDB(dbConnector);
        ArrayList<Player> players = new ArrayList<>();

        EditText searchInput = search.findViewById(R.id.search_bar);
        ListView searchResults = search.findViewById(R.id.search_results);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = searchInput.getText().toString();

                    if (!searchText.isEmpty()) {
                        playerDB.getPlayer(searchText, new PlayerCallback() {
                            @Override
                            public void onPlayerCallback(Player searchedPlayer) {
                                players.add(searchedPlayer);
                                ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), players);
                                searchResults.setAdapter(playerArrayAdapter);
                            }
                        });
                    } else {
                        players.clear();
                        ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), players);
                        searchResults.setAdapter(playerArrayAdapter);
                    }
                    return true;
                }
                return false;
            }
        });

        return search;
    }*/
        DBConnector dbConnector = new DBConnector();
        PlayerDB playerDB = new PlayerDB(dbConnector);
        ArrayList<Player> players = new ArrayList<>();

        EditText searchInput = search.findViewById(R.id.search_bar);
        ListView searchResults = search.findViewById(R.id.search_results);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        String searchText = searchInput.getText().toString();

                        players.clear();

                        if (!searchText.isEmpty()) {
                            playerDB.getPlayer(searchText, new PlayerCallback() {
                                @Override
                                public void onPlayerCallback(Player searchedPlayer) {
                                    players.add(searchedPlayer);
                                    ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), players);
                                    searchResults.setAdapter(playerArrayAdapter);
                                }
                            });
                        } else {
                            players.clear();
                            ArrayAdapter<Player> playerArrayAdapter = new LeaderboardSearchArrayAdapter(getActivity(), players);
                            searchResults.setAdapter(playerArrayAdapter);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        return search;
    }}


        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, usernames);
            //searchResults.setAdapter(adapter);


        //}




        //ArrayAdapter<Player> playerArrayAdapter = new LeaderboardArrayAdapter(getActivity(), players);
        //listView.setAdapter(playerArrayAdapter);


    /*return search;


        searchInput = search.findViewById(R.id.search_bar);
        searchResults = search.findViewById(R.id.search_results);
        db = FirebaseFirestore.getInstance();
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return search;
    }*/

    /*private void search(String query) {
        CollectionReference collectionRef = db.collection("Players");
        Query searchQuery = collectionRef.whereEqualTo("username", query);

        searchQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> results = task.getResult().getDocuments();
                displayResults(results);
            } else {
                // handle errors here
            }
        });
    }*/



        /*private void displayResults(List<DocumentSnapshot> results) {
        List<String> usernames = new ArrayList<>();
        //List<String> names = new ArrayList<>();
        for (DocumentSnapshot document : results) {
            usernames.add(document.getString("username"));
            //names.add(document.getString("name"));
            //names.add(document.getString("points"));

        }
        // display the names in a ListView
        //LeaderboardViewModel leaderboardViewModel = new LeaderboardViewModel();
        //ArrayList<Player> names = leaderboardViewModel.getPlayerList();
        ArrayAdapter<Player> playerArrayAdapter = new LeaderboardArrayAdapter(getActivity(), usernames);

        searchResults.setAdapter(playerArrayAdapter);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, usernames);
        //searchResults.setAdapter(adapter);
    }*/


       // }
