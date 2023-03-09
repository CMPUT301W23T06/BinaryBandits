package com.example.binarybandits.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.binarybandits.models.Player;

import java.util.ArrayList;

public class LeaderboardViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private Player key;
    private Player temp;
    private ArrayList<Player> sortedPlayerList;

    // create random players to test
    private Player player1 = new Player("John", null, 500,0, null, null);
    private Player player2 = new Player("Karen", null, 600,0,null,null);
    private Player player3 = new Player("Jake", null, 800, 0, null, null);
    private Player player4 = new Player("Chad", null, 100,0,  null, null);


    public LeaderboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
        sortedPlayerList = new ArrayList<Player>();
        sortedPlayerList.add(player1);
        sortedPlayerList.add(player2);
        sortedPlayerList.add(player3);
        sortedPlayerList.add(player4);
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
            key = sortedPlayerList.get(i);
            k = i-1;
            while (k>=0 && sortedPlayerList.get(k).getTotalScore() < key.getTotalScore()){
                temp = sortedPlayerList.get(k);
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
