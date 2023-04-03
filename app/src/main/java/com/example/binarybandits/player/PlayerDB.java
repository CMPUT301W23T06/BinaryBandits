package com.example.binarybandits.player;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.Geolocation;
import com.example.binarybandits.controllers.PlayerController;
import com.example.binarybandits.models.Comment;
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


import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Stores, retrieves, adds, and deletes Player data
 * Outstanding issues:
 *   -It takes a noticeable amount of time to process results (about half a second)
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
        data.put("highestScore", player.getHighestScore());
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
     * Get a Player based on username
     * @param username Current player's username
     */
    public void getPlayer(String username, PlayerCallback callback) {
        //Referenced: https://cloud.google.com/firestore/docs/query-data/get-data#javaandroid_2

        if(username != null && !username.isEmpty()) {
            DocumentReference documentReference = collectionReference.document(username);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Player player;
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String phone = documentSnapshot.getString("phone");
                        Bitmap avatar = (Bitmap) documentSnapshot.get("avatar");
                        int totalScore = Objects.requireNonNull(documentSnapshot.getLong("totalScore")).intValue();
                        int totalQRCodes = Objects.requireNonNull(documentSnapshot.getLong("totalQRCodes")).intValue();
                        //ArrayList<Map<String, Object>> qrCodesScanned = (ArrayList<Map<String, Object>>) documentSnapshot.get("qrCodesScanned");
                        ArrayList<String> qrCodesScanned = (ArrayList<String>) documentSnapshot.get("qrCodesScanned");
                        //ArrayList<QRCode> convertedQRCodes = getPlayerHelper(qrCodesScanned);
                        int highestScore = Objects.requireNonNull(documentSnapshot.getLong("highestScore")).intValue();
                        player = new Player(username, phone, totalScore, totalQRCodes, avatar, qrCodesScanned, highestScore);
                        Log.d(TAG, "Player information retrieved from database");
                        Log.d(TAG, "Player Name: " + player.getUsername() + "\n Score: " + player.getTotalScore());
                    } else {
                        player = null;
                        Log.d(TAG, "Player not found in database!");
                        ;
                    }
                    //Referenced: https://stackoverflow.com/questions/48499310/how-to-return-a-documentsnapshot-as-a-result-of-a-method/48500679#48500679
                    //Author: https://stackoverflow.com/users/5246885/alex-mamo
                    //License: CC BY-SA 4.0.
                    callback.onPlayerCallback(player);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Could not retrieve document reference!" + e);
                }
            });
        }
        else {
            //Pass null to callback if username is empty or null
            callback.onPlayerCallback(null);
        }
    }

    /**
     * Helper function for getPlayer that get a list of a Player's QRCodes
     * @param qrCodesScanned An ArrayList to hold QRCode objects scanned by the Player
     * @return Return a list of the Player's QR codes
     */
    public ArrayList<QRCode> getPlayerHelper(ArrayList<Map<String, Object>> qrCodesScanned) {
        ArrayList<QRCode> convertedQRCodes = new ArrayList<QRCode>();
        if(qrCodesScanned != null) {
            //Create a QRCode based on the map representation generated from reading Firebase DB
            for (int i = 0; i < qrCodesScanned.size(); i++) {
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

                ArrayList<Comment> comments;
                if(map.get("comments") == null) {
                    comments = null;
                }
                else {
                    comments = (ArrayList<Comment>) map.get("comments");
                }
                int numPlayersScannedBy = Integer.parseInt(map.get("numPlayersScannedBy").toString());

                ArrayList<String> playersScannedBy;
                if(map.get("playersScannedBy") == null) {
                    playersScannedBy = null;
                }
                else {
                    playersScannedBy = (ArrayList<String>) map.get("playersScannedBy");
                }

                QRCode qrCode = new QRCode(hash, name, points, scannerUID, coordinates,
                        locationImage, comments, numPlayersScannedBy, playersScannedBy);
                convertedQRCodes.add(qrCode);
            }
        }
        return convertedQRCodes;
    }

    /**
     * Get players that satisfy an inputted query
     * @param query Firebase Firestore query containing results to process
     * @param callback has method containing what to do with queried playerList
     */
    public void getPlayersByQuery(Query query, PlayerListCallback callback) {
        //Referenced: https://stackoverflow.com/questions/72607619/firestore-database-java-add-where-condition-before-get-collection
        ArrayList<Player> playerList = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc: task.getResult()) {
                        Log.d(TAG, doc.getString("username"));
                        if (doc != null) {
                            String username = doc.getString("username");
                            String phone = doc.getString("phone");
                            Bitmap avatar = (Bitmap) doc.get("avatar");
                            int totalScore = Objects.requireNonNull(doc.getLong("totalScore")).intValue();
                            int totalQRCodes = Objects.requireNonNull(doc.getLong("totalQRCodes")).intValue();
                            //ArrayList<Map<String, Object>> qrCodesScanned = (ArrayList<Map<String, Object>>) doc.get("qrCodesScanned");
                            ArrayList<String> qrCodesScanned = (ArrayList<String>) doc.get("qrCodesScanned");
                            //ArrayList<QRCode> convertedQRCodes = getPlayerHelper(qrCodesScanned);
                            int highestScore = Objects.requireNonNull(doc.getLong("highestScore")).intValue();
                            Player player = new Player(username, phone, totalScore, totalQRCodes, avatar, qrCodesScanned, highestScore);
                            playerList.add(player);
                            Log.d(TAG, "Player added!");
                        }
                    }
                    Log.d(TAG, playerList.toString());
                    callback.onPlayerListCallback(playerList);
                }
                else {
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }

    /**
     * Get query containing sorted players by score
     * @return Return the Query containing all players in database sorted by score
     */
    public Query getSortedPlayers() {
        return collectionReference.orderBy("totalScore", Query.Direction.DESCENDING);
    }

    /**
     * Get all Players currently in Players collection in the database
     * @return Return the Query containing all players in the database sorted by username
     */
    public Query getAllPlayers() {
        return collectionReference.orderBy("username");
    }

    /**
     * Get query containing sorted players by highest scoring QR code
     * @return Return the Query containing all players in database sorted by highest scoring QR code
     */
    public Query getHighestQRCodes() {
        return collectionReference.orderBy("highestScore", Query.Direction.DESCENDING);
    }

    /**
     * Searches the database for all players in the given list of usernames
     * @param usernames list of player usernames to search for in the database
     * @return Query result of searching for players in the list of usernames
     */
    public Query searchListOfPlayers(ArrayList<String> usernames) {
        //Change to whereArrayCont
        return collectionReference.whereIn("username", usernames);
    }

    /**
     * Find all players that have scanned a given QR code
     * @param qrCode QR code to find all players that have scanned it
     * @param callback has method containing what to do with list of players with the given QR code
     */
    public void findPlayersWithQRCode(String qrCode, PlayerListCallback callback) {
        ArrayList<Player> playersWithQRCode = new ArrayList<>();
        getPlayersByQuery(getAllPlayers(), new PlayerListCallback() {
            @Override
            public void onPlayerListCallback(ArrayList<Player> playerList) {
                for(int i = 0; i < playerList.size(); i++) {
                    Player player = playerList.get(i);
                    if(player.findQRCodeScanned(qrCode)) {
                        playersWithQRCode.add(player);
                    }
                }
                callback.onPlayerListCallback(playersWithQRCode);
            }
        });
    }

    /**
     * Find all players with a username containing input
     * @param input text to search for in Player usernames
     * @return Query result of searching for a Player with a username matching input
     */
    public Query searchPlayer(String input) {
        //Referenced: https://stackoverflow.com/questions/46568142/google-firestore-query-on-substring-of-a-property-value-text-search
        return collectionReference.orderBy("username").startAt(input).endAt(input + '~');
    }

    /**
     * Update a field in a Player document
     * @param player Player to update
     */
    public void updatePlayer(Player player) {
        collectionReference.document(player.getUsername()).update(
                "username", player.getUsername(),
                "phone", player.getPhone(),
                "totalScore", player.getTotalScore(),
                "totalQRCodes", player.getTotalQRCodes(),
                "playerAvatar", player.getPlayerAvatar(),
                "qrCodesScanned", player.getQrCodesScanned(),
                "highestScore", player.getHighestScore()
        );
    }

    /**
     * Delete a Player from the database. Only used in UI testing
     * @param username Username of Player to delete from database
     */
    public void deletePlayer(String username) {
        collectionReference.document(username).delete();
    }

}
