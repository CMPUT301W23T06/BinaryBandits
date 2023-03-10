package com.example.binarybandits.player;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.security.auth.callback.Callback;

/**
 * Stores, retrieves, adds, and deletes Player data
 */
public class PlayerDB {
    private final String TAG = "PlayerDB";
    public Callback callback;
    private FirebaseFirestore db;
    private final CollectionReference collectionReference;

    public interface Callback {
        void onCallback(Player player);
    }
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

    public void addPlayerToDatalist(ArrayList<Player> playerList) {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                playerList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String username = doc.getString("username");
                    String phone = doc.getString("phone");
                    Bitmap avatar = (Bitmap)doc.get("avatar");
                    int totalScore = Objects.requireNonNull(doc.getLong("totalScore")).intValue();
                    int totalQRCodes = Objects.requireNonNull(doc.getLong("totalQRCodes")).intValue();
                    ArrayList<QRCode> qrCodesScanned = new ArrayList<>(); //temporary
                    playerList.add(new Player(username, phone, totalScore, totalQRCodes, avatar, qrCodesScanned));
                }
            }
        });
    }

    /**
     * Get a Player based on username
     * @param username
     */
    public void getPlayer(String username, Callback callback) {
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
                    int totalScore = Objects.requireNonNull(documentSnapshot.getLong("totalScore")).intValue();
                    int totalQRCodes = Objects.requireNonNull(documentSnapshot.getLong("totalQRCodes")).intValue();
                    //ArrayList<QRCode> qrCodesScanned = (ArrayList<QRCode>) documentSnapshot.get("qrCodesScanned");
                    ArrayList<QRCode> qrCodesScanned = new ArrayList<>(); //temporary

                    Player player = new Player(username, phone, totalQRCodes, totalScore, avatar, qrCodesScanned);
                    Log.d(TAG, "Player information retrieved from database");
                    Log.d(TAG, "Player Name: " + player.getUsername() + "\n Phone Number: " + player.getPhone());
                    callback.onCallback(player);
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
    }

    /**
     * Query all player for leaderboard
     */
    public void getAllPlayers() {

    }

    public void getQRCodes(String username) {

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
