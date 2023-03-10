package com.example.binarybandits.player;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
import com.example.binarybandits.ui.auth.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores, retrieves, adds, and deletes Player data
 */
public class PlayerDB {
    private final String TAG = "PlayerDB";
    private FirebaseFirestore db;
    private final CollectionReference collectionReference;

    /**
     * Retrieve the database and the reference for the Players collection
     * @param connector
     */
    public PlayerDB(DBConnector connector) {
        this.db = connector.getDB();
        this.collectionReference = connector.getCollectionReference("Players");
    }

    /**
     * Adds a new player to the Players collection in the database. A new player with a username
     * already in the database is not added.
     * @param player New player to add to the Players collection in the database
     */
    public void addPlayer(Player player) {
        //Referenced: https://stackoverflow.com/questions/53332471/checking-if-a-document-exists-in-a-firestore-collection
        String username = player.getUsername();
        DocumentReference documentReference = collectionReference.document(username);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(!document.exists()) {
                        Log.d(TAG, "Valid username");
                        addOnSuccess(username, player);
                    }
                    else {
                        Log.d(TAG, "Username is taken!");
                    }
                }
            }
        });
    }

    /**
     * If a username is available, add a player to the database
     * @param username
     * @param player
     */
    public void addOnSuccess(String username, Player player) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", player.getUsername());
        data.put("phone", player.getPhone());
        data.put("playerAvatar", player.getPlayerAvatar());
        data.put("totalScore", player.getTotalScore());
        data.put("totalQRCodes", player.getTotalQRCodes());
        data.put("qrCodesScanned", player.getQrCodesScanned());
        collectionReference
                .document(username)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Player has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Player could not be added!" + e.toString());
                    }
                });
    }

    /**
     * Get a Player based on username
     * @param username
     */
    public void getPlayer(String username) {
        //To-do: Implement getPlayer() -> Alex
        //Referenced: https://cloud.google.com/firestore/docs/query-data/get-data#javaandroid_2
        DocumentReference documentReference = collectionReference.document(username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //Player player = documentSnapshot.toObject(Player.class);
                    String username = documentSnapshot.getString("username");
                    String phone = documentSnapshot.getString("phone");
                    Bitmap avatar = (Bitmap)documentSnapshot.get("avatar");
                    int totalScore = Integer.parseInt(documentSnapshot.getString("totalScore"));
                    int totalQRCodes = Integer.parseInt(documentSnapshot.getString("totalQRCodes"));
                    //ArrayList<QRCode> qrCodesScanned = (ArrayList<QRCode>) documentSnapshot.get("qrCodesScanned");

                    //Player player = new Player(username, phone, avatar, totalScore, totalQRCodes);
                    Log.d(TAG, "Player information retrieved from database");
                }
                else {
                    Log.d(TAG, "Player not found in database!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Could not retrieve document reference!" + e.toString());
            }
        });
        /*
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Player player = task.getResult().toObject(Player.class);
                    Log.d(TAG, "Player information retrieved from database");
                }
                else {
                    Log.d(TAG, "Player not found in database!");
                }
            }
        });*/
    }

    /**
     * Query all player for leaderboard
     */
    public void getAllPlayers() {

    }


    /**
     * Update a field in a Player document
     * @param player
     */
    public void updatePlayer(Player player, String fieldToUpdate, Object newValue) {
        //Alex: I'm not sure if this works. Need to test this method
        collectionReference.document(player.getUsername()).update(fieldToUpdate, newValue);
    }
}
