package com.example.binarybandits.qrcode;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.binarybandits.DBConnector;
import com.example.binarybandits.Geolocation;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores, retrieves, adds, and deletes QR code data
 * Outstanding issues: N/A
 */
public class QRCodeDB {
    private final String TAG = "QRCodeDB";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private final CollectionReference collectionReference;
    private StorageReference storageRef;
    private StorageReference locationImgsRef;

    /**
     * Constructor for QRCodeDB
     * @param connector connection to the FirebaseFirestore database
     */
    public QRCodeDB(DBConnector connector) {
        this.db = connector.getDB();
        this.storage = connector.getStorage();
        this.collectionReference = connector.getCollectionReference("QRCodes");
        this.storageRef = this.storage.getReference();
        this.locationImgsRef = this.storageRef.child("locationImgs");
    }

    /**
     * Adds a QRCode object as a document in the QR code collection within the database
     * @param qrCode QRCode to add to database
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
                        getQRCode(name, new QRCodeCallback() {
                            @Override
                            public void onQRCodeCallback(QRCode qrCode) {
                                qrCode.incrementNumPlayersScannedBy();
                                updateQRCode(qrCode);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Helper function for addQRCode. Adds the QRCode to database with the name of the QRCode as the key
     * @param name name of the QRCode
     * @param qrCode QRCode object to store on database
     */
    public void addOnSuccess(String name, QRCode qrCode) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", qrCode.getName());
        data.put("scannerUID", qrCode.getScannerUID());
        data.put("hash", qrCode.getHash());
        data.put("points", qrCode.getPoints());
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
     * Get all QRCodes within a given radius of a location. Location can be inputted by the user or the user's current location
     * @param location location to search for nearby QRCodes
     * @param radius radius of search
     * @param callback method containing how the queried QR codes will be used
     */
    public void getQRCodesWithinRadius(ArrayList<Double> location, int radius, QRCodeListCallback callback) {
        ArrayList<QRCode> qrCodeList = new ArrayList<>();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc: task.getResult()) {
                        Log.d(TAG, doc.getString("name"));
                        if (doc != null) {
                            String scannerUID = doc.getString("scannerUID");
                            String hash = doc.getString("hash");
                            String name = doc.getString("name");
                            int points = doc.getLong("points").intValue();
                            ArrayList<Double> coordinates = (ArrayList<Double>) doc.get("coordinates");
                            String locationImage = doc.getString("locationImage");
                            ArrayList<Map<String, Object>> comments = (ArrayList<Map<String, Object>>) doc.get("comments");

                            ArrayList<Comment> convertedComments = getQRCodeHelper(comments);

                            ArrayList<String> playersScannedBy = (ArrayList<String>) doc.get("playersScannedBy");
                            int numPlayersScannedBy = doc.getLong("numPlayersScannedBy").intValue();
                            QRCode qrCode = new QRCode(hash, name, points, scannerUID, coordinates,
                                    locationImage, convertedComments, numPlayersScannedBy, playersScannedBy);
                            if(qrCode.getCoordinates() != null && qrCode.getCoordinates().get(0) >= location.get(0) - radius &&
                                qrCode.getCoordinates().get(1) <= location.get(0) + radius) {
                                    qrCodeList.add(qrCode);
                            }
                        }
                    }
                    Log.d(TAG, qrCodeList.toString());
                    callback.onQRCodeListCallback(qrCodeList);
                }
                else {
                    Log.d(TAG, task.getException().getMessage());
                }
            }
        });
    }

    /**
     * Gets a QRCode from the database based on name
     * @param name name of the QR code to find in database
     * @param callback has method containing what to do with queried QRCode
     */
    public void getQRCode(String name, QRCodeCallback callback) {
        DocumentReference documentReference = collectionReference.document(name);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String scannerUID = documentSnapshot.getString("scannerUID");
                    String hash = documentSnapshot.getString("hash");
                    String name = documentSnapshot.getString("name");
                    int points = documentSnapshot.getLong("points").intValue();
                    ArrayList<Double> coordinates = (ArrayList<Double>) documentSnapshot.get("coordinates");
                    String locationImage = documentSnapshot.getString("locationImage");
                    //ArrayList<Comment> comments = (ArrayList<Comment>) documentSnapshot.get("comments");
                    ArrayList<Map<String, Object>> comments = (ArrayList<Map<String, Object>>) documentSnapshot.get("comments");
                    ArrayList<Comment> convertedComments = getQRCodeHelper(comments);

                    int numPlayersScannedBy = documentSnapshot.getLong("numPlayersScannedBy").intValue();
                    QRCode qrCode = new QRCode(hash, name, points, scannerUID, coordinates,
                            locationImage, convertedComments, numPlayersScannedBy, null);
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
     * Helper function for getQRCode that get a list of a QR code's comments
     * @param comments An ArrayList to hold comments of a QR code
     * @return Return a list of a QR code's comments
     */
    public ArrayList<Comment> getQRCodeHelper(ArrayList<Map<String, Object>> comments) {
        ArrayList<Comment> convertedComments = new ArrayList<Comment>();
        if(comments != null) {
            //Create a QRCode based on the map representation generated from reading Firebase DB
            for (int i = 0; i < comments.size(); i++) {
                Map<String, Object> map = comments.get(i);
                String content = map.get("content").toString();
                String author = map.get("author").toString();
                Comment comment = new Comment(content, author);
                convertedComments.add(comment);
            }
        }
        return convertedComments;
    }

    /**
     * Updates a QRCode's fields in the database
     * @param qrCode QRCode to update
     */
    public void updateQRCode(QRCode qrCode) {
        //To-do: Implement updateQRCode() -> Alex
        collectionReference.document(qrCode.getName())
                .update("name", qrCode.getName(),
                        "scannerUID", qrCode.getScannerUID(),
                        "hash", qrCode.getHash(),
                        "points", qrCode.getPoints(),
                        "coordinates", qrCode.getCoordinates(),
                        "locationImage", qrCode.getLocationImage(),
                        "comments", qrCode.getComments(),
                        "numPlayersScannedBy", qrCode.getNumPlayersScannedBy());
    }

    /**
     * Updates number of players who have scanned a QRCode when a Player deletes a
     * QRCode from their account
     * @param qrCode QRCode to update in database
     */
    public void deleteQRCode(QRCode qrCode) {
        if(qrCode.getNumPlayersScannedBy() >= 1) {
            //Decrement numPlayersScannedBy
            qrCode.decrementNumPlayersScannedBy();
            //Update QRCode in database
            updateQRCode(qrCode);
        }
    }

    /**
     * Adds a location image to the database
     * @param bitmap location image to add to database
     * @param name name of image
     */
    public void addLocationImageToServer(Bitmap bitmap, String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference qrCodeRef = this.locationImgsRef.child(name + ".jpg");
        UploadTask uploadTask = qrCodeRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }


}
