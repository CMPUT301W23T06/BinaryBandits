package com.example.binarybandits.qrcode;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.MainActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.squareup.picasso.Picasso;

/**
 * QRCodeInfoActivity gets the name of a QR code to display and displays the QR codes score,
 * visual representation
 * (to add) comments, and navigates to other features such as map page and similar players page.
 * Gets information from caller about state of user viewing profile and ensures a user cannot
 * delete another players QR code from their account
 */
public class QRCodeInfoActivity extends AppCompatActivity {

    /**
     * Accesses bundles from instance and handles events accordingly - if user is viewing another
     * persons QR code, the delete button will not be a clickable object
     * Get necessary information of QR code using the string ID name that was passed through the bundle
     * and display such information
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_qrpage);


        QRCodeDB db_qr = new QRCodeDB(new DBConnector());
        PlayerDB db_player = new PlayerDB(new DBConnector());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            String name = extras.getString("name");
            String player_user = extras.getString("username");
            Boolean current_player = extras.getBoolean("current_user");
            /*
             * getQR code will allow database access to the QR code, while using a Callback
             */
            db_qr.getQRCode(name, new QRCodeCallback() {
                /**
                 * Obtain and display appropriate information for the QRCode
                 * @param qrCode
                 *      QRcode object of the QR the user clicked on
                 */
                @Override
                public void onQRCodeCallback(QRCode qrCode) {

                    // declare view objects to use
                    ImageView qr_image = findViewById(R.id.QRImageView);
                    TextView qr_name = findViewById(R.id.qr_code_name);
                    TextView qr_score = findViewById(R.id.qr_code_score);
                    ImageButton delete_button = findViewById(R.id.delete_button);

                    // get score and picture of QR code
                    String score = Integer.toString(qrCode.getPoints());
                    String url = qrCode.getImageURL();

                    // display QR code name, score and picture
                    Picasso.get().load(url).into(qr_image);
                    qr_name.setText(name);
                    qr_score.setText(score);


                    // if we are viewing another persons profile, ensure that user cannot delete
                    // another users QR code
                    if (!current_player){
                        delete_button.setVisibility(View.GONE);
                    }


                    /*
                     * If current user clicks delete button, confirm message is prompted. If confirmed,
                     * QR code will be deleted from database and user profile page. After deletion,
                     * user is taken back to their profile page with updated list
                     */
                    delete_button.setOnClickListener(new View.OnClickListener() {
                        /**
                         * If current user clicks delete button, confirm message is prompted. If confirmed,
                         * QR code will be deleted from database and user profile page. After deletion,
                         * user is taken back to their profile page with updated list
                         * @param view The delete button that was clicked.
                         */
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeInfoActivity.this);
                            // confirm delete message
                            builder.setMessage("Are you sure you want to delete this QR code from your profile?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        /**
                                         * After confirm, delete QR code from users database and send back to
                                         * profile page
                                         * @param dialog the dialog that received the click
                                         * @param id yes button
                                         */
                                        public void onClick(DialogInterface dialog, int id) {
                                            db_player.getPlayer(player_user, new PlayerCallback() {
                                                @Override
                                                public void onPlayerCallback(Player player) {
                                                    //Update highest score if QR code to delete is the highest scoring QR code
                                                    int highestScore = player.getHighestScore();

                                                    //remove QR code from players in database and locally
                                                    player.removeQRCodeScanned(qrCode);
                                                    if(qrCode.getPoints() == highestScore) {
                                                        //Find second highest score
                                                        PlayerController playerController = new PlayerController(player);
                                                        QRCode newHighestQRCode = playerController.getHighestQRCode();
                                                        int newHighestScore = 0;
                                                        if(newHighestQRCode != null) {
                                                            newHighestScore = newHighestQRCode.getPoints();
                                                        }
                                                        //Set highest score to second highest scoring QR code after deleting the highest scoring QR code
                                                        player.setHighestScore(newHighestScore);
                                                    }
                                                    db_qr.deleteQRCode(qrCode, player_user);

                                                    player.decrementTotalQRCodes();
                                                    int newScore = player.getTotalScore() - qrCode.getPoints();
                                                    player.setTotalScore(newScore);
                                                    db_player.updatePlayer(player);
                                                }
                                            });

                                            QRCodeInfoActivity.this.finish();
                                            // Send back to profile page with updated QR code list
                                            // BUG: back to profile page shows home screen selected on bottom navigation
                                            Toast message = Toast.makeText(QRCodeInfoActivity.this, "QRCode has been deleted!", Toast.LENGTH_LONG);
                                            message.show();
                                            Intent myIntent = new Intent(QRCodeInfoActivity.this, MainActivity.class);
                                            Bundle extras = new Bundle();
                                            extras.putBoolean("Deleted QR code", true);
                                            myIntent.putExtras(extras);
                                            startActivity(myIntent);
                                        }
                                    })
                                    // if user declines confirm delete message, do nothing
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });
                }
            });

            //Get other players that have scanned a QRCode
            Button playersScannedByButton = findViewById(R.id.other_players_button);
            playersScannedByButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(QRCodeInfoActivity.this, QRCodeScannedByActivity.class);

                    Bundle extras = new Bundle();
                    extras.putString("name", name);
                    myIntent.putExtras(extras);
                    startActivity(myIntent);
                }
            });
        } else {
            System.out.println("error");
        }

        ScrollView scrollView = findViewById(R.id.scroll_comment);


        ImageButton back_button = findViewById(R.id.back_button);
        /*
         * A click on back button will take you back to the previous fragment (ie profile page)
         */
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QRCodeInfoActivity.this.finish();
            }
        });
    }
}
