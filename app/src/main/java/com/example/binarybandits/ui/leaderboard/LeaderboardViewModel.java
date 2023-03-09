package com.example.binarybandits.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.binarybandits.models.Player;

import java.util.ArrayList;

public class LeaderboardViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final ArrayList<Player> sortedPlayerList;

    public LeaderboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
        sortedPlayerList = new ArrayList<>();
        // create random players to test
        Player player1 = new Player("John", null, 500, 0, null, null);
        sortedPlayerList.add(player1);
        Player player2 = new Player("Karen", null, 600, 0, null, null);
        sortedPlayerList.add(player2);
        Player player3 = new Player("Jake", null, 800, 0, null, null);
        sortedPlayerList.add(player3);
        Player player4 = new Player("Chad", null, 100, 0, null, null);
        sortedPlayerList.add(player4);
        Player player5 = new Player("Logan", null, 6000, 0, null, null);
        sortedPlayerList.add(player5);
        Player player6 = new Player("Vera", null, 2800, 0, null, null);
        sortedPlayerList.add(player6);
        Player player7 = new Player("Advik", null, 1100, 0, null, null);
        sortedPlayerList.add(player7);
        Player player8 = new Player("Sukhnoor", null, 6400, 0, null, null);
        sortedPlayerList.add(player8);
        Player player9 = new Player("Aryaman", null, 10000, 0, null, null);
        sortedPlayerList.add(player9);
        Player player0 = new Player("logan11111", null, 1100, 0, null, null);
        sortedPlayerList.add(player0);
    }

    /**
     * Reference: https://www.javatpoint.com/insertion-sort-in-java
     * sort array of players based on score using insertion sort
     */
    public void sortPlayer_list(){
        int len = sortedPlayerList.size();
        int i;
        int k;
        for(i = 1; i<len; i++) {
            Player key = sortedPlayerList.get(i);
            k = i-1;
            while (k>=0 && sortedPlayerList.get(k).getTotalScore() < key.getTotalScore()){
                Player temp = sortedPlayerList.get(k);
                sortedPlayerList.set(k+1, temp);
                k = k -1;
            }
            sortedPlayerList.set(k+1, key);
        }

    }

    public ArrayList<Player >getPlayerList(){
        return sortedPlayerList;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
