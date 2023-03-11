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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;

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
                        Log.d(TAG, "Player could not be added!" + e);
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
    public void getPlayer(String username, PlayerCallback callback) {
        //To-do: Implement getPlayer() -> Alex
        //Referenced: https://cloud.google.com/firestore/docs/query-data/get-data#javaandroid_2
        DocumentReference documentReference = collectionReference.document(username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    String phone = documentSnapshot.getString("phone");
                    Bitmap avatar = (Bitmap)documentSnapshot.get("avatar");
                    int totalScore = Objects.requireNonNull(documentSnapshot.getLong("totalScore")).intValue();
                    int totalQRCodes = Objects.requireNonNull(documentSnapshot.getLong("totalQRCodes")).intValue();
                    ArrayList<Map<String, Object>> qrCodesScanned = (ArrayList<Map<String, Object>>) documentSnapshot.get("qrCodesScanned");

                    //Referenced:
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

                            Geolocation coordinates;
                            if(map.get("coordinates") == null) {
                                coordinates = null;
                            }
                            else {
                                coordinates = (Geolocation)map.get("coordinates");
                            }

                            Bitmap locationImage;
                            if(map.get("locationImage") == null) {
                                locationImage = null;
                            }
                            else {
                                locationImage = (Bitmap)map.get("locationImage");
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
                    Player player = new Player(username, phone, totalScore, totalQRCodes, avatar, convertedQRCodes);
                    Log.d(TAG, "Player information retrieved from database");
                    Log.d(TAG, "Player Name: " + player.getUsername() + "\n Score: " + player.getTotalScore());
                    callback.onPlayerCallback(player);
                }
                else {
                    Log.d(TAG, "Player not found in database!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Could not retrieve document reference!" + e);
            }
        });
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
