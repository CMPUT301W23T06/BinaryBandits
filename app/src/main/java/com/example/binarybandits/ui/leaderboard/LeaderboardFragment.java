package com.example.binarybandits.ui.leaderboard;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.otherProfileActivity;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.player.PlayerListCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
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
        String user = AuthController.getUsername(getActivity());

        PlayerDB db = new PlayerDB(new DBConnector());

        db.getPlayersByQuery(db.getSortedPlayers(), new PlayerListCallback() {
            @Override
            public void onPlayerListCallback(ArrayList<Player> playerResultsList) {
                Log.d("Leaderboard", playerResultsList.toString());
                // sort players list
                //players = leaderboardViewModel.sortPlayer_list(players);
                players = playerResultsList;

                // get current player profile
                int user_rank = 0;
                Player current_user = players.get(0);
                for(int i=0; i<players.size(); i++)
                    if(Objects.equals(players.get(i).getUsername(), user)){
                        current_user = players.get(i);
                        user_rank = i+1;
                    }

                // set users info at bottom of leaderboard
                user_name.setText(current_user.getUsername());
                user_score.setText(Integer.toString(current_user.getTotalScore()));
                users_rank.setText("#"+Integer.toString(user_rank));
                String url_user = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + current_user.getUsername();
                Picasso.get().load(url_user).into(user_image);

                // set values of top three players
                if(players.size()>0) {
                    nameOne.setText(players.get(0).getUsername());
                    scoreOne.setText(Integer.toString(players.get(0).getTotalScore()));
                    ImageView image1 = leaderboard.findViewById(R.id.limage1);
                    String url1 = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + players.get(0).getUsername();
                    Picasso.get().load(url1).into(image1);
                }

                if(players.size()>1) {
                    nameTwo.setText(players.get(1).getUsername());
                    scoreTwo.setText(Integer.toString(players.get(1).getTotalScore()));
                    ImageView image2 = leaderboard.findViewById(R.id.limage2);
                    String url2 = "https://api.dicebear.com/5.x/avataaars-neutral/png?seed=" + players.get(1).getUsername();
                    Picasso.get().load(url2).into(image2);
                }
                if(players.size()>2) {
                    nameThree.setText(players.get(2).getUsername());
                    scoreThree.setText(Integer.toString(players.get(2).getTotalScore()));
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
                    ArrayAdapter<Player> playerArrayAdapter = new LeaderboardArrayAdapter(getActivity(), players_copy);
                    playerList.setAdapter(playerArrayAdapter);
                }
                // set onclick listener for search button to open search fragment
                Button button = leaderboard.findViewById(R.id.button);
                ListView listview = leaderboard.findViewById(R.id.playerList);

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
            }
        });

        // set onclick listener for search button to open search fragment
        Button button = leaderboard.findViewById(R.id.button);
        ListView listview = leaderboard.findViewById(R.id.playerList);

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
                    Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", String.valueOf(players.get(i).getUsername()));
                    extras.putString("list", "long");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            ImageView image1 = leaderboard.findViewById(R.id.limage1);
        image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", String.valueOf(players.get(0).getUsername()));
                    extras.putString("list", "short");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

            ImageView image2 = leaderboard.findViewById(R.id.limage2);
        image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", String.valueOf(players.get(1).getUsername()));
                    extras.putString("list", "short");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            ImageView image3 = leaderboard.findViewById(R.id.limage3);
        image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeaderboardFragment.this.getActivity(), otherProfileActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("name", String.valueOf(players.get(2).getUsername()));
                    extras.putString("list", "short");
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

        return leaderboard;
    }


}
