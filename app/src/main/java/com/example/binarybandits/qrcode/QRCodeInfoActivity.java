package com.example.binarybandits.qrcode;
import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.MainActivity;
import com.example.binarybandits.R;
import com.example.binarybandits.ScoreCallback;
import com.example.binarybandits.controllers.AuthController;
import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Comment;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.player.PlayerCallback;
import com.example.binarybandits.player.PlayerDB;
import com.example.binarybandits.ui.QRedit.LocationImageFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

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

        // initialize variables for adding comments
        ListView commentsView = findViewById(R.id.commentsList);
        ArrayList<Comment> commentsList = new ArrayList<>();
        ArrayList<String> commentsStringList = new ArrayList<>();
        ArrayAdapter commentsAdapter = new ArrayAdapter<>(this, R.layout.comment, commentsStringList);
        commentsView.setAdapter(commentsAdapter);
        FirebaseFirestore db;
        CollectionReference collectionReference;
        DBConnector connector = new DBConnector();
        db = connector.getDB();
        collectionReference = connector.getCollectionReference("QRCodes");

        QRCodeDB db_qr = new QRCodeDB(new DBConnector());
        PlayerDB db_player = new PlayerDB(new DBConnector());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle extras = getIntent().getExtras();

        // get currents users name for commenting
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("login_username", "");

        if (extras != null) {

            String name = extras.getString("name");
            String player_user = extras.getString("username");
            Boolean current_player = extras.getBoolean("current_user");
            /**
             * getQR code will allow dataBase access to the QR code, while using a Callback
             */
            db_qr.getQRCode(name, new QRCodeCallback() {
                /**
                 * Obtain and display appropriate information for the QRCode
                 * @param qrCode
                 *      QRCode object of the QR the user clicked on
                 */
                @Override
                public void onQRCodeCallback(QRCode qrCode) {

                    // declare view objects to use
                    ImageView qr_image = findViewById(R.id.QRImageView);
                    TextView qr_name = findViewById(R.id.qr_code_name);
                    TextView qr_score = findViewById(R.id.qr_code_score);
                    ImageButton delete_button = findViewById(R.id.delete_button);
                    Button commentButton = findViewById(R.id.addCommentBtn);
                    ImageButton view_location_button = findViewById(R.id.location_img_button);
                    ImageButton view_map_button = findViewById(R.id.map_button);
                    EditText textBox = findViewById(R.id.user_comment);

                    // get list of players who have scanned this QR Code
                    ArrayList<String> players = qrCode.getPlayersScannedBy();

                    // bool for testing if user can comment on this qr code
                    Boolean playerHasScanned =  false;

                    // test to see if player has scanned QR Code
                    Integer playersSize = players.size();
                    for(int i = 0; i<playersSize; i++){
                        if(Objects.equals(players.get(i), username)){
                            playerHasScanned = true;
                        }
                    }
                    // set permissions if player does not have QR code
                    if(playerHasScanned == false){
                        commentButton.setEnabled(false);
                        textBox.setEnabled(false);
                        textBox.setHint("Scan this QR code to comment");
                    }

                    // get score and picture of QR code
                    String score = Integer.toString(qrCode.getPoints());
                    String url = qrCode.getImageURL();

                    // display QR code name, score and picture
                    Picasso.get().load(url).into(qr_image);
                    qr_name.setText(name);
                    qr_score.setText(score);

                    // get all comments on QR code from dataBase
                    ArrayList<Comment> comments = qrCode.getComments();
                    Integer commSize = comments.size();
                    for(int i =0; i<commSize; i++){
                        commentsList.add(comments.get(i));
                        commentsStringList.add(commentsList.get(i).getAuthor()+": "+commentsList.get(i).getContent());
                    }
                    commentsAdapter.notifyDataSetChanged();


                    // if we are viewing another persons profile, ensure that user cannot delete
                    // another users QR code
                    Log.d(TAG, current_player.toString() + " is the current player");
                    if (current_player == false){
                        delete_button.setVisibility(View.GONE);
                    }

                    /**
                     * When view_location_button is clicked, send to showLocationImage with QRcode
                     * name and the users username to handle getting bitmap instance of image and
                     * display
                     */
                    view_location_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showLocationImage(v, qrCode, player_user);
                        }
                    });


                    /**
                     * When view_map_button is clicked, send to map fragment via main activity such
                     * that the corresponding QR code is zoomed in on and highlighted in the map
                     */
                    view_map_button.setOnClickListener((new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (qrCode.getCoordinates() != null){
                                Intent myIntent = new Intent(QRCodeInfoActivity.this, MainActivity.class);
                                Bundle extras = new Bundle();
                                extras.putBoolean("Map Page", true);
                                extras.putString("QRCode", qrCode.getName());
                                myIntent.putExtras(extras);
                                startActivity(myIntent);
                            }
                            else {
                                AlertDialog.Builder noLocationPopup = new AlertDialog.Builder(QRCodeInfoActivity.this);
                                // confirm delete message
                                noLocationPopup.setMessage("No geolocation available.")
                                        .setCancelable(true)
                                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = noLocationPopup.create();
                                alertDialog.show();

                            }

                        }
                    }));


                    /**
                     * add comment upon user pressing button
                     * add comment to fireStore database
                     */
                    commentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String commentText = String.valueOf(textBox.getText());
                            // make sure comment is not empty
                            if(commentText.equals("")){
                                Toast.makeText(getApplicationContext(), "Comment can not be empty!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                commentsAdapter.notifyDataSetChanged();
                                textBox.setText("");
                                Comment newComment = new Comment(commentText, username);
                                commentsList.add(newComment);
                                commentsStringList.add(username + ": " + commentText);
                                collectionReference.document(name).update("comments", commentsList);
                            }
                        }
                    });

                    /**
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


                                                    //remove QR code from players in database and locally
                                                    player.removeQRCodeScanned(qrCode.getName());
                                                    db_qr.deleteQRCode(qrCode, player_user);

                                                    player.decrementTotalQRCodes();
                                                    int newScore = player.getTotalScore() - qrCode.getPoints();
                                                    player.setTotalScore(newScore);
                                                    PlayerController controller = new PlayerController(player);
                                                    controller.getHighestQRCode(new ScoreCallback() {
                                                        @Override
                                                        public void scoreCallback(int score) {
                                                            player.setHighestScore(score);
                                                            Log.d("ProfileScore", String.valueOf(player.getHighestScore()));
                                                            db_player.updatePlayer(player);
                                                        }
                                                    });
                                                }
                                            });

                                            //QRCodeInfoActivity.this.finish();
                                            // Send back to profile page with updated QR code list
                                            Toast message = Toast.makeText(QRCodeInfoActivity.this,
                                                    "QRCode has been deleted!", Toast.LENGTH_LONG);
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
            ImageButton playersScannedByButton = findViewById(R.id.other_players_button);
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

        ImageButton back_button = findViewById(R.id.back_button);
        /**
         * A click on back button will take you back to the previous fragment (ie profile page)
         */
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QRCodeInfoActivity.this.finish();
            }
        });


    }

    /**
     * Displays a pop up of the image of the location for a given QR code
     * and user name
     * @param v
     *      current view
     * @param qrCode
     *      current QR code
     */
    private void showLocationImage(View v, QRCode qrCode, String username) {

        String firebase_url_string = "locationImgs/"+qrCode.getName()+username+".jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(firebase_url_string);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Get string of image URL via Firebase
                String imageUrl = uri.toString();
                // Use Picasso to retrieve a bitmap image of URL
                Picasso.get()
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        // display image of location via fragment
                        new LocationImageFragment(bitmap).show(getSupportFragmentManager(), "show");
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            // if no image is associated with the QRcode and user, display alert dialogue stating
            // no image was saved.
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertDialog.Builder noLocationPopup = new AlertDialog.Builder(QRCodeInfoActivity.this);
                // pop up message stating no location image stored
                noLocationPopup.setMessage("No location image available")
                        .setCancelable(true)
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = noLocationPopup.create();
                alertDialog.show();
            }
        });

    }




}
