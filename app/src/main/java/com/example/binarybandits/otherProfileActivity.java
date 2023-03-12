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
import com.example.binarybandits.qrcode.QRCodeInfoActivity;
import com.example.binarybandits.ui.leaderboard.LeaderboardFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

/**
 * switch activity to otherProfileActivity
 * respond to back button press and send back to MainActivity
 * set fields of player profile of user clicked on
 */
public class otherProfileActivity extends Activity {
    ArrayList<Player> players;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayerDB db = new PlayerDB(new DBConnector());
        setContentView(R.layout.fragment_profile);
        TextView name = findViewById(R.id.text_username);
        TextView score = findViewById(R.id.score_text);
        TextView num_scanned = findViewById(R.id.total_qr_scanned_text);
        TextView lowest = findViewById(R.id.lowest_score_text);
        TextView highest = findViewById(R.id.highest_score_text);
        ImageView otherImage = findViewById(R.id.profileIconImageView);
        ImageButton back = findViewById(R.id.buttonBack);
        back.setVisibility(View.VISIBLE);
        LeaderboardFragment leaderboard = new LeaderboardFragment();
        Bundle extras = getIntent().getExtras();
        String player_name = extras.getString("name");
        String typeOfList = extras.getString("list");

        db.getPlayersByQuery(db.getSortedPlayers(), new PlayerListCallback() {
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
                PlayerController controller = new PlayerController(otherPlayer);
                name.setText(otherPlayer.getUsername());
                score.setText(Integer.toString(otherPlayer.getTotalScore()));
                num_scanned.setText(Integer.toString(otherPlayer.getTotalQRCodes()));
                String url_user = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + otherPlayer.getUsername();
                Picasso.get().load(url_user).into(otherImage);
                QRCode highestQR = controller.getHighestQRCode();
                QRCode lowestQR = controller.getLowestQRCode();
                if(highestQR != null)
                    highest.setText(Integer.toString(highestQR.getPoints()));
                else {
                    highest.setText("0");
                }
                if(lowestQR != null)
                    lowest.setText(Integer.toString(lowestQR.getPoints()));
                else{
                    lowest.setText("0");
                }
                ListView QRlist = findViewById(R.id.list_view_player_qr_codes);
                ArrayList<QRCode> dataList = new ArrayList<>();
                dataList = otherPlayer.getQrCodesScanned();
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
                });
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

