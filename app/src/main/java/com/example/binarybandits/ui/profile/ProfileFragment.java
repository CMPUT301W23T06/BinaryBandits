package com.example.binarybandits.ui.profile;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.R;
import com.example.binarybandits.ScoreCallback;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.qrcode.QRArrayAdapter;

import com.example.binarybandits.qrcode.QRCodeDB;
import com.example.binarybandits.qrcode.QRCodeInfoActivity;

import com.example.binarybandits.qrcode.DownloadImageTask;
import com.example.binarybandits.qrcode.QRCodeListCallback;


import java.util.ArrayList;
import java.util.Comparator;

/**
 * ProfileFragment displays the current users profile page, including their username, total score,
 * total number of scans, highest and lowest scoring QR points, and a list of their QR codes
 *
 * Outstanding Issue: QR codes are only displayed properly when a player has 10 or less QR codes
 */
public class ProfileFragment extends Fragment {

    private ImageView imageView;
    private PlayerController playerController;
    private ArrayList<QRCode> finalDataList;
    private ArrayAdapter<QRCode> QRAdapter;

    /**
     * On create, display current players profile icon and send to getCurrentPlayer()
     * to get necessary information
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = view.findViewById(R.id.profileIconImageView);
        DownloadImageTask.loadAvatarImageIntoView(imageView, AuthController.getUsername(getActivity()));
        getCurrentPlayer(view);
        return view;
    }

    /**
     * Get the username of the current user and access the database to retrieve player information
     * getPlayer() retrieves username, score, total number of QR codes scanned, highest and lowest
     * scoring QR codes, and a list of the current players QR codes
     * @param view
     */
    public void getCurrentPlayer(View view) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = preferences.getString("login_username", "");

        PlayerDB db = new PlayerDB(new DBConnector());
        QRCodeDB qrCodeDB = new QRCodeDB(new DBConnector());
        TextView usernameText = view.findViewById(R.id.text_username);
        usernameText.setText(username);


        // get player with username
        db.getPlayer(username, new PlayerCallback() {
            @Override
            public void onPlayerCallback(Player player) {
                if (player != null) {

                    // assigning view objects to variables
                    TextView scoreText = view.findViewById(R.id.score_text);
                    TextView totalQRText = view.findViewById(R.id.total_qr_scanned_text);
                    TextView highestQRCode = view.findViewById(R.id.highest_score_text);
                    TextView lowestQRCode = view.findViewById(R.id.lowest_score_text);

                    highestQRCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sortQRList(false);
                        }
                    });
                    lowestQRCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sortQRList(true);
                        }
                    });

                    ImageView upArrow = view.findViewById(R.id.arrow_up);
                    ImageView downArrow = view.findViewById(R.id.arrow_down);

                    upArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sortQRList(false);
                        }
                    });

                    downArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sortQRList(true);
                        }
                    });


                    // set total score and total QQR code count
                    scoreText.setText(String.valueOf(player.getTotalScore()));
                    totalQRText.setText(String.valueOf(player.getTotalQRCodes()));


                    //Get highest/lowest scoring QR codes
                    playerController = new PlayerController(player);
                    playerController.getHighestQRCode(new ScoreCallback() {
                        @Override
                        public void scoreCallback(int score) {
                            highestQRCode.setText(Integer.toString(score));
                        }
                    });
                    playerController.getLowestQRCode(new ScoreCallback() {
                        @Override
                        public void scoreCallback(int score) {
                            lowestQRCode.setText(Integer.toString(score));
                        }
                    });
                    /*QRCode playerHighestQRCode = playerController.getHighestQRCode();
                    if(playerHighestQRCode != null) {
                        highestQRCode.setText(String.valueOf(playerHighestQRCode.getPoints()));
                    }
                    else {
                        highestQRCode.setText("0");
                    }

                    QRCode playerLowestQRCode = playerController.getLowestQRCode();
                    if(playerLowestQRCode != null) {
                        lowestQRCode.setText(String.valueOf(playerLowestQRCode.getPoints()));
                    }
                    else {
                        lowestQRCode.setText("0");
                    }*/


                    //Get ListView of QR codes scanned
                    ArrayList<String> qrCodeNames = player.getQrCodesScanned();
                    Log.d("Profile", qrCodeNames.toString());

                    qrCodeDB.getPlayerQRCodes(player.getUsername(), new QRCodeListCallback() {
                        @Override
                        public void onQRCodeListCallback(ArrayList<QRCode> qrCodeList) {
                            Log.d("Profile", qrCodeList.toString());
                            ListView QRlist = view.findViewById(R.id.list_view_player_qr_codes);
                            QRAdapter = new QRArrayAdapter(getActivity(), qrCodeList);
                            QRlist.setAdapter(QRAdapter);
                            finalDataList = qrCodeList;
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
                                    Intent myIntent = new Intent(getActivity(), QRCodeInfoActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("name", String.valueOf(finalDataList.get(position).getName()));
                                    extras.putString("username", String.valueOf(player.getUsername()));
                                    extras.putBoolean("current_user", true);
                                    myIntent.putExtras(extras);
                                    // go to QRCodeInfoActivity to display the QR code
                                    getActivity().startActivity(myIntent);
                                    QRAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            }
        });


    }

    public void sortQRList(boolean inc) {
        if (finalDataList != null) {
            finalDataList.sort(new Comparator<QRCode>() {
                @Override
                public int compare(QRCode qr1, QRCode qr2) {
                    int result = Integer.compare(qr1.getPoints(), qr2.getPoints());
                    return inc ? result : -result;
                }
            });
            QRAdapter.notifyDataSetChanged();
        }
    }
}
