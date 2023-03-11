package com.example.binarybandits.ui.leaderboard;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderboardViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final ArrayList<Player> sortedPlayerList;

    /**
     * connect to db and get all players, create player instance, and add to array of players
     */
    public LeaderboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
        sortedPlayerList = new ArrayList<>();

    }

    /**
     * Reference: https://www.javatpoint.com/insertion-sort-in-java
     * sort array of players based on score using insertion sort
     */
    public ArrayList<Player> sortPlayer_list(ArrayList<Player> sortedPlayerList){
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
        return sortedPlayerList;
    }

    /**
     * returns array of sorted players
     * @return
     */
    public ArrayList<Player >getPlayerList(){
        return sortedPlayerList;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
