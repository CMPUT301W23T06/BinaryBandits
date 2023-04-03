package com.example.binarybandits;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.player.PlayerListCallback;
import com.example.binarybandits.qrcode.QRArrayAdapter;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.qrcode.QRCodeListCallback;
import com.example.binarybandits.ui.leaderboard.LeaderboardFragment;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

/**
 * switch activity to otherProfileActivity
 * respond to back button press and send back to MainActivity
 * set fields of player profile of user clicked on
 */
public class otherProfileActivity extends Activity {
    private ArrayList<Player> players;
    private PlayerController controller;

    /**
     * Create the activity for another player's profile
     * @param savedInstanceState the saved instance state that is restored after the app crashes
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayerDB db = new PlayerDB(new DBConnector());
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
        setContentView(R.layout.fragment_profile);
        TextView name = findViewById(R.id.text_username);
        TextView score = findViewById(R.id.score_text);
        TextView num_scanned = findViewById(R.id.total_qr_scanned_text);
        TextView lowest = findViewById(R.id.lowest_score_text);
        TextView highest = findViewById(R.id.highest_score_text);
        ImageView otherImage = findViewById(R.id.profileIconImageView);
        ImageButton back = findViewById(R.id.buttonBack);
        back.setVisibility(View.VISIBLE);
        Bundle extras = getIntent().getExtras();
        String player_name = extras.getString("name");
        String typeOfList = extras.getString("list");

        //Query used to order players depends on whether the current leaderboard is the total score leaderboard
        //or the highest scoring QR code leaderboard
        boolean scoreLeaderboard = extras.getBoolean("scoreLeaderboard");
        Query query;
        if(scoreLeaderboard) {
            query = db.getSortedPlayers();
        }
        else {
            query = db.getHighestQRCodes();
        }
        db.getPlayersByQuery(query, new PlayerListCallback() {
            @Override
            public void onPlayerListCallback(ArrayList<Player> playerList) {
                players = playerList;
                Player otherPlayer = players.get(0);
                // get player from array
                for(int i =0; i<players.size(); i++){
                    if(Objects.equals(players.get(i).getUsername(), player_name)){
                        if(Objects.equals(typeOfList, "long")) {
                            otherPlayer = players.get(i+3);
                        }
                        else{
                            otherPlayer = players.get(i);
                        }
                    }
                }
                controller = new PlayerController(otherPlayer);
                name.setText(otherPlayer.getUsername());
                score.setText(Integer.toString(otherPlayer.getTotalScore()));
                num_scanned.setText(Integer.toString(otherPlayer.getTotalQRCodes()));
                String url_user = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + otherPlayer.getUsername();
                Picasso.get().load(url_user).into(otherImage);
                controller.getHighestQRCode(new ScoreCallback() {
                    @Override
                    public void scoreCallback(int score) {
                        highest.setText(Integer.toString(score));
                    }
                });
                controller.getLowestQRCode(new ScoreCallback() {
                    @Override
                    public void scoreCallback(int score) {
                        lowest.setText(Integer.toString(score));
                    }
                });
                /*if(highestQR != null)
                    highest.setText(Integer.toString(highestQR.getPoints()));
                else {
                    highest.setText("0");
                }
                if(lowestQR != null)
                    lowest.setText(Integer.toString(lowestQR.getPoints()));
                else{
                    lowest.setText("0");
                }*/
                ArrayList<String> qrCodeNames = otherPlayer.getQrCodesScanned();
                //ArrayList<QRCode> dataList = new ArrayList<>();

                final Player finalOtherPlayer = otherPlayer;
                qrCodeDB.getQRCodesFromList(qrCodeNames, new QRCodeListCallback() {
                    @Override
                    public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                        ListView QRlist = findViewById(R.id.list_view_player_qr_codes);
                        ArrayAdapter<QRCode> QRAdapter = new QRArrayAdapter(otherProfileActivity.this, qrCodeList);
                        QRlist.setAdapter(QRAdapter);
                        ArrayList<QRCode> finalDataList = qrCodeList;
                        QRAdapter.notifyDataSetChanged();

                        /**
                         * When a QR code object is clicked on from the list of the users QR codes,
                         * user is taken to QR page to see more information on the given QR
                         * (go to QRCodeInfoActivity)
                         */
                        QRlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                // create intent with name of the QR code, username of the user, and a
                                // true boolean value that confirms we are on the current users profile
                                Intent myIntent = new Intent(otherProfileActivity.this, QRCodeInfoActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("name", String.valueOf(finalDataList.get(position).getName()));
                                extras.putString("username", String.valueOf(finalOtherPlayer.getUsername()));
                                extras.putBoolean("current_user", false);
                                myIntent.putExtras(extras);
                                // go to QRCodeInfoActivity to display the QR code
                                startActivity(myIntent);
                                QRAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

                /*dataList = otherPlayer.getQrCodesScanned();
                ArrayAdapter<QRCode> QRAdapter = new QRArrayAdapter(otherProfileActivity.this, dataList);
                QRlist.setAdapter(QRAdapter);

                ArrayList<QRCode> finalDataList = dataList;
                Player finalOtherPlayer = otherPlayer;
                QRlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Intent myIntent = new Intent(otherProfileActivity.this, QRCodeInfoActivity.class);

                        Bundle extras = new Bundle();
                        extras.putString("name", String.valueOf(finalDataList.get(position).getName()));
                        extras.putString("username", String.valueOf(finalOtherPlayer.getUsername()));
                        extras.putBoolean("current_user", false);
                        myIntent.putExtras(extras);
                        otherProfileActivity.this.startActivity(myIntent);
                    }
                });*/
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherProfileActivity.this.finish();
            }
        });

    }
}

