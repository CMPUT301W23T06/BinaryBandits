package com.example.binarybandits.ui.leaderboard;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    private ArrayList<Player> players;
    private LeaderboardViewModel leaderboardViewModel;
    private View leaderboard;
    private ArrayAdapter<Player> playerArrayAdapter;
    private ListView playerList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        leaderboardViewModel = new LeaderboardViewModel();
        leaderboardViewModel.sortPlayer_list();
        players = leaderboardViewModel.getPlayerList();
        leaderboard = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        playerList = leaderboard.findViewById(R.id.playerList);
        playerArrayAdapter = new LeaderboardArrayAdapter(getActivity(), players);
        playerList.setAdapter(playerArrayAdapter);
        return leaderboard;
    }


}
