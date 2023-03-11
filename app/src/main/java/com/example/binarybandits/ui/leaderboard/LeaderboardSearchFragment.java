package com.example.binarybandits.ui.leaderboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardSearchFragment extends Fragment {

    private EditText searchInput;
    private ListView searchResults;
    private FirebaseFirestore db;

    public LeaderboardSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard_search, container, false);

        searchInput = rootView.findViewById(R.id.search_bar);
        searchResults = rootView.findViewById(R.id.search_results);
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

        return rootView;
    }

    private void search(String query) {
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
    }

    private void displayResults(List<DocumentSnapshot> results) {
        List<String> usernames = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (DocumentSnapshot document : results) {
            names.add(document.getString("username"));
            names.add(document.getString("name"));
            //names.add(document.getString("points"));

        }
        // display the names in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, usernames);
        searchResults.setAdapter(adapter);
    }


}