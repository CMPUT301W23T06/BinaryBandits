package com.example.binarybandits.qrcode;


import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores, retrieves, adds, and deletes QR code data
 */
public class QRCodeDB {
    private final String TAG = "QRCodeDB";
    private FirebaseFirestore db;
    private final CollectionReference collectionReference;

    public QRCodeDB(DBConnector connector) {
        this.db = connector.getDB();
        this.collectionReference = connector.getCollectionReference("QRCodes");
    }

    /**
     *
     * @param qrCode
     */
    public void addQRCode(QRCode qrCode) {
        //Referenced: https://stackoverflow.com/questions/53332471/checking-if-a-document-exists-in-a-firestore-collection
        String name = qrCode.getName();
        DocumentReference documentReference = collectionReference.document(name);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(!document.exists()) {
                        Log.d(TAG, "New QRCode!");
                        addOnSuccess(name, qrCode);
                    }
                    else {
                        Log.d(TAG, "QRCode already in database!");
                    }
                }
            }
        });
    }

    /**
     *
     * @param name
     * @param qrCode
     */
    public void addOnSuccess(String name, QRCode qrCode) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", qrCode.getName());
        data.put("scannerUID", qrCode.getScannerUID());
        data.put("hash", qrCode.getHash());
        data.put("points", qrCode.getPoints());
        //To-do: Add Geolocation to data
        data.put("geolocation", qrCode.getCoordinates());
        data.put("locationImage", qrCode.getLocationImage());
        data.put("comments", qrCode.getComments());
        data.put("numPlayersScannedBy", qrCode.getNumPlayersScannedBy());

        collectionReference
                .document(name)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "QRCode has been added successfully!");
                        Log.d(TAG, "Name: " + qrCode.getName() + "\nPoints: " + qrCode.getPoints());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "QRCode could not be added!" + e.toString());
                    }
                });
    }

    /**
     *
     */
    public void getQRCode(String id, QRCodeCallback callback) {
        DocumentReference documentReference = collectionReference.document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //Player player = documentSnapshot.toObject(Player.class);
                    String scannerUID = documentSnapshot.getString("scannerUID");
                    String hash = documentSnapshot.getString("hash");
                    String name = documentSnapshot.getString("name");
                    int points = Integer.parseInt(documentSnapshot.getString("points"));
                    Geolocation coordinates = (Geolocation) documentSnapshot.get("coordinates");
                    Bitmap locationImage = (Bitmap)documentSnapshot.get("locationImage");
                    ArrayList<String> comments = (ArrayList<String>)documentSnapshot.get("comments");
                    int numPlayersScannedBy = Integer.parseInt(documentSnapshot.getString("numPlayerScannedBy"));

                    QRCode qrCode = new QRCode(hash, name, points, scannerUID, coordinates,
                            locationImage, comments, numPlayersScannedBy);
                    Log.d(TAG, "QR code information retrieved from database");
                    Log.d(TAG, "Name: " + name + "\nPoints: " + points);
                    callback.onQRCodeCallback(qrCode);
                }
                else {
                    Log.d(TAG, "QR code not found in database!");
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
     *
     * @param qrCode
     */
    public void updateQRCode(QRCode qrCode) {
        //To-do: Implement updateQRCode() -> Alex
    }

    /**
     *
     * @param qrCode
     */
    public void deleteQRCode(QRCode qrCode) {
        //To-do: Implement deleteQRCode() -> Alex
        if(qrCode.getNumPlayersScannedBy() == 1) {
            //Delete QRCode from DB if no one else has scanned the QRCode
            collectionReference.document(qrCode.getScannerUID()).delete();
        }
        else {
            //Decrement numPlayersScannedBy
            qrCode.decrementNumPlayersScannedBy();
            //Update QRCode in database
            updateQRCode(qrCode);
        }
    }
}
