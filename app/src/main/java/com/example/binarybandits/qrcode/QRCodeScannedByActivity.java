package com.example.binarybandits.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.otherProfileActivity;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.player.PlayerListCallback;
import com.example.binarybandits.ui.leaderboard.LeaderboardFragment;
import com.example.binarybandits.ui.leaderboard.LeaderboardSearchArrayAdapter;
import com.example.binarybandits.ui.leaderboard.LeaderboardSearchFragment;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * Displays other players that have scanned a QR code. The QR code is provided by
 * QRInfoActivity where the player clicks "See other players" to navigate to this
 * activity.
 */
public class QRCodeScannedByActivity extends AppCompatActivity {

    /**
     * Set the content view to the scanned by page and get the QR code from the database.
     * Allow users to go back to QRInfoActivity if the click the back button
     * @param savedInstanceState saved instance state of the activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannedby);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();
        Button backButton = findViewById(R.id.back_to_qrcode);

        if(extras != null) {
            //Retrieve QR code name from QRInfoActivity
            String qrCodeName = extras.getString("name");
            Log.d("QRCodeScanned", qrCodeName);
            getQRCode(qrCodeName);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeScannedByActivity.this.finish();
            }
        });
    }

    /**
     * Given a name of a QRCode, get the QRCode with the given name from the database
     * @param qrCodeName name of the QRCode to find in database
     */
    public void getQRCode(String qrCodeName) {
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
        qrCodeDB.getQRCode(qrCodeName, new QRCodeCallback() {
            @Override
            public void onQRCodeCallback(QRCode qrCode) {
                displayPlayersScannedBy(qrCode);
            }
        });
    }

    /**
     * Given a QRCode, display all players that have scanned the QRCode (except the current user)
     * @param qrCode QR code to find players that have scanned
     */
    public void displayPlayersScannedBy(QRCode qrCode) {
        ArrayList<String> playersScannedBy = qrCode.getPlayersScannedBy();
        PlayerDB playerDB = new PlayerDB(new DBConnector());

        ListView results = findViewById(R.id.scanned_by_results);

        if (playersScannedBy != null) {
            //Get the current user's username
            String currentUser = AuthController.getUsername(QRCodeScannedByActivity.this);
            //Remove the current player from the list of players to display
            for (int i = 0; i < playersScannedBy.size(); i++) {
                if (playersScannedBy.get(i).equals(currentUser)) {
                    playersScannedBy.remove(playersScannedBy.get(i));
                }
            }
        }
        if(playersScannedBy != null && !playersScannedBy.isEmpty()) {
            playerDB.getPlayersByQuery(playerDB.searchListOfPlayers(playersScannedBy), new PlayerListCallback() {
                @Override
                public void onPlayerListCallback(ArrayList<Player> playerList) {
                    LeaderboardSearchArrayAdapter arrayAdapter = new LeaderboardSearchArrayAdapter(QRCodeScannedByActivity.this, playerList);
                    results.setAdapter(arrayAdapter);
                    results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(QRCodeScannedByActivity.this, otherProfileActivity.class); // go to other player's profile
                            Bundle extras = new Bundle(); // pass the player's username to the other profile activity
                            extras.putString("name", playerList.get(i).getUsername());
                            extras.putString("list", "search");
                            intent.putExtras(extras);
                            startActivity(intent); // start the activity
                        }
                    });
                }
            });
        }

        else {
            //Referenced: https://stackoverflow.com/questions/10017088/android-displaying-text-when-listview-is-empty
            //Only show back button if there are no results
            TextView header = findViewById(R.id.scanned_by_header);
            header.setVisibility(TextView.INVISIBLE);
            Toast message = Toast.makeText(QRCodeScannedByActivity.this, "No other players found!", Toast.LENGTH_LONG);
            message.show();
        }
    }
}
