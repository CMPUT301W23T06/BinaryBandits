package com.example.binarybandits.qrcode;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.binarybandits.DBConnector;
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

import java.util.HashMap;

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
        String id = qrCode.getScannerUID();
        DocumentReference documentReference = collectionReference.document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(!document.exists()) {
                        Log.d(TAG, "New QRCode!");
                        addOnSuccess(id, qrCode);
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
     * @param id
     * @param qrCode
     */
    public void addOnSuccess(String id, QRCode qrCode) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("Name", qrCode.getName());
        data.put("Hash", qrCode.getHash());
        data.put("Points", qrCode.getPoints());
        //To-do: Add Geolocation to data

        data.put("LocationImage", qrCode.getLocationImage());
        data.put("Comments", qrCode.getComments());
        collectionReference
                .document(id)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "QRCode has been added successfully!");
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

    }
}
