package com.example.binarybandits;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public class DBConnector {
    private final String TAG = "DBConnector";
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    /**
     *
     */
    public DBConnector() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     *
     * @return
     */
    public FirebaseFirestore getDB() {
        return this.db;
    }

    public CollectionReference getCollectionReference(String collection) {
        return this.db.collection(collection);
    }
}
