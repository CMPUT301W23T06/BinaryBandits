package com.example.binarybandits.common;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 *
 */
public class Database {
    private final String TAG = "Database";
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    /**
     *
     */
    public Database() {
        this.db = new DBConnector().getDB();
    }


}
