package com.example.binarybandits.player;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.Geolocation;
import com.example.binarybandits.models.Player;
import com.example.binarybandits.models.QRCode;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                    //Add a user to the database if their username is unique
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
     * @param username Requested username to associate with a new Player
     * @param player Player object to add to Players collection
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
                        Log.d(TAG, "Player could not be added!" + e);
                    }
                });
    }

    /**
     * Get all Players currently in Players collection in the database
     * @param playerList local copy of the list of Player to update
     * @param callback has method containing what to do with queried playerList
     */
    public void getAllPlayers(ArrayList<Player> playerList, PlayerListCallback callback) {
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
                    ArrayList<Map<String, Object>> qrCodesScanned = (ArrayList<Map<String, Object>>) doc.get("qrCodesScanned");

                    ArrayList<QRCode> convertedQRCodes = getPlayerHelper(qrCodesScanned, totalQRCodes);
                    Player player = new Player(username, phone, totalScore, totalQRCodes, avatar, convertedQRCodes);
                    playerList.add(player);
                }
                callback.onPlayerListCallback(playerList);
            }
        });
    }

    /**
     * Get a Player based on username
     * @param username Current player's username
     */
    public void getPlayer(String username, PlayerCallback callback) {
        //Referenced: https://cloud.google.com/firestore/docs/query-data/get-data#javaandroid_2
        //Author:
        //License:
        DocumentReference documentReference = collectionReference.document(username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Player player;
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    String phone = documentSnapshot.getString("phone");
                    Bitmap avatar = (Bitmap)documentSnapshot.get("avatar");
                    int totalScore = Objects.requireNonNull(documentSnapshot.getLong("totalScore")).intValue();
                    int totalQRCodes = Objects.requireNonNull(documentSnapshot.getLong("totalQRCodes")).intValue();
                    ArrayList<Map<String, Object>> qrCodesScanned = (ArrayList<Map<String, Object>>) documentSnapshot.get("qrCodesScanned");

                    ArrayList<QRCode> convertedQRCodes = getPlayerHelper(qrCodesScanned, totalQRCodes);
                    player = new Player(username, phone, totalScore, totalQRCodes, avatar, convertedQRCodes);
                    Log.d(TAG, "Player information retrieved from database");
                    Log.d(TAG, "Player Name: " + player.getUsername() + "\n Score: " + player.getTotalScore());
                    callback.onPlayerCallback(player);
                }
                else {
                    player = null;
                    Log.d(TAG, "Player not found in database!");;
                }
                callback.onPlayerCallback(player);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Could not retrieve document reference!" + e);
            }
        });
    }

    /**
     * Helper function for getPlayer that get a list of a Player's QRCodes
     * @param qrCodesScanned An ArrayList to hold QRCode objects scanned by the Player
     * @param totalQRCodes Total QR codes to add to qrCodesScanned
     */
    public ArrayList<QRCode> getPlayerHelper(ArrayList<Map<String, Object>> qrCodesScanned, int totalQRCodes) {
        ArrayList<QRCode> convertedQRCodes = new ArrayList<QRCode>();
        if(qrCodesScanned != null) {
            //Create a QRCode based on the map representation generated from reading Firebase DB
            for (int i = 0; i < totalQRCodes; i++) {
                Map<String, Object> map = qrCodesScanned.get(i);
                String name = map.get("name").toString();
                String hash = map.get("hash").toString();
                int points = Integer.parseInt(map.get("points").toString());

                String scannerUID;
                if(map.get("scannerUID") == null) {
                    scannerUID = null;
                }
                else {
                    scannerUID = map.get("scannerUID").toString();
                }

                ArrayList<Double> coordinates;
                if(map.get("coordinates") == null) {
                    coordinates = null;
                }
                else {
                    coordinates = (ArrayList<Double>) map.get("coordinates");
                }

                String locationImage;
                if(map.get("locationImage") == null) {
                    locationImage = null;
                }
                else {
                    locationImage = (String)map.get("locationImage");
                }

                ArrayList<String> comments;
                if(map.get("comments") == null) {
                    comments = null;
                }
                else {
                    comments = (ArrayList<String>)map.get("comments");
                }
                int numPlayersScannedBy = Integer.parseInt(map.get("numPlayersScannedBy").toString());

                QRCode qrCode = new QRCode(hash, name, points, scannerUID, coordinates,
                        locationImage, comments, numPlayersScannedBy);
                convertedQRCodes.add(qrCode);
            }
        }
        return convertedQRCodes;
    }

    /**
     *
     */
    public void getPlayersByScore(ArrayList<Player> playerList, PlayerListCallback callback) {
        //Alex: I am still working on this function!
        //Referenced: https://firebase.google.com/docs/firestore/query-data/order-limit-data
        //Referenced:
        Query sortQuery = collectionReference.orderBy("score", Query.Direction.ASCENDING);
        sortQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                playerList.clear();

                for(QueryDocumentSnapshot doc: task.getResult()) {
                    String username = doc.getString("username");
                    String phone = doc.getString("phone");
                    Bitmap avatar = (Bitmap)doc.get("avatar");
                    int totalScore = Objects.requireNonNull(doc.getLong("totalScore")).intValue();
                    int totalQRCodes = Objects.requireNonNull(doc.getLong("totalQRCodes")).intValue();
                    ArrayList<Map<String, Object>> qrCodesScanned = (ArrayList<Map<String, Object>>) doc.get("qrCodesScanned");

                    ArrayList<QRCode> convertedQRCodes = getPlayerHelper(qrCodesScanned, totalQRCodes);
                    Player player = new Player(username, phone, totalScore, totalQRCodes, avatar, convertedQRCodes);
                    playerList.add(player);
                }
                callback.onPlayerListCallback(playerList);
            }
        });
    }


    /**
     *
     */
    public void searchPlayer(String input) {
    }


    /**
     * Update a field in a Player document
     * @param player Player to update
     */
    public void updatePlayer(Player player) {
        //Alex: I'm not sure if this works. Need to test this method
        collectionReference.document(player.getUsername()).update(
                "username", player.getUsername(),
                "phone", player.getPhone(),
                "totalScore", player.getTotalScore(),
                "totalQRCodes", player.getTotalQRCodes(),
                "playerAvatar", player.getPlayerAvatar(),
                "qrCodesScanned", player.getQrCodesScanned()
        );
    }
}
