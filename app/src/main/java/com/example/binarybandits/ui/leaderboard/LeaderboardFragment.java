package com.example.binarybandits.ui.leaderboard;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.LeaderboardController;
import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.otherProfileActivity;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.player.PlayerListCallback;
import com.example.binarybandits.qrcode.QRCodeCallback;
import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeListCallback;
import com.example.binarybandits.ui.profile.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * updates view of leaderboard fragment
 * responds to click of search button
 * responds to click of user profile
 */
public class LeaderboardFragment extends Fragment {
    LeaderboardViewModel leaderboardViewModel = new LeaderboardViewModel();
    ArrayList<Player> players = leaderboardViewModel.getPlayerList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View leaderboard = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        PlayerDB db = new PlayerDB(new DBConnector());
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());

        /*
        qrCodeDB.getAllQRCodes(new QRCodeListCallback() {
            @Override
            public void onQRCodeListCallback(ArrayList<QRCode> qrCodes) {
                for(int i = 0; i < qrCodes.size(); i++) {
                    qrCodes.get(i).setPlayersScannedBy(new ArrayList<>());
                    qrCodeDB.updateQRCode(qrCodes.get(i));
                }
            }
        });
        */

        //Get list of players sorted by score
        db.getPlayersByQuery(db.getSortedPlayers(), new PlayerListCallback() {
            @Override
            public void onPlayerListCallback(ArrayList<Player> playerResultsList) {
                Log.d("Leaderboard", playerResultsList.toString());
                //Remove Later!!!
                /*
                Log.d("Size", String.valueOf(playerResultsList.size()));
                for(int i = 0; i < playerResultsList.size(); i++) {
                    ArrayList<QRCode> qrCodes = playerResultsList.get(i).getQrCodesScanned();
                    ArrayList<String> names = new ArrayList<>();
                    for(int j = 0; j < qrCodes.size(); j++) {
                        names.add(qrCodes.get(j).getName());
                    }
                    Log.d("OuterLoop", names.toString());
                    Log.d("Username", playerResultsList.get(i).getUsername());
                    if(qrCodes != null) {
                        for(int j = 0; j < qrCodes.size(); j++) {
                            int finalJ = j;
                            db.findPlayersWithQRCode(qrCodes.get(j), new PlayerListCallback() {
                                @Override
                                public void onPlayerListCallback(ArrayList<Player> playerList) {
                                    for(int k = 0; k < playerList.size(); k++) {
                                        qrCodes.get(finalJ).addPlayerScannedBy(playerList.get(k).getUsername());
                                    }
                                    qrCodeDB.updateQRCode(qrCodes.get(finalJ));
                                }
                            });
                        }
                    }
                }
                */
                displayLeaderboard(leaderboard, playerResultsList, true);
            }
        });

        // set onclick listener for search button to open search fragment
        Button button = leaderboard.findViewById(R.id.button);
        ListView listview = leaderboard.findViewById(R.id.playerList);

        //Referenced: https://stackoverflow.com/questions/23358822/how-to-custom-switch-button
        RadioButton scoreButton = (RadioButton)leaderboard.findViewById(R.id.score_button);
        RadioButton qrCodeButton = (RadioButton)leaderboard.findViewById(R.id.qrCode_button);

        /**
         * This method is called when the user clicks the search button
         * @param v - the view that was clicked
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.fragment_container, new LeaderboardSearchFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView name = leaderboard.findViewById(R.id.current_username);
                    String nameOfPlayer = (String) name.getText();
                    // If user clicks on themselves in leaderboard send them to their profile page
                    if(Objects.equals(players.get(i+3).getUsername(), nameOfPlayer)){
                        getFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                    }
                    // else send them to other players profile
                    else{
                        Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("name", String.valueOf(players.get(i).getUsername()));
                        extras.putString("list", "long");
                        extras.putBoolean("scoreLeaderboard", scoreButton.isChecked());
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                }
            });
            ImageView image1 = leaderboard.findViewById(R.id.limage1);
        image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView name = leaderboard.findViewById(R.id.current_username);
                    String nameOfPlayer = (String) name.getText();
                    // If user clicks on themselves in leaderboard send them to their profile page
                    if(Objects.equals(players.get(0).getUsername(), nameOfPlayer)){
                        getFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                    }
                    // else send them to other players profile
                    else {
                        Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("name", String.valueOf(players.get(0).getUsername()));
                        extras.putString("list", "short");
                        extras.putBoolean("scoreLeaderboard", scoreButton.isChecked());
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                }
            });

            ImageView image2 = leaderboard.findViewById(R.id.limage2);
        image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView name = leaderboard.findViewById(R.id.current_username);
                    String nameOfPlayer = (String) name.getText();
                    // If user clicks on themselves in leaderboard send them to their profile page
                    if(Objects.equals(players.get(1).getUsername(), nameOfPlayer)){
                        getFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                    }
                    // else send them to other players profile
                    else {
                        Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("name", String.valueOf(players.get(1).getUsername()));
                        extras.putString("list", "short");
                        extras.putBoolean("scoreLeaderboard", scoreButton.isChecked());
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                }
            });
            ImageView image3 = leaderboard.findViewById(R.id.limage3);
        image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView name = leaderboard.findViewById(R.id.current_username);
                    String nameOfPlayer = (String) name.getText();
                    // If user clicks on themselves in leaderboard send them to their profile page
                    if(Objects.equals(players.get(2).getUsername(), nameOfPlayer)){
                        getFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                    }
                    // else send them to other players profile
                    else {
                        Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("name", String.valueOf(players.get(2).getUsername()));
                        extras.putString("list", "short");
                        extras.putBoolean("scoreLeaderboard", scoreButton.isChecked());
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                }
            });
        MaterialToolbar toolbar = leaderboard.findViewById(R.id.materialToolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
            }
        });

        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.getPlayersByQuery(db.getSortedPlayers(), new PlayerListCallback() {
                    @Override
                    public void onPlayerListCallback(ArrayList<Player> playerList) {
                        displayLeaderboard(leaderboard, playerList, true);
                        scoreButton.setTextColor(Color.parseColor("#FFFFFF"));
                        scoreButton.setBackgroundResource(R.drawable.red_button);
                        qrCodeButton.setBackground(null);
                        qrCodeButton.setTextColor(Color.parseColor("#FD426F"));
                    }
                });
            }
        });


        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Leaderboard", "Switch to QR Code leaderboard");
                db.getPlayersByQuery(db.getHighestQRCodes(), new PlayerListCallback() {
                    @Override
                    public void onPlayerListCallback(ArrayList<Player> playerList) {
                        displayLeaderboard(leaderboard, playerList, false);
                        qrCodeButton.setTextColor(Color.parseColor("#FFFFFF"));
                        qrCodeButton.setBackgroundResource(R.drawable.red_button);
                        scoreButton.setBackground(null);
                        scoreButton.setTextColor(Color.parseColor("#FD426F"));
                    }
                });
            }
        });

        return leaderboard;
    }


    /**
     * Display the current leaderboard
     * @param leaderboard The leaderboard view
     * @param playerResultsList The sorted results of Players (by either score or highest QR code)
     */
    public void displayLeaderboard(View leaderboard, ArrayList<Player> playerResultsList, boolean scoreLeaderboard) {
        // instantiate variables for top three players
        ListView playerList = leaderboard.findViewById(R.id.playerList);
        TextView nameOne = leaderboard.findViewById(R.id.player1);
        TextView nameTwo = leaderboard.findViewById(R.id.player2);
        TextView nameThree = leaderboard.findViewById(R.id.player3);
        TextView scoreOne = leaderboard.findViewById(R.id.player1_score);
        TextView scoreTwo = leaderboard.findViewById(R.id.player2_score);
        TextView scoreThree = leaderboard.findViewById(R.id.player3_score);
        ImageView user_image = leaderboard.findViewById(R.id.users_image);
        TextView user_name = leaderboard.findViewById(R.id.current_username);
        TextView user_score = leaderboard.findViewById(R.id.current_user_score);
        TextView users_rank = leaderboard.findViewById(R.id.user_rank);
        TextView percentileLabel = leaderboard.findViewById(R.id.percentile_label);
        String user = AuthController.getUsername(getActivity());

        LeaderboardController leaderboardController = new LeaderboardController();
        players = playerResultsList;

        ArrayList<Integer> scores = new ArrayList<>();
        // get current player profile
        int user_rank = 0;
        Player current_user = players.get(0);
        for(int i=0; i<players.size(); i++) {
            if (Objects.equals(players.get(i).getUsername(), user)) {
                current_user = players.get(i);
                user_rank = i + 1;
            }
            scores.add(players.get(i).getHighestScore());
        }
        // set users info at bottom of leaderboard
        user_name.setText(current_user.getUsername());

        CardView card4 = leaderboard.findViewById(R.id.card4);
        if(scoreLeaderboard) {
            user_score.setText(Integer.toString(current_user.getTotalScore()));
            users_rank.setText("#"+Integer.toString(user_rank));
            String url_user = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + current_user.getUsername();
            Picasso.get().load(url_user).into(user_image);
            user_name.setVisibility(View.VISIBLE);
            user_score.setVisibility(View.VISIBLE);
            users_rank.setVisibility(View.VISIBLE);
            user_image.setVisibility(View.VISIBLE);
            card4.setVisibility(View.VISIBLE);
            percentileLabel.setVisibility(View.INVISIBLE);
        }
        else {
            Log.d("LeaderboardControllerCalc", scores.toString());
            Log.d("LeaderboardControllerCalc", String.valueOf(current_user.getHighestScore()));
            user_name.setVisibility(View.INVISIBLE);
            user_score.setVisibility(View.INVISIBLE);
            users_rank.setVisibility(View.INVISIBLE);
            user_image.setVisibility(View.INVISIBLE);
            card4.setVisibility(View.INVISIBLE);
            percentileLabel.setVisibility(View.VISIBLE);

            int percentile = leaderboardController.getPercentile(scores, current_user.getHighestScore());
            String newPercentileLabel = String.format(Locale.CANADA, "You are in the top %d%% of players", percentile);
            percentileLabel.setText(newPercentileLabel);
        }

        // set values of top three players
        if(players.size()>0) {
            nameOne.setText(players.get(0).getUsername());
            if(scoreLeaderboard) {
                scoreOne.setText(Integer.toString(players.get(0).getTotalScore()));
            }
            else {
                scoreOne.setText(Integer.toString(players.get(0).getHighestScore()));
            }
            ImageView image1 = leaderboard.findViewById(R.id.limage1);
            String url1 = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + players.get(0).getUsername();
            Picasso.get().load(url1).into(image1);
        }

        if(players.size()>1) {
            nameTwo.setText(players.get(1).getUsername());
            if(scoreLeaderboard) {
                scoreTwo.setText(Integer.toString(players.get(1).getTotalScore()));
            }
            else {
                scoreTwo.setText(Integer.toString(players.get(1).getHighestScore()));
            }
            ImageView image2 = leaderboard.findViewById(R.id.limage2);
            String url2 = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + players.get(1).getUsername();
            Picasso.get().load(url2).into(image2);
        }
        if(players.size()>2) {
            nameThree.setText(players.get(2).getUsername());
            if(scoreLeaderboard) {
                scoreThree.setText(Integer.toString(players.get(2).getTotalScore()));
            }
            else {
                scoreThree.setText(Integer.toString(players.get(2).getHighestScore()));
            }
            ImageView image3 = leaderboard.findViewById(R.id.limage3);
            String url3 = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + players.get(2).getUsername();
            Picasso.get().load(url3).into(image3);
        }

        // call ArrayAdapter to add each item in array to ListView
        if(players.size()>3) {
            // create copy of array without top 3 players
            ArrayList<Player> players_copy = new ArrayList<>();
            for(int i =3; i<players.size(); i++){
                players_copy.add(players.get(i));
            }
            ArrayAdapter<Player> playerArrayAdapter = new LeaderboardArrayAdapter(getActivity(), players_copy, scoreLeaderboard);
            playerList.setAdapter(playerArrayAdapter);
        }
        // set onclick listener for search button to open search fragment
        Button button = leaderboard.findViewById(R.id.button);
        ListView listview = leaderboard.findViewById(R.id.playerList);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                toggleUserIconVisibility(leaderboard);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.fragment_container, new LeaderboardSearchFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });
    }

    /**
     * Change the visibility of the top 3 icons on leaderboard and user ranking icon when the search button is clicked
     * @param leaderboard leaderboard view object
     */
    public void toggleUserIconVisibility(View leaderboard) {
        CardView card1 = leaderboard.findViewById(R.id.card1);
        CardView card2 = leaderboard.findViewById(R.id.card2);
        CardView card3 = leaderboard.findViewById(R.id.card3);
        CardView card4 = leaderboard.findViewById(R.id.card4);

        if (card1.getVisibility() == View.VISIBLE) {
            card1.setVisibility(View.INVISIBLE);
            card2.setVisibility(View.INVISIBLE);
            card3.setVisibility(View.INVISIBLE);
            card4.setVisibility(View.INVISIBLE);
        } else {
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
            card4.setVisibility(View.VISIBLE);
        }
    }


}
