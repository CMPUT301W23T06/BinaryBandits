package com.example.binarybandits;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.example.binarybandits.models.Player;
import com.example.binarybandits.ui.leaderboard.LeaderboardFragment;


public class otherProfileActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        TextView name = findViewById(R.id.text_username);
        TextView score = findViewById(R.id.score_text);
        TextView num_scanned = findViewById(R.id.total_qr_scanned_text);
        TextView lowest = findViewById(R.id.lowest_score_text);
        TextView highest = findViewById(R.id.highest_score_text);
        LeaderboardFragment leaderboard = new LeaderboardFragment();

        //Player player = leaderboard.getOtherPlayer();
        name.setText("Unfinished");
        lowest.setText("Will to live");
        highest.setText("Suffering");

    }
}

