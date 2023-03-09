package com.example.binarybandits.ui.leaderboard;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.binarybandits.R;
import com.example.binarybandits.models.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class LeaderboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LeaderboardViewModel leaderboardViewModel = new LeaderboardViewModel();
        leaderboardViewModel.sortPlayer_list();
        ArrayList<Player> players = leaderboardViewModel.getPlayerList();
        View leaderboard = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        ListView playerList = leaderboard.findViewById(R.id.playerList);
        TextView nameOne = leaderboard.findViewById(R.id.player1);
        TextView nameTwo = leaderboard.findViewById(R.id.player2);
        TextView nameThree = leaderboard.findViewById(R.id.player3);
        TextView scoreOne = leaderboard.findViewById(R.id.player1_score);
        TextView scoreTwo = leaderboard.findViewById(R.id.player2_score);
        TextView scoreThree = leaderboard.findViewById(R.id.player3_score);
        nameOne.setText(players.get(0).getUsername());
        nameTwo.setText(players.get(1).getUsername());
        nameThree.setText(players.get(2).getUsername());
        scoreOne.setText(Integer.toString(players.get(0).getTotalScore()));
        scoreTwo.setText(Integer.toString(players.get(1).getTotalScore()));
        scoreThree.setText(Integer.toString(players.get(2).getTotalScore()));
        players.remove(0);
        players.remove(0);
        players.remove(0);
        ArrayAdapter<Player> playerArrayAdapter = new LeaderboardArrayAdapter(getActivity(), players);
        playerList.setAdapter(playerArrayAdapter);
        return leaderboard;
    }


}
