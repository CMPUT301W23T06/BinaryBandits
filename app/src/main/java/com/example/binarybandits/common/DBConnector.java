package com.example.binarybandits.common;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public class DBConnector {
    private final String TAG = "DBConnector";
    private FirebaseFirestore db;

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
}
